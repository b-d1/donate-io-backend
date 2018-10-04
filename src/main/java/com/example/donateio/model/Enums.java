package com.example.donateio.model;

public class Enums {
    public enum CampaignStatus {
        Active,
        FundingGoalReachedNotExpired,
        FundingGoalReachedExpired,
        FundingGoalNotReachedExpired,
        Stopped,
        Succeeded
    };

    public enum CampaignType {
        NonContract,
        Contract
    };
}
