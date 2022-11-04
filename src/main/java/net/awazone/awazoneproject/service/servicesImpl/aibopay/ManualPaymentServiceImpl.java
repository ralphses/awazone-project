package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.CustomInvalidParamException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.ManualPayment;
import net.awazone.awazoneproject.repository.aibopay.ManualPaymentRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.ManualPaymentService;
import net.awazone.awazoneproject.utility.files.FileStorage;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus.PENDING;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class ManualPaymentServiceImpl implements ManualPaymentService {

    private final ManualPaymentRepository manualPaymentRepository;
    private final FileStorage fileStorage;

    @Override
    public ResponseMessage getAllPayments(Integer page) {
        List<ManualPayment> payments = manualPaymentRepository
                .findAll(PageRequest.of(page-1, 10)).toList();

        return new ResponseMessage("success", OK, Map.of("payments", payments));
    }

    @Override
    public ResponseMessage getByReference(String reference) {
        return new ResponseMessage("Success", OK, Map.of("payment", getManualPayment(reference)));
    }

    @Override
    public ResponseMessage deletePayment(String reference) {

        ManualPayment manualPayment = getManualPayment(reference);

        manualPaymentRepository.delete(manualPayment);
        return new ResponseMessage("Payment deleted successfully", OK, null);
    }

    @Override
    public ResponseMessage newPayment(String username, String paymentPurpose, MultipartFile file) {

        String reference = UUID.nameUUIDFromBytes((paymentPurpose+username+LocalDateTime.now()).getBytes()) + ".jpg";

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/aibopay/manual/get/one/download")
                .queryParam("reference", reference)
                .toUriString();

        //TOdo: Save payment
        fileStorage.storeFile(file, reference);

        ManualPayment manualPayment = ManualPayment.builder()
                .paymentPurpose(paymentPurpose)
                .imageUrl(fileDownloadUri)
                .submittedOn(LocalDateTime.now())
                .reference(reference)
                .transactionStatus(PENDING)
                .emailAddress(username)
                .build();

        manualPaymentRepository.save(manualPayment);
        return new ResponseMessage("success", OK, Map.of("status", "upload success"));
    }

    @Override
    public FileUploadResponse getByReferenceDownload(String reference, HttpServletRequest httpServletRequest) {
        return fileStorage.getFileUploadResponse(httpServletRequest, reference);
    }

    private ManualPayment getManualPayment(String reference) {
        return manualPaymentRepository.findByReference(reference)
                .orElseThrow(() -> new CustomInvalidParamException("Invalid payment reference " + reference));
    }
}
