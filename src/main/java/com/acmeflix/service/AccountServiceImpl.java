package com.acmeflix.service;

import com.acmeflix.domain.Account;
import com.acmeflix.domain.Profile;
import com.acmeflix.domain.SubscriptionPlan;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl extends AbstractServiceImpl implements AccountService {

    @Override
    public Account initiateAccount(String name, SubscriptionPlan subscriptionPlan) {
        Account account = new Account();
        account.setName(name);
        account.setSubscriptionPlan(subscriptionPlan);
        return account;
    }

    @Override
    public void addProfile(Account account, short profileId, String username) {
        Profile profile = new Profile();
        profile.setProfileId(profileId);
        profile.setUsername(username);

        List<Profile> profiles = account.getProfiles();
        if (profiles == null)
            profiles = new ArrayList<>();
        profiles.add(profile);
        account.setProfiles(profiles);
    }

    @Override
    public void checkout(Account account) {
        dataRepositoryService.save(account);
    }

    @Override
    public void getAccounts() {
        dataRepositoryService.getAccounts();
    }

    @Override
    public void getProfiles() {
        dataRepositoryService.getProfiles();
    }
}
