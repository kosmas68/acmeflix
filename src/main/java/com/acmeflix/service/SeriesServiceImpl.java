package com.acmeflix.service;

import com.acmeflix.domain.*;

import java.util.ArrayList;
import java.util.List;

public class SeriesServiceImpl extends AbstractServiceImpl implements SeriesService {
    @Override
    public Series initiateSeries(String title, ProgramCategory category) {
        Series series = new Series();
        series.setTitle(title);
        series.setProgramType(ProgramType.SERIES);
        series.setProgramCategory(category);
        return series;
    }

    @Override
    public Season initiateSeason(short seasonId) {
        Season season = new Season();
        season.setSeasonId(seasonId);
        return season;
    }

    @Override
    public void addSeason(Series series, Season season) {
        List<Season> seasons = series.getSeasons();
        if (seasons == null)
            seasons = new ArrayList<>();
        seasons.add(season);
        series.setSeasons(seasons);
    }

    @Override
    public Episode initiateEpisode(short episodeId) {
        Episode episode = new Episode();
        episode.setEpisodeId(episodeId);
        return episode;
    }

    @Override
    public void addEpisode(Season season, Episode episode) {
        List<Episode> episodes = season.getEpisodes();
        if (episodes == null)
            episodes = new ArrayList<>();
        episodes.add(episode);
        season.setEpisodes(episodes);
    }

    @Override
    public void addSpeakingLanguage(Episode episode, Language language) {
        List<Language> languages = episode.getSpeakingLanguages();
        if (languages == null)
            languages = new ArrayList<>();
        languages.add(language);
        episode.setSpeakingLanguages(languages);
    }

    @Override
    public void addSubtitleLanguage(Episode episode, Language language) {
        List<Language> languages = episode.getSubtitleLanguages();
        if (languages == null)
            languages = new ArrayList<>();
        languages.add(language);
        episode.setSubtitleLanguages(languages);
    }

    @Override
    public void checkout(Series series) {
        dataRepositoryService.save(series);
    }
}
