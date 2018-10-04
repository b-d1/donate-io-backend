package com.example.donateio.model;

import java.util.List;

public class DonationCampaignWithDonations {

    public String name;

    public String email;
    public String website;
    public String description;
    public String totalReceived;
    public String fundingGoal;
    public String timeGoal;
    public String txid;
    public Enum campaignType;

    public List<DonationBody> donations;

}
