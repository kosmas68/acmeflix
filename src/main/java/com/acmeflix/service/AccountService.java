package com.acmeflix.service;

import com.acmeflix.domain.Account;
import com.acmeflix.domain.SubscriptionPlan;

public interface AccountService {

    Account initiateAccount(String name, SubscriptionPlan subscriptionPlan);

    void addProfile(Account account, short profileId, String username);

    void checkout(Account account);

    void getAccounts();

    void getProfiles();
}
