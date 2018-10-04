package com.example.donateio.service.impl;

import com.example.donateio.jpa.DonationCampaignContractRepository;
import com.example.donateio.jpa.DonationCampaignRepository;
import com.example.donateio.jpa.DonationRepository;
import com.example.donateio.jpa.UserRepository;
import com.example.donateio.model.*;
import com.example.donateio.model.exceptions.DonationCampaignNotFoundException;
import com.example.donateio.model.exceptions.UserNotFoundException;
import com.example.donateio.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    public DonationCampaignRepository donationCampaignRepository;

    @Autowired
    public DonationCampaignContractRepository donationCampaignContractRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public DonationRepository donationRepository;

    @Override
    public Donation createDonation(DonationBody donationBody) {

        Donation donation = new Donation();

        User user = userRepository.findByEmail(donationBody.userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + donationBody.userEmail + " was not found!"));
        DonationCampaign donationCampaign = donationCampaignRepository.findByName(donationBody.donationCampaignName).orElseThrow(() -> new DonationCampaignNotFoundException("Donation campaign with name: " + donationBody.donationCampaignName + " was not found!"));

        donation.user = user;
        donation.donationCampaign = donationCampaign;
        donation.addressFrom = donationBody.addressFrom;
        donation.addressTo = donationBody.addressTo;
        donation.amount = donationBody.amount;
        donation.fee = donationBody.fee;
        donation.description = donationBody.description;
        donation.transactionId = donationBody.transactionId;
        donation.createdOn = new Date();

        String donationAmount = donation.amount.substring(2);
        String totalReceivedAmount = donationCampaign.totalReceived.substring(2);

        BigInteger amountBi = new BigInteger(donationAmount, 16);
        BigInteger totalReceived = new BigInteger(totalReceivedAmount, 16);
        totalReceived = totalReceived.add(amountBi);

        donationCampaign.totalReceived = "0x" + totalReceived.toString(16);

        donationCampaignRepository.save(donationCampaign);

        return donationRepository.save(donation);
    }

    @Override
    public Donation createContractDonation(DonationBody donationBody) {

        Donation donation = new Donation();

        User user = userRepository.findByEmail(donationBody.userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + donationBody.userEmail + " was not found!"));
        DonationCampaignContract donationCampaign = donationCampaignContractRepository.findByName(donationBody.donationCampaignName).orElseThrow(() -> new DonationCampaignNotFoundException("Donation campaign with name: " + donationBody.donationCampaignName + " was not found!"));

        donation.user = user;
        donation.donationCampaignContract = donationCampaign;
        donation.addressFrom = donationBody.addressFrom;
        donation.addressTo = donationBody.addressTo;
        donation.amount = donationBody.amount;
        donation.fee = donationBody.fee;
        donation.description = donationBody.description;
        donation.transactionId = donationBody.transactionId;
        donation.createdOn = new Date();

        String donationAmount = donation.amount.substring(2);
        String totalReceivedAmount = donationCampaign.totalReceived.substring(2);

        BigInteger amountBi = new BigInteger(donationAmount, 16);
        BigInteger totalReceived = new BigInteger(totalReceivedAmount, 16);
        totalReceived = totalReceived.add(amountBi);

        donationCampaign.totalReceived = "0x" + totalReceived.toString(16);

        donationCampaignContractRepository.save(donationCampaign);

        return donationRepository.save(donation);
    }

    @Override
    public Page<Donation> getDonationsByDonationCampaign(Long donationCampaignId) {
        return donationRepository.findByDonationCampaignId(donationCampaignId, PageRequest.of(0, 20));
    }

    @Override
    public Page<Donation> getDonationsByDonationCampaignContract(Long donationCampaignId) {
        return donationRepository.findByDonationCampaignContractId(donationCampaignId, PageRequest.of(0, 20));
    }

    @Override
    public Page<Donation> getDonationsByUser(Long userId) {
        return donationRepository.findByUserId(userId, PageRequest.of(0, 20));
    }

    @Override
    public Donation getDonationById(Long id) {
        return null;
    }
}
