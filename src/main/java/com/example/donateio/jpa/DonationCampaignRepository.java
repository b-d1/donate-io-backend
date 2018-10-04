package com.example.donateio.jpa;

import com.example.donateio.model.DonationCampaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface DonationCampaignRepository extends CrudRepository<DonationCampaign, Long> {

    Page<DonationCampaign> findByInstitutionId(Long institutionId, Pageable pageable);

    Optional<DonationCampaign> findByName(String name);
}
