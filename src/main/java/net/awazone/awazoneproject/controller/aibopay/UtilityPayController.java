package net.awazone.awazoneproject.controller.aibopay;

import lombok.RequiredArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.mobileUtility.BuyAirtimeRequest;
import net.awazone.awazoneproject.model.mobileUtility.BuyDataRequest;
import net.awazone.awazoneproject.service.servicesImpl.UtilityPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/aibopay/utility")
public class UtilityPayController {

    private final UtilityPaymentService utilityPaymentService;

    @GetMapping(path = "/data-bundles")
    public ResponseEntity<ResponseMessage> dataBundles() {

        return ResponseEntity
                .ok(new ResponseMessage(
                        "success",
                        HttpStatus.OK,
                        Map.of("response", utilityPaymentService.getAvailableData())));
    }

    @PostMapping(path = "/buy-data")
    @PreAuthorize("#buyDataRequest.email == authentication.principal")
    public ResponseEntity<ResponseMessage> buyData(@RequestBody @Valid BuyDataRequest buyDataRequest) {

        return ResponseEntity.ok(new ResponseMessage("success", HttpStatus.OK, Map.of("status", utilityPaymentService.buyData(buyDataRequest))));
    }

    @PostMapping(path = "/buy-airtime")
    @PreAuthorize("#buyAirtimeRequest.email == authentication.principal")
    public ResponseEntity<ResponseMessage> buyAirtime(@RequestBody @Valid BuyAirtimeRequest buyAirtimeRequest) {

        return ResponseEntity.ok(new ResponseMessage("success", HttpStatus.OK, Map.of("status", utilityPaymentService.buyAirtime(buyAirtimeRequest))));
    }


}
