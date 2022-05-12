package com.acmeflix.service;

import com.acmeflix.domain.Language;
import com.acmeflix.domain.Movie;
import com.acmeflix.domain.ProgramCategory;

public interface MovieService {

    Movie initiateMovie(String title, ProgramCategory category);

    void addSpeakingLanguage(Movie movie, Language language);

    void addSubtitleLanguage(Movie movie, Language language);

    void checkout(Movie movie);

    void getPrograms();

    void getLanguages();
}
