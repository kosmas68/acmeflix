package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data
public class Movie extends Program {
    private List<Language> speakingLanguages;
    private List<Language> subtitleLanguages;
}
