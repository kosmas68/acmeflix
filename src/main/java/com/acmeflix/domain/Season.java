package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data

public class Season {
    private short seasonId;
    private List<Episode> episodes;
}
