package com.example.donateio.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "donationCampaigns")
public class DonationCampaign {

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

//    @OneToMany(mappedBy = "donationCampaign")
//    public List<Donation> donations;

    @ManyToOne
    @JoinColumn(name="institution_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Institution institution;
}
