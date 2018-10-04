package com.example.donateio.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DonationCampaignNotFoundException extends RuntimeException {

    public DonationCampaignNotFoundException(String message) {
        super(message);
    }

}
