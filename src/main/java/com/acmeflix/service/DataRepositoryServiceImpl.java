package com.acmeflix.service;

import com.acmeflix.AcmeFlix;
import com.acmeflix.DataSource;
import com.acmeflix.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DataRepositoryServiceImpl implements DataRepositoryService {
    private static final Logger logger = LoggerFactory.getLogger(DataRepositoryService.class);

    @Override
    public boolean save(Account account) {
        logger.info("000");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.account.000"), Statement.RETURN_GENERATED_KEYS)) {

            logger.info("111000111");
            //preparedStatement.setInt(1, 0);
            logger.info("222");
            preparedStatement.setString(1, account.getName());
            logger.info("333");
            preparedStatement.setInt(2, account.getSubscriptionPlan().getPlan());

            logger.info("Amesws prin to insert gia {} kai {}", account.getName(), account.getSubscriptionPlan().getPlan());
            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of account {}", account);

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    account.setId(keys.getInt(1));
                    for (Profile profile : account.getProfiles()) {
                        save(profile, account.getId());
                    }
                }
            }

            return true;
        } catch (SQLException ex) {
            logger.info("Error while inserting account {}", account);
            return false;
        }
    }

    private void save(Profile profile, int accountId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.profile.000"), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, accountId);
            preparedStatement.setInt(2, profile.getProfileId());
            preparedStatement.setString(3, profile.getUsername());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of profile {}", profile);
        } catch (SQLException ex) {
            logger.info("Error while inserting profile {}", profile);
        }
    }

    @Override
    public void getAccounts() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.accounts.001"))) {

            logger.info("---------------------------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving accounts");
        }
    }

    @Override
    public void getProfiles() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.profiles.001"))) {

            logger.info("---------------------------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving profiles");
        }
    }

    @Override
    public boolean save(Movie movie) {
        logger.info("000");
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.program.000"), Statement.RETURN_GENERATED_KEYS)) {

            logger.info("111");
            logger.info("222");
            preparedStatement.setString(1, movie.getTitle());
            logger.info("333");
            preparedStatement.setInt(2, movie.getProgramType().getType());
            preparedStatement.setInt(3, movie.getProgramCategory().getCategory());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of movie {}", movie);

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    movie.setId(keys.getInt(1));
                    for (Language language : movie.getSpeakingLanguages()) {
                        saveSpeaking(language, movie.getId(), 0, 0);
                    }
                    for (Language language : movie.getSubtitleLanguages()) {
                        saveSubtitle(language, movie.getId(), 0, 0);
                    }
                }
            }
            return true;
        } catch (SQLException ex) {
            logger.info("Error while inserting movie {}", movie);
            return false;
        }

    }

    @Override
    public boolean save(Series series) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.program.000"), Statement.RETURN_GENERATED_KEYS)) {

            logger.info("111");
            logger.info("222");
            preparedStatement.setString(1, series.getTitle());
            logger.info("333");
            preparedStatement.setInt(2, series.getProgramType().getType());
            preparedStatement.setInt(3, series.getProgramCategory().getCategory());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of series {}", series);

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    series.setId(keys.getInt(1));
                    for (Season season : series.getSeasons()) {
                        for (Episode episode : season.getEpisodes()) {
                            for (Language language : episode.getSpeakingLanguages()) {
                                saveSpeaking(language, series.getId(), season.getSeasonId(), episode.getEpisodeId());
                            }
                            for (Language language : episode.getSubtitleLanguages()) {
                                saveSubtitle(language, series.getId(), season.getSeasonId(), episode.getEpisodeId());
                            }
                        }
                    }
                }
            }
            return true;
        } catch (SQLException ex) {
            logger.info("Error while inserting series {}", series);
            return false;
        }

    }

    private void saveSpeaking(Language language, int programId, int seasonId, int episodeId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.SpeakingLanguage.000"), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, programId);
            preparedStatement.setInt(2, seasonId);
            preparedStatement.setInt(3, episodeId);
            preparedStatement.setInt(4, language.getLanguage());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of speaking language {}", language);
        } catch (SQLException ex) {
            logger.info("Error while inserting speaking language {}", language);
        }
    }

    private void saveSubtitle(Language language, int programId, int seasonId, int episodeId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.SubtitleLanguage.000"), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, programId);
            preparedStatement.setInt(2, seasonId);
            preparedStatement.setInt(3, episodeId);
            preparedStatement.setInt(4, language.getLanguage());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of subtitle language {}", language);
        } catch (SQLException ex) {
            logger.info("Error while inserting subtitle language {}", language);
        }

    }

    @Override
    public void getPrograms() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.programs.001"))) {

            logger.info("---------------------------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3),
                        resultSet.getMetaData().getColumnName(4), resultSet.getString(4));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving programs");
        }
    }

    @Override
    public void getLanguages() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.speakingLanguage.001"))) {

            logger.info("---------------------------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3),
                        resultSet.getMetaData().getColumnName(4), resultSet.getString(4));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving speaking Languages");
        }

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.subtitleLanguage.001"))) {

            logger.info("---------------------------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3),
                        resultSet.getMetaData().getColumnName(4), resultSet.getString(4));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving subtitle Languages");
        }

    }

}

