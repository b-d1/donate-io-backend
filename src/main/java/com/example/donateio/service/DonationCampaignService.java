package com.example.donateio.service;

import com.example.donateio.model.DonationCampaign;
import com.example.donateio.model.DonationCampaignBody;
import com.example.donateio.model.DonationCampaignContract;
import com.example.donateio.model.Enums;
import org.springframework.data.domain.Page;

public interface DonationCampaignService {

    DonationCampaign createDonationCampaign(DonationCampaignBody campaign) throws Exception;
    DonationCampaignContract createDonationCampaignContract(DonationCampaignBody campaign) throws Exception;

    Iterable<DonationCampaign> getDonationCampaigns();
    Iterable<DonationCampaignContract> getDonationCampaignsContract();


    Page<DonationCampaign> getDonationCampaignsByInstitutionId(Long id);
    Page<DonationCampaignContract> getContractDonationCampaignsByInstitutionId(Long id);

    DonationCampaign getDonationById(Long id);
    DonationCampaignContract getContractDonationById(Long id);

    DonationCampaignContract changeDonationCampaignContractStatus(Long id, Enums.CampaignStatus campaignStatus);

}
