package com.example.donateio.service;

import com.example.donateio.model.Address;
import com.example.donateio.model.Institution;
import com.example.donateio.model.User;

import java.util.List;

public interface DetailsService {

    List<Address> getUserAddresses(Long id);
    List<Address> getInstitutionAddresses(Long id);

    List<Institution> getInstitutions();

    Institution getInstitutionDetails(String email);

    User getUserDetails(String email);

}
