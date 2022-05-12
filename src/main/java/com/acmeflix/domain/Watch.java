package com.acmeflix.domain;

import lombok.Data;

import java.util.Date;

@Data

public class Watch {
    private int accountId;
    private short profileId;
    private int programId;
    private short seasonId;
    private short episodeId;
    private Date watchDate;
}
