package com.acmeflix.domain;

import lombok.Data;

@Data

public class Profile {
    private short profileId;
    private String username;

    /* public Profile(short profileId, String username) {
        this.profileId = profileId; this.username = username;
    } */
}
