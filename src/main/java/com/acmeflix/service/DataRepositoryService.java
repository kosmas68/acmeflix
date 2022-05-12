package com.acmeflix.service;

import com.acmeflix.domain.Account;
import com.acmeflix.domain.Movie;
import com.acmeflix.domain.Series;
import com.acmeflix.domain.Watch;

public interface DataRepositoryService {
    boolean save(Account account);

    void getAccounts();

    void getProfiles();

    boolean save(Movie movie);

    void getPrograms();

    void getLanguages();

    boolean save(Series series);

    void save(Watch watch);

    void getWatches();

    void report1(int accountId);

    void report2(int accountId);

    void report3();
}
