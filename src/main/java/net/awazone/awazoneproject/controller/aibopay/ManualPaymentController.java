package net.awazone.awazoneproject.controller.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.ManualPaymentService;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/aibopay/manual")
public class ManualPaymentController {

    private final ManualPaymentService manualPaymentService;

    @GetMapping("/get/all/{page}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_GET_ALL_MANUAL_PAY')")
    public ResponseEntity<ResponseMessage> getAllPayments(@PathVariable @NotBlank Integer page) {
        return ResponseEntity.ok(manualPaymentService.getAllPayments(page));
    }

    @GetMapping("/get/one")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_GET_ONE_MANUAL_PAY')")
    public ResponseEntity<ResponseMessage> getOnePayment(@RequestParam("reference") String reference) {
        return ResponseEntity.ok(manualPaymentService.getByReference(reference));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_VIEW_DOCS','ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/one/download")
    public ResponseEntity<Resource> downloadKycDocument(@RequestParam("reference") String reference,
                                                        HttpServletRequest httpServletRequest) {
        FileUploadResponse paymentDocs = manualPaymentService.getByReferenceDownload(reference, httpServletRequest);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(paymentDocs.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + paymentDocs.getFileName() + "\"")
                .body(paymentDocs.getResource());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_DELETE_MANUAL_PAYMENT')")
    public ResponseEntity<ResponseMessage> deletePayment(@RequestParam("reference") String reference) {
        return ResponseEntity.ok(manualPaymentService.deletePayment(reference));
    }

    @PostMapping(value = "/new-manual-payment", consumes = { "multipart/form-data" })
    public ResponseEntity<ResponseMessage> newManualPayment(
            @RequestParam("email") @NotBlank String email,
            @RequestParam("paymentPurpose") @NotBlank String paymentPurpose,
            @RequestPart("file") @NotNull MultipartFile file) {
        return ResponseEntity.ok(manualPaymentService.newPayment(email, paymentPurpose, file));
    }
}
