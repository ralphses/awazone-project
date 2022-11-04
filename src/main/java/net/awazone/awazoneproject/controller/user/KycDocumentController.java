package net.awazone.awazoneproject.controller.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.user.kyc.KycDocument;
import net.awazone.awazoneproject.service.serviceInterfaces.user.KycDocumentService;
import net.awazone.awazoneproject.utility.files.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/user/kyc")
public class KycDocumentController {
    private final KycDocumentService kycDocumentService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_UPLOAD_DOCS','ROLE_SUPER:ADMIN')")
    @PostMapping(path = "/upload/{userId}")
    public ResponseEntity<ResponseMessage> uploadKycDocument(@PathVariable Long userId, MultipartFile file) {

        FileUploadResponse fileUploadResponse = kycDocumentService.registerNewKyc(userId, file);
        return ResponseEntity.ok(new ResponseMessage("success", HttpStatus.OK, Map.of("response", fileUploadResponse)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_VIEW_DOCS','ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/download")
    public ResponseEntity<Resource> downloadKycDocument(@RequestParam("username") String username, HttpServletRequest httpServletRequest) {
        FileUploadResponse kycByUserUsername = kycDocumentService.findKycByUserUsername(username, httpServletRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(kycByUserUsername.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + kycByUserUsername.getFileName() + "\"")
                .body(kycByUserUsername.getResource());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DELETE_DOCS','ROLE_SUPER:ADMIN')")
    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<ResponseMessage> deleteKyc(@PathVariable Long userId) {
        return ResponseEntity.ok(kycDocumentService.deleteKycByUserId(userId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_DOCS','ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/all/{page}")
    public ResponseEntity<ResponseMessage> findAllDocument(@PathVariable int page) {

        List<KycDocument> allUserKyc = kycDocumentService.findAllUserKyc(page);
        return ResponseEntity.ok(new ResponseMessage("success", HttpStatus.OK, Map.of("response", allUserKyc)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_READ_KYC_STATUS','ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/status/{kycId}")
    public ResponseEntity<ResponseMessage> kycStatus(@PathVariable Long kycId) {
        return ResponseEntity.ok(kycDocumentService.getKycStatus(kycId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_APPROVE_DOC','ROLE_SUPER:ADMIN')")
    @PutMapping(path = "/update/approve/{kycId}")
    public ResponseEntity<ResponseMessage> approve(@PathVariable Long kycId) {
        return ResponseEntity.ok(kycDocumentService.verifyUserKyc(kycId));
    }

    @Secured({"ROLE_USER", "ROLE_GET_DOC_BY_USERNAME", "ROLE_SUPER:ADMIN"})
    @PreAuthorize("#email == authentication.getPrincipal()")
    @GetMapping(path = "/get/email")
    public KycDocument getWithUsername(@RequestParam String email) {
        return kycDocumentService.findKycByUserEmail(email);
    }

    @Secured({"ROLE_USER", "ROLE_DELETE_DOC_BY_USERNAME", "ROLE_SUPER:ADMIN"})
    @PreAuthorize("#email == authentication.getPrincipal()")
    @DeleteMapping(path = "/delete/email")
    public ResponseEntity<ResponseMessage> deleteKycByUserEmail(@RequestParam String email) {
        return ResponseEntity.ok(kycDocumentService.deleteKycByUsername(email));
    }
}
