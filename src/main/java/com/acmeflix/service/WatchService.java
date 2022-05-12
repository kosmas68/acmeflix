package com.acmeflix.service;

import com.acmeflix.domain.Watch;

public interface WatchService {
    void watch(Watch watch);

    void getWatches();
}
