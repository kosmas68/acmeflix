package com.acmeflix.service;

import com.acmeflix.domain.Language;
import com.acmeflix.domain.Movie;
import com.acmeflix.domain.ProgramCategory;
import com.acmeflix.domain.ProgramType;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl extends AbstractServiceImpl implements MovieService {
    @Override
    public Movie initiateMovie(String title, ProgramCategory category) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setProgramType(ProgramType.MOVIE);
        movie.setProgramCategory(category);
        return movie;
    }

    @Override
    public void addSpeakingLanguage(Movie movie, Language language) {
        List<Language> languages = movie.getSpeakingLanguages();
        if (languages == null)
            languages = new ArrayList<>();
        languages.add(language);
        movie.setSpeakingLanguages(languages);
    }

    @Override
    public void addSubtitleLanguage(Movie movie, Language language) {
        List<Language> languages = movie.getSubtitleLanguages();
        if (languages == null)
            languages = new ArrayList<>();
        languages.add(language);
        movie.setSubtitleLanguages(languages);
    }

    @Override
    public void checkout(Movie movie) {
        dataRepositoryService.save(movie);
    }

    @Override
    public void getPrograms() {
        dataRepositoryService.getPrograms();
    }

    @Override
    public void getLanguages() {
        dataRepositoryService.getLanguages();
    }

}
