package com.example.donateio.service;

import com.example.donateio.model.Institution;
import com.example.donateio.model.User;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService {


boolean validateUser(String email);

User userExists(String email);

Institution institutionExists(String email);

boolean validateInstitution(String email);

User registerUser(String name, String email, String password);

User updateUser(String name, String email, String password);

Institution registerInstitution(String name, String email, String password);

Object login(String email, String password);

String generateQRUrl(Object object) throws UnsupportedEncodingException;

}
