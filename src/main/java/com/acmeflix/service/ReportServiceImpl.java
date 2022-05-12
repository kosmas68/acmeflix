package com.acmeflix.service;

public class ReportServiceImpl extends AbstractServiceImpl implements ReportService {
    @Override
    public void report1(int accountId) {
        dataRepositoryService.report1(accountId);
    }

    @Override
    public void report2(int accountId) {
        dataRepositoryService.report2(accountId);
    }

    @Override
    public void report3() {
        dataRepositoryService.report3();
    }
}
