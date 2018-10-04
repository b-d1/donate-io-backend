package com.example.donateio.service;

import com.example.donateio.model.Donation;
import com.example.donateio.model.DonationBody;
import org.springframework.data.domain.Page;


public interface DonationService {

    Donation createDonation(DonationBody donation);

    Donation createContractDonation(DonationBody donation);

    Page<Donation> getDonationsByDonationCampaign(Long donationCampaignId);
    Page<Donation> getDonationsByDonationCampaignContract(Long donationCampaignId);

    Page<Donation> getDonationsByUser(Long userId);

    Donation getDonationById(Long id);
}
