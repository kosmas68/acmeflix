package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data

public class Account {
    private int id;
    private String name;
    private SubscriptionPlan subscriptionPlan;
    private List<Profile> profiles;
}
