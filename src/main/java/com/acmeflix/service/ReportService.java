package com.acmeflix.service;

public interface ReportService {
    void report1(int accountId); //List of the programs viewed by a specific account and its connected profiles

    void report2(int accountId); //Account's popular content categories

    void report3(); //List of the 5 most viewed programs, TV shows and movies
}
