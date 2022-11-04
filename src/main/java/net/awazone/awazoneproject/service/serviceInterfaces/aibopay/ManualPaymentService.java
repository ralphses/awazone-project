package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ManualPaymentService {

    ResponseMessage getAllPayments(Integer page);

    ResponseMessage getByReference(String reference);

    ResponseMessage deletePayment(String reference);
    ResponseMessage newPayment(String email, String paymentPurpose, MultipartFile file);

    FileUploadResponse getByReferenceDownload(String reference, HttpServletRequest httpServletRequest);
}
