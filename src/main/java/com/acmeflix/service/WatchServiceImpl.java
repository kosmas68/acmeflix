package com.acmeflix.service;

import com.acmeflix.domain.Watch;

public class WatchServiceImpl extends AbstractServiceImpl implements WatchService {
    @Override
    public void watch(Watch watch) {
        dataRepositoryService.save(watch);
    }

    @Override
    public void getWatches() {
        dataRepositoryService.getWatches();
    }
}
