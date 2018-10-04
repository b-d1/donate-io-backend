package com.example.donateio.service.impl;

import com.example.donateio.model.Address;
import com.example.donateio.model.Institution;
import com.example.donateio.model.User;
import com.example.donateio.model.exceptions.UserNotFoundException;
import com.example.donateio.jpa.AddressRepository;
import com.example.donateio.jpa.InstitutionRepository;
import com.example.donateio.jpa.UserRepository;
import com.example.donateio.service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailsServiceImpl implements DetailsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public InstitutionRepository institutionRepository;


    @Override
    public List<Address> getUserAddresses(Long id) {
        return null;
    }

    @Override
    public List<Address> getInstitutionAddresses(Long id) {
        return null;
    }

    @Override
    public List<Institution> getInstitutions() {
        return null;
    }

    @Override
    public Institution getInstitutionDetails(String email) {
        Institution i = null;
        for(Institution inst : institutionRepository.findAll()) {
            if(inst.email.compareTo(email) == 0) {
                i = inst;
                break;
            }
        }
        if(i != null) return i;
        else throw new UserNotFoundException("Institution not found!");
    }

    @Override
    public User getUserDetails(String email) {
        User u = userRepository.findByEmail(email).get();
        if(u != null) {
            return u;
        }
        else {
            throw new UserNotFoundException("User not found!");
        }
    }

}
