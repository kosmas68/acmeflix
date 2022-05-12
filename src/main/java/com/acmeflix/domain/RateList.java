package com.acmeflix.domain;

import lombok.Data;

@Data

public class RateList {
    private int accountId;
    private short profileId;
    private int programId;
    private short seasonId;
    private Rate rate;
}
