package com.example.donateio.jpa;

import com.example.donateio.model.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DonationRepository extends CrudRepository<Donation, Long>{

    Page<Donation> findByUserId(Long userId, Pageable pageable);

    Page<Donation> findByDonationCampaignId(Long donationCampaignId, Pageable pageable);

    Page<Donation> findByDonationCampaignContractId(Long donationCampaignContractId, Pageable pageable);

}
