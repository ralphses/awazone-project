package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;

public interface UtilityBillProcessor {

    ResponseMessage buyAirtime(String network, double amount, String destinationPhoneNumber);
    ResponseMessage buyData(String network, double amount, String destinationPhoneNumber);
}
