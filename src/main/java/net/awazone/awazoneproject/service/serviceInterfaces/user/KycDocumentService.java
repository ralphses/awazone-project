package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.controller.exception.ResponseMessage;
import net.awazone.awazoneproject.model.userService.kyc.KycDocument;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface KycDocumentService {
    ResponseMessage deleteKycByUserId(Long userId);
    List<KycDocument> findAllUserKyc(int page);
    ResponseMessage getKycStatus(Long kycId);
    KycDocument findKycById(Long kycId);
    ResponseMessage verifyUserKyc(Long kycId);
    KycDocument findKycByUserEmail(String email);
    ResponseMessage deleteKycByUsername(String userName);
    FileUploadResponse findKycByUserUsername(String username, HttpServletRequest httpServletRequest);
    FileUploadResponse registerNewKyc(Long userId, MultipartFile file);
}
