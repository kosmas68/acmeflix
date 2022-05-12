package com.acmeflix.domain;

import lombok.Data;

@Data

public class ProgramSubtitle {
    private int programId;
    private short seasonId;
    private short episodeId;
    private Language language;
}
