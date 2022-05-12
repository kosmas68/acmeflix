package com.acmeflix.service;

import com.acmeflix.domain.*;

public interface SeriesService {

    Series initiateSeries(String title, ProgramCategory category);

    Season initiateSeason(short seasonId);

    void addSeason(Series series, Season season);

    Episode initiateEpisode(short episodeId);

    void addEpisode(Season season, Episode episode);

    void addSpeakingLanguage(Episode episode, Language language);

    void addSubtitleLanguage(Episode episode, Language language);

    void checkout(Series series);

}
