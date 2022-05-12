package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data

public class Episode {
    private short episodeId;
    private List<Language> speakingLanguages;
    private List<Language> subtitleLanguages;
}
