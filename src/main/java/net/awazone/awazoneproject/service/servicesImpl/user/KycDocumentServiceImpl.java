package net.awazone.awazoneproject.service.servicesImpl.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.controller.exception.CustomFileNotFoundException;
import net.awazone.awazoneproject.controller.exception.KycDocumentException;
import net.awazone.awazoneproject.controller.exception.ResponseMessage;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.kyc.KycDocument;
import net.awazone.awazoneproject.repository.user.KycDocumentRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.KycDocumentService;
import net.awazone.awazoneproject.utility.files.FileStorage;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static net.awazone.awazoneproject.model.userService.kyc.KycStatus.APPROVED;
import static net.awazone.awazoneproject.model.userService.kyc.KycStatus.PENDING;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KycDocumentServiceImpl implements KycDocumentService {

    private final KycDocumentRepository kycDocumentRepository;
    private final AwazoneUserService awazoneUserService;
    @Autowired
    private FileStorage fileStorage;

    @Override
    public FileUploadResponse registerNewKyc(Long userId, MultipartFile file) {

        AwazoneUser awazoneUser = awazoneUserService.findAppUserById(userId);

        Optional<KycDocument> old = kycDocumentRepository.findByAwazoneUser(awazoneUser);
        if(old.isPresent()) {
            throw new KycDocumentException("User already verified");
        }

        String fileName = fileStorage.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/user/kyc/download")
                .queryParamIfPresent("username", Optional.ofNullable(awazoneUser.getAwazoneUserContact().getEmail()))
                .toUriString();

        //Attach this to user
        String fileContentType = file.getContentType();
        long fileSize = file.getSize();

        kycDocumentRepository.save(
                KycDocument.builder()
                        .documentName(fileName)
                        .awazoneUser(awazoneUser)
                        .dateUploaded(LocalDateTime.now())
                        .documentType(fileContentType)
                        .kycStatus(PENDING)
                        .size(fileSize)
                        .downloadUri(fileDownloadUri)
                        .build()
        );
        return new FileUploadResponse(fileName, fileDownloadUri, fileContentType, fileSize, null);
    }

    @Override
    public FileUploadResponse findKycByUserUsername(String username, HttpServletRequest httpServletRequest) {
        AwazoneUser awazoneUser = findKycByUserNameOrEmail(username);

        KycDocument kycDocument = kycDocumentRepository.findByAwazoneUser(awazoneUser)
                .orElseThrow(() -> new CustomFileNotFoundException("No KYC document found"));

        Resource kycDocumentResource = fileStorage.loadFileAsResource(kycDocument.getDocumentName());

        try {
            AtomicReference<String> contentType = new AtomicReference<>(httpServletRequest.getServletContext().getMimeType(kycDocumentResource.getFile().getAbsolutePath()));
            if(contentType.get() == null) contentType.set("application/octet-stream");

            return new FileUploadResponse(kycDocumentResource.getFilename(), "", contentType.get(), 0, kycDocumentResource);

        } catch (IOException e) {
            log.info("Could not determine file type");
            throw new CustomFileNotFoundException("Could not determine file type");
        }

    }

    @Override
    public ResponseMessage deleteKycByUserId(Long userId) {

        Optional<KycDocument> kycDocument = kycDocumentRepository.findByAwazoneUser(awazoneUserService.findAppUserById(userId));

        if(kycDocument.isPresent()) {
            KycDocument document = kycDocument.get();
            try {
                fileStorage.deleteFile(document.getDocumentName());
                kycDocumentRepository.delete(document);

            } catch (IOException e) {
                throw new CustomFileNotFoundException("No Kyc document for user with this ID" + userId);
            }
        }
        return ResponseMessage.builder()
                .message("Document delete successful")
                .httpStatus(OK)
                .build();
    }

    @Override
    public List<KycDocument> findAllUserKyc(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Order.by("dateUploaded")));
        return kycDocumentRepository.findAll(pageable).getContent();
    }

    @Override
    public ResponseMessage getKycStatus(Long kycId) {
        return new ResponseMessage(findKycById(kycId).getKycStatus().name(), OK);
    }

    @Override
    public KycDocument findKycById(Long kycId) {
        return kycDocumentRepository
                .findById(kycId).orElseThrow(() -> new CustomFileNotFoundException("KYC with this does not exist"));
    }

    @Override
    public ResponseMessage verifyUserKyc(Long kycId) {
        KycDocument kycDocument = findKycById(kycId);
        kycDocument.setKycStatus(APPROVED);
        kycDocument.setDateVerified(LocalDateTime.now());
        return new ResponseMessage("KYC Approved", OK);
    }

    @Override
    public KycDocument findKycByUserEmail(String email) {
        return kycDocumentRepository
                .findByAwazoneUser(findKycByUserNameOrEmail(email))
                .orElseThrow(() -> new CustomFileNotFoundException("No Kyc document for this User"));
    }

    @Override
    public ResponseMessage deleteKycByUsername(String userName) {
        Optional<KycDocument> kycDocument = kycDocumentRepository
                .findByAwazoneUser(findKycByUserNameOrEmail(userName));
        kycDocument.ifPresent(kycDocumentRepository::delete);
        return new ResponseMessage("Document deleted successfully", OK);
    }

    private AwazoneUser findKycByUserNameOrEmail(String username) {
        return awazoneUserService.findAppUserByUsername(username);
    }

}
