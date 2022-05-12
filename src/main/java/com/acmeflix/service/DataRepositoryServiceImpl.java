package com.acmeflix.service;

import com.acmeflix.AcmeFlix;
import com.acmeflix.DataSource;
import com.acmeflix.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.DateFormat;

public class DataRepositoryServiceImpl implements DataRepositoryService {
    private static final Logger logger = LoggerFactory.getLogger(DataRepositoryService.class);

    @Override
    public boolean save(Account account) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.account.000"), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, account.getName());
            preparedStatement.setInt(2, account.getSubscriptionPlan().getPlan());

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

            logger.info("-------------- Accounts ---------------------------------------------");
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

            logger.info("-------------- Profiles ---------------------------------------------");
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
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.program.000"), Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, movie.getTitle());
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

            preparedStatement.setString(1, series.getTitle());
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

    @Override
    public void save(Watch watch) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AcmeFlix.sqlCommands.getProperty("insert.watch.000"), Statement.RETURN_GENERATED_KEYS)) {


            preparedStatement.setInt(1, watch.getAccountId());
            preparedStatement.setShort(2, watch.getProfileId());
            preparedStatement.setInt(3, watch.getProgramId());
            preparedStatement.setShort(4, watch.getSeasonId());
            preparedStatement.setShort(5, watch.getEpisodeId());
            preparedStatement.setDate(6, (Date) watch.getWatchDate());

            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Successful insert of watch {}", watch);
        } catch (SQLException ex) {
            logger.info("Error while inserting watch {}", watch);
        }
    }

    @Override
    public void getWatches() {
        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     AcmeFlix.sqlCommands.getProperty("select.watch.001"))) {

            logger.info("-------------- Watch ------------------------------------------------");
            //@formatter:off
            int rowCount = 1;
            while (resultSet.next()) {
                logger.info("{}. {}:{}, {}:{}, {}:{}, {}:{}, {}:{}, {}:{}", rowCount++,
                        resultSet.getMetaData().getColumnName(1), resultSet.getString(1),
                        resultSet.getMetaData().getColumnName(2), resultSet.getString(2),
                        resultSet.getMetaData().getColumnName(3), resultSet.getString(3),
                        resultSet.getMetaData().getColumnName(4), resultSet.getString(4),
                        resultSet.getMetaData().getColumnName(5), resultSet.getString(5),
                        resultSet.getMetaData().getColumnName(6), resultSet.getString(6));
            }
            //@formatter:on
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.info("Error while retrieving watches");
        }
    }

    @Override
    public void report1(int accountId) {
        // List of the programs viewed by a specific account and its connected profiles
        String mySelect =
                "SELECT profile.username, program.title, watch.seasonId, watch.episodeId, watch.watchDate " +
                        "FROM watch, profile, program " +
                        "WHERE watch.accountId = " + accountId +
                        "  AND profile.accountId = watch.accountId " +
                        "  AND profile.profileId = watch.profileId " +
                        "  AND program.id = watch.programId;";

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(mySelect)) {
            logger.info("----- report 1 for account {} ---------------------------------------", accountId);
            while (resultSet.next()) {
                logger.info("{} {} {} {} {}", resultSet.getString(1), resultSet.getString(2),
                        resultSet.getShort(3), resultSet.getShort(4),
                        DateFormat.getDateInstance().format(resultSet.getDate(5)));
            }
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.error("Error while reading data for report 1", ex);
        }

    }

    @Override
    public void report2(int accountId) {
        //Account's popular content categories
        // Σχόλιο: Όπως είναι τώρα φέρνει όλες τις κατηγορίες που έχει δει ο accountId. Για να φέρνει μόνο τις πιο
        //         δημοφιλείς θα πρέπει είτε να μπει ένας μετρητής (όπως ακριβώς στο report 3 παρακάτω), είτε να μπει
        //         στο query ένα HAVING COUNT > κάποιο όριο. Επειδή δεν ξέρω ποιο απ' τα δύο προτιμάμε, το αφήνω έτσι.
        //         Άλλωστε φέρνει πρώτες τις πιο δημοφιλείς.
        String mySelect =
                "SELECT program.category, COUNT(DISTINCT program.Id) " +
                        "FROM watch, program " +
                        "WHERE watch.accountId = " + accountId +
                        "  AND program.id = watch.programId " +
                        "GROUP BY program.category " +
                        "ORDER BY 2 DESC;";

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(mySelect)) {
            logger.info("----- report 2 for account {} ---------------------------------------", accountId);
            while (resultSet.next()) {
                logger.info("{} {}", resultSet.getShort(1), resultSet.getInt(2));
            }
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.error("Error while reading data for report 2", ex);
        }

    }

    @Override
    public void report3() {
        //List of the 5 most viewed programs, TV shows and movies
        String mySelect =
                "SELECT program.id, program.title, program.type, COUNT(DISTINCT (watch.accountId, watch.profileId)) " +
                        "FROM watch, program " +
                        "WHERE program.id = watch.programId " +
                        "GROUP BY program.id, program.title, program.type " +
                        "ORDER BY 4 DESC;";

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(mySelect)) {
            logger.info("----- report 3 ------------------------------------------------------");
            short count = 0;
            while (resultSet.next() && count < 5) {
                logger.info("{} {} {} {}", resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getShort(3), resultSet.getInt(4));
                count++;
            }
            logger.info("---------------------------------------------------------------------");

        } catch (SQLException ex) {
            logger.error("Error while reading data for report 1", ex);
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

            logger.info("-------------- Programs ---------------------------------------------");
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

            logger.info("-------------- Speaking Languages -----------------------------------");
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

            logger.info("-------------- Subtitle Languages -----------------------------------");
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

