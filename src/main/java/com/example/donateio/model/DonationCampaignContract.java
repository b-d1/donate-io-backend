package com.example.donateio.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "contractDonationCampaigns")
public class DonationCampaignContract {

    @Id
    @GeneratedValue
    public Long id;

    @Column(unique=true, nullable = false)
    public String name;

    public String email;
    public String website;

    @Lob
    public String description;

    public String image;
    public String address;
    public String totalReceived; //hex
    public String fundingGoal; //hex
    public String timeGoal;
    public String txid;
    public Enums.CampaignStatus campaignStatus;

    @ManyToOne
    @JoinColumn(name="institution_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Institution institution;

}
