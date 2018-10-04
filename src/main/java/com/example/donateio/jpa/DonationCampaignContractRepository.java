package com.example.donateio.jpa;


import com.example.donateio.model.DonationCampaignContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DonationCampaignContractRepository extends CrudRepository<DonationCampaignContract, Long> {

    Page<DonationCampaignContract> findByInstitutionId(Long institutionId, Pageable pageable);

    Optional<DonationCampaignContract> findByName(String name);
}