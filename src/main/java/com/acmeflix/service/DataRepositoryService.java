package com.acmeflix.service;

import com.acmeflix.domain.Account;
import com.acmeflix.domain.Movie;
import com.acmeflix.domain.Series;

public interface DataRepositoryService {
    boolean save(Account account);

    void getAccounts();

    void getProfiles();

    boolean save(Movie movie);

    void getPrograms();

    void getLanguages();

    boolean save(Series series);
}
