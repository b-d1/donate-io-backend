package com.example.donateio.rest;

import com.example.donateio.jpa.*;
import com.example.donateio.model.*;
import com.example.donateio.model.exceptions.DonationCampaignNotFoundException;
import com.example.donateio.model.exceptions.InvalidQRCodeException;
import com.example.donateio.model.exceptions.UserNotFoundException;
import com.example.donateio.service.*;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    AuthenticationService authService;
    DetailsService detailsService;
    DonationCampaignService donationCampaignService;
    ImageService imageService;
    DonationService donationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    InstitutionRepository institutionRepository;


    @Autowired
    DonationCampaignRepository donationCampaignRepository;

    @Autowired
    DonationCampaignContractRepository donationCampaignContractRepository;


    @Autowired
    public UserController(AuthenticationService authService, DetailsService detailsService, DonationCampaignService donationCampaignService, ImageService imageService, DonationService donationService) {
        this.authService = authService;
        this.detailsService = detailsService;
        this.donationCampaignService = donationCampaignService;
        this.imageService = imageService;
        this.donationService = donationService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "registerUser", method = RequestMethod.POST)
    public User registerUser(@RequestBody User user) {

        return authService.registerUser(user.name, user.email, user.password);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public User updateUser(@RequestBody User user) {

        return authService.updateUser(user.name, user.email, user.password);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getQRCode", method = RequestMethod.GET)
    public UrlResponse getQRCode(@RequestParam String email, String type) throws UnsupportedEncodingException {
        if(type.compareTo("user") == 0) {
            User u = detailsService.getUserDetails(email);
            String qrCodeUrl = authService.generateQRUrl(u);
            UrlResponse url = new UrlResponse();
            url.url = qrCodeUrl;
            return url;
        } else if(type.compareTo("institution") == 0) {
            Institution i = detailsService.getInstitutionDetails(email);
            String qrCodeUrl = authService.generateQRUrl(i);
            UrlResponse url = new UrlResponse();
            url.url = qrCodeUrl;
            return url;
        }

        return new UrlResponse();

    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "verifyQRCode", method = RequestMethod.GET)
    public Object verifyQRCode(@RequestParam String qrcode, @RequestParam String email, String type) throws UnsupportedEncodingException {

       String secret = null;
        if(type.compareTo("user") == 0) {
            User u = detailsService.getUserDetails(email);
            secret = u.secret;
            Totp totp = new Totp(secret);
            if(!totp.verify(qrcode)) {
                throw new InvalidQRCodeException("Invalid QR Code!");
            }
            return u;
        } else if(type.compareTo("institution") == 0) {
            Institution u = detailsService.getInstitutionDetails(email);
            secret = u.secret;
            Totp totp = new Totp(secret);
            if(!totp.verify(qrcode)) {
                throw new InvalidQRCodeException("Invalid QR Code!");
            }
            return u;
        }
        return new Object();

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "registerInstitution", method = RequestMethod.POST)
    public Institution registerInstitution(@RequestBody Institution i) {
        return authService.registerInstitution(i.name, i.email, i.password);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Object login(@RequestBody User user) {
        Object login = authService.login(user.email, user.password);
        LoginResponse logResponse = new LoginResponse();
        logResponse.login = login;
        if(login.getClass() == User.class) {
            logResponse.type = "user";
        } else if(login.getClass() == Institution.class) {
            logResponse.type = "institution";
        }
        return logResponse;
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }


//    @RequestMapping(value = "register", method = RequestMethod.GET)
//    public User addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
//        User u = new User();
//        u.name = name;
//        u.email = email;
//        u.password = password;
//        System.out.println("SAVING USER");
//        User saved = userRepository.save(u);
//        System.out.println("USER SAVED");
//        return saved;
//    }


    @RequestMapping(value = "addAddress", method = RequestMethod.GET)
    public Address addAddress(@RequestParam String address, @RequestParam Long userId) {
        Address a = new Address();
        a.address = address;
        a.userId = userId;
        System.out.println("SAVING Address");
        Address saved = addressRepository.save(a);
        System.out.println("USER SAVED");
        return saved;
    }

    @RequestMapping(value = "getUsers", method = RequestMethod.GET)
    public Iterable<User> listUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "getUserByEmail", method = RequestMethod.GET)
    public User getUserByEmail(@RequestParam String email) {
        return detailsService.getUserDetails(email);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "createCampaign", method = RequestMethod.POST)
    public DonationCampaign createCampaign(@RequestBody DonationCampaignBody donationCampaign) throws Exception {

        return donationCampaignService.createDonationCampaign(donationCampaign);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "createCampaignContract", method = RequestMethod.POST)
    public DonationCampaignContract createCampaignContract(@RequestBody DonationCampaignBody donationCampaign) throws Exception {

        return donationCampaignService.createDonationCampaignContract(donationCampaign);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getDonationCampaigns", method = RequestMethod.GET)
    public Iterable<DonationCampaign> getDonationCampaigns() {

        return donationCampaignService.getDonationCampaigns();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getDonationCampaignsContract", method = RequestMethod.GET)
    public Iterable<DonationCampaignContract> getDonationCampaignsContract() {

        return donationCampaignService.getDonationCampaignsContract();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getInstitutionContractDonations", method = RequestMethod.GET)
    public CampaignsByInstitution getInstitutionContractDonations(@RequestParam String email) {

        Institution institution = institutionRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Institution with email: " + email + " was not found!"));

        Page<DonationCampaignContract> donationCampaigns = donationCampaignService.getContractDonationCampaignsByInstitutionId(institution.id);

        CampaignsByInstitution campaignsByInstitution = new CampaignsByInstitution();
        campaignsByInstitution.donationCampaigns = new ArrayList<DonationCampaignWithDonations>();
        for(DonationCampaignContract campaign : donationCampaigns.getContent()) {

            DonationCampaignWithDonations donationCampaign = new DonationCampaignWithDonations();
            donationCampaign.description = campaign.description;
            donationCampaign.email = campaign.email;
            donationCampaign.name = campaign.name;
            donationCampaign.website = campaign.website;
            donationCampaign.donations = new ArrayList<DonationBody>();
            donationCampaign.totalReceived = campaign.totalReceived;
            donationCampaign.fundingGoal = campaign.fundingGoal;
            donationCampaign.timeGoal = campaign.timeGoal;
            donationCampaign.txid = campaign.txid;
            donationCampaign.campaignType = Enums.CampaignType.Contract;

            Page<Donation> donations = donationService.getDonationsByDonationCampaignContract(campaign.id);

            addDonations(donations, donationCampaign);

            campaignsByInstitution.donationCampaigns.add(donationCampaign);

        }

        return campaignsByInstitution;
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getInstitutionDonations", method = RequestMethod.GET)
    public CampaignsByInstitution getInstitutionDonations(@RequestParam String email) {

        Institution institution = institutionRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Institution with email: " + email + " was not found!"));

        Page<DonationCampaign> donationCampaigns = donationCampaignService.getDonationCampaignsByInstitutionId(institution.id);

        CampaignsByInstitution campaignsByInstitution = new CampaignsByInstitution();
        campaignsByInstitution.donationCampaigns = new ArrayList<DonationCampaignWithDonations>();
        for(DonationCampaign campaign : donationCampaigns.getContent()) {

            DonationCampaignWithDonations donationCampaign = new DonationCampaignWithDonations();
            donationCampaign.description = campaign.description;
            donationCampaign.email = campaign.email;
            donationCampaign.name = campaign.name;
            donationCampaign.website = campaign.website;
            donationCampaign.donations = new ArrayList<DonationBody>();
            donationCampaign.totalReceived = campaign.totalReceived;
            donationCampaign.campaignType = Enums.CampaignType.NonContract;

            Page<Donation> donations = donationService.getDonationsByDonationCampaign(campaign.id);

            addDonations(donations, donationCampaign);

            campaignsByInstitution.donationCampaigns.add(donationCampaign);

        }

        return campaignsByInstitution;
    }


    private void addDonations(Page<Donation> donations, DonationCampaignWithDonations donationCampaign) {
        for(Donation donation : donations.getContent()) {
            DonationBody donationBody = new DonationBody();

            donationBody.addressFrom = donation.addressFrom;
            donationBody.userEmail = donation.user.email;
            donationBody.donationCampaignName = donationCampaign.name;
            donationBody.addressTo = donation.addressTo;
            donationBody.amount = donation.amount;
            donationBody.fee = donation.fee;
            donationBody.description = donation.description;
            donationBody.transactionId = donation.transactionId;
            String createdOn = "";
            if(donation.createdOn != null) {
                createdOn = donation.createdOn.toString();
            }
            donationBody.createdOn = createdOn;
            donationCampaign.donations.add(donationBody);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getUserDonations", method = RequestMethod.GET)
    public List<DonationBody> getUserDonations(@RequestParam String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " was not found!"));

        Page<Donation> donations = donationService.getDonationsByUser(user.id);

        List<DonationBody> userDonations = new ArrayList<DonationBody>();

        for(Donation donation : donations.getContent()) {
            DonationBody donationBody = new DonationBody();

            donationBody.addressFrom = donation.addressFrom;
            donationBody.userEmail = donation.user.email;
            if(donation.donationCampaign != null) {
                donationBody.donationCampaignName = donation.donationCampaign.name;
            } else {
                donationBody.donationCampaignName = donation.donationCampaignContract.name;
            }
            donationBody.addressTo = donation.addressTo;
            donationBody.amount = donation.amount;
            donationBody.fee = donation.fee;
            donationBody.description = donation.description;
            donationBody.transactionId = donation.transactionId;
            String createdOn = "";
            if(donation.createdOn != null) {
                createdOn = donation.createdOn.toString();
            }
            donationBody.createdOn = createdOn;

            userDonations.add(donationBody);
        }

        return userDonations;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getDonationCampaignById", method = RequestMethod.GET)
    public DonationCampaign getDonationCampaignById(@RequestParam Long id) {
        return donationCampaignService.getDonationById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getDonationCampaignContractById", method = RequestMethod.GET)
    public DonationCampaignContract getDonationCampaignContractById(@RequestParam Long id) {
        return donationCampaignService.getContractDonationById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "getDonationCampaignImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDonationCampaignImage(@RequestParam String imageName) {

        System.out.println("GETTING IMAGE " + imageName);

        String imagePath = "donationCampaigns/" + imageName;

        byte[] image = getImageHelper(imagePath);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "createDonation", method = RequestMethod.POST)
    public Donation createDonation(@RequestBody DonationBody donationBody) throws DonationCampaignNotFoundException {
        Donation donation = null;
        try {
            donation = donationService.createDonation(donationBody);
        } catch(DonationCampaignNotFoundException e) {
            donation = donationService.createContractDonation(donationBody);
        } finally {
            return donation;
        }
    }


    public byte[] getImageHelper(String imageName) {
        byte[] image = new byte[0];
        try {
            image = imageService.getImage(imageName);
        } catch (IOException e) {
            System.out.println("ERROR WHILE OBTAINING IMAGE");
            e.printStackTrace();
        }
        return image;
    }

}

