package com.example.donateio.service.impl;

import com.example.donateio.model.Institution;
import com.example.donateio.model.User;
import com.example.donateio.model.exceptions.EntityAlreadyExistsException;
import com.example.donateio.model.exceptions.UserNotFoundException;
import com.example.donateio.model.exceptions.WrongPasswordException;
import com.example.donateio.jpa.AddressRepository;
import com.example.donateio.jpa.InstitutionRepository;
import com.example.donateio.jpa.UserRepository;
import com.example.donateio.service.AuthenticationService;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public InstitutionRepository institutionRepository;

    public BCryptPasswordEncoder encoder;

    public static String QR_PREFIX =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public AuthenticationServiceImpl() {
        this.encoder = new BCryptPasswordEncoder();
    }


    @Override
    public boolean validateUser(String email) {
        Iterable<User> users = userRepository.findAll();
        for(User u : users) {
            if(u.email.compareTo(email) == 0) return false;
        }

        return true;
    }

    @Override
    public User userExists(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            return userRepository.findByEmail(email).get();
        } else {
            return new User();
        }
    }

    @Override
    public Institution institutionExists(String email) {
        if(institutionRepository.findByEmail(email).isPresent()) {
            return institutionRepository.findByEmail(email).get();
        } else {
            return new Institution();
        }
    }

    @Override
    public boolean validateInstitution(String email) {
        Iterable<Institution> users = institutionRepository.findAll();
        for(Institution i : users) {
            if(i.email.compareTo(email) == 0) return false;
        }

        return true;
    }

    @Override
    public User registerUser(String name, String email, String password) {

        if (validateUser(email)) {
            User u = new User();
            u.name = name;
            u.email = email;
            u.secret = Base32.random();
            u.password = encoder.encode(password); // Hash it first
            u.hash = UUID.randomUUID().toString().replace("-", "");
            return userRepository.save(u);
        } else {
            throw new EntityAlreadyExistsException("User exists");
        }

    }

    @Override
    public User updateUser(String name, String email, String password) {

        User user = userExists(email);
        if(user != null) {
            user.name = name;
            if(!password.isEmpty()) {
                user.password = encoder.encode(password);
            }
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found");
        }

    }

    @Override
    public Institution registerInstitution(String name, String email, String password) {
        if(validateInstitution(email)) {
            Institution i = new Institution();
            i.name = name;
            i.email = email;
            i.secret = Base32.random();
            i.password = encoder.encode(password); // Hash it first
            i.hash = UUID.randomUUID().toString().replace("-", "");
            return institutionRepository.save(i);
        }
        else {
            throw new EntityAlreadyExistsException("Institution exists");
        }

    }

    @Override
    public Object login(String email, String password) {
        User u = this.userExists(email);

        if(u == null || u.id == null) {

            Institution i = this.institutionExists(email);

            if(i == null || i.id == null) {
                throw new UserNotFoundException("User not found");
            } else if(!encoder.matches(password, i.password)) {
                throw new WrongPasswordException("Wrong password");
            }
            return i;

        } else if(!encoder.matches(password, u.password)) {
            throw new WrongPasswordException("Wrong password");
        }

        return u;

    }

    @Override
    public String generateQRUrl(Object user) throws UnsupportedEncodingException {

        if(user.getClass() == User.class) {
            User userTmp = (User)user;

            return QR_PREFIX + URLEncoder.encode(String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                    "DonateIO", userTmp.email, userTmp.secret, "DonateIO"),
                    "UTF-8");
        } else {
            Institution institutionTmp = (Institution)user;

            return QR_PREFIX + URLEncoder.encode(String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                    "DonateIO", institutionTmp.email, institutionTmp.secret, "DonateIO"),
                    "UTF-8");
        }

    }
}
