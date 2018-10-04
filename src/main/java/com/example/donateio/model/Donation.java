package com.example.donateio.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @ManyToOne()
    @JoinColumn(name = "donationCampaign_id", nullable = true)
    public DonationCampaign donationCampaign;

    @ManyToOne()
    @JoinColumn(name = "donationCampaignContract_id", nullable = true)
    public DonationCampaignContract donationCampaignContract;


    public String addressFrom;
    public String addressTo;
    public String description;
    public String amount;
    public String fee;
    public String transactionId;
    public Date createdOn;

}
