package com.example.donateio.service.impl;

import com.example.donateio.jpa.DonationCampaignContractRepository;
import com.example.donateio.jpa.InstitutionRepository;
import com.example.donateio.model.*;
import com.example.donateio.jpa.DonationCampaignRepository;
import com.example.donateio.service.DonationCampaignService;
import com.example.donateio.service.DonationCampaignService;
import com.example.donateio.service.ImageService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


@Service
public class DonationCampaignServiceImpl implements DonationCampaignService {

    @Autowired
    public DonationCampaignRepository donationCampaignRepository;

    @Autowired
    public DonationCampaignContractRepository donationCampaignContractRepository;

    @Autowired
    public InstitutionRepository institutionRepository;

    public ImageService imageService;

    @Autowired
    public DonationCampaignServiceImpl(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public DonationCampaign createDonationCampaign(DonationCampaignBody campaign) throws Exception {

        String extendedFileName = UUID.randomUUID().toString();
        extendedFileName = extendedFileName.replace("-", "");
        extendedFileName = extendedFileName + campaign.fileName;

        String filePath = "donationCampaigns/" + extendedFileName;
        System.out.println("EXTENDED FILE PATH");
        System.out.println(filePath);

        String result = this.imageService.uploadImage(campaign.image, filePath);

        if(result.isEmpty()) {
            throw new Exception("Image has not been uploaded");
        }

        Institution institution = institutionRepository.findById(campaign.institutionId).orElseThrow(() -> new Exception("Institution with id " + campaign.institutionId +  " was not found"));

        DonationCampaign donationCampaign = new DonationCampaign();
        donationCampaign.name = campaign.name;
        donationCampaign.address = campaign.address;
        donationCampaign.website = campaign.website;
        donationCampaign.email = campaign.email;
        donationCampaign.description = campaign.description;
        donationCampaign.institution = institution;
        donationCampaign.image = extendedFileName;
        donationCampaign.totalReceived = "0x0";

        return donationCampaignRepository.save(donationCampaign);

    }

    @Override
    public DonationCampaignContract createDonationCampaignContract(DonationCampaignBody campaign) throws Exception {
        String extendedFileName = UUID.randomUUID().toString();
        extendedFileName = extendedFileName.replace("-", "");
        extendedFileName = extendedFileName + campaign.fileName;

        String filePath = "donationCampaigns/" + extendedFileName;
        System.out.println("EXTENDED FILE PATH");
        System.out.println(filePath);

        String result = this.imageService.uploadImage(campaign.image, filePath);

        if(result.isEmpty()) {
            throw new Exception("Image has not been uploaded");
        }

        Institution institution = institutionRepository.findById(campaign.institutionId).orElseThrow(() -> new Exception("Institution with id " + campaign.institutionId +  " was not found"));

        DonationCampaignContract donationCampaign = new DonationCampaignContract();
        donationCampaign.name = campaign.name;
        donationCampaign.address = campaign.address;
        donationCampaign.website = campaign.website;
        donationCampaign.email = campaign.email;
        donationCampaign.description = campaign.description;
        donationCampaign.institution = institution;
        donationCampaign.image = extendedFileName;
        donationCampaign.totalReceived = "0x0";
        donationCampaign.fundingGoal = campaign.fundingGoal;
        donationCampaign.timeGoal = campaign.timeGoal;
        donationCampaign.txid = campaign.txid;
        donationCampaign.campaignStatus = Enums.CampaignStatus.Active;

        return donationCampaignContractRepository.save(donationCampaign);
    }

    @Override
    public Iterable<DonationCampaign> getDonationCampaigns() {
        return donationCampaignRepository.findAll();
    }

    @Override
    public Iterable<DonationCampaignContract> getDonationCampaignsContract() {
        return donationCampaignContractRepository.findAll();
    }

    @Override
    public Page<DonationCampaign> getDonationCampaignsByInstitutionId(Long id) {
         return donationCampaignRepository.findByInstitutionId(id, PageRequest.of(0, 20));
    }

    @Override
    public Page<DonationCampaignContract> getContractDonationCampaignsByInstitutionId(Long id) {
        return donationCampaignContractRepository.findByInstitutionId(id, PageRequest.of(0, 20));
    }

    @Override
    public DonationCampaignContract getContractDonationById(Long id) {
        return this.donationCampaignContractRepository.findById(id).get();
    }

    @Override
    public DonationCampaignContract changeDonationCampaignContractStatus(Long id, Enums.CampaignStatus campaignStatus) {
        DonationCampaignContract donationCampaignContract = getContractDonationById(id);
        donationCampaignContract.campaignStatus = campaignStatus;
        return this.donationCampaignContractRepository.save(donationCampaignContract);
    }

    @Override
    public DonationCampaign getDonationById(Long id) {
        return this.donationCampaignRepository.findById(id).get();
    }
}
