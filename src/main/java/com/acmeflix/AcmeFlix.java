package com.acmeflix;

import com.acmeflix.domain.*;
import com.acmeflix.service.*;
import org.h2.message.DbException;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.lang.System.exit;

public class AcmeFlix {
    public static final Properties sqlCommands = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(AcmeFlix.class);
    private static Server server;

    static {
        try (InputStream inputStream = AcmeFlix.class.getClassLoader().getResourceAsStream(
                "sql.properties")) {
            if (inputStream == null) {
                logger.error("Sorry, unable to find sql.properties, exiting application.");
                // Abnormal exit
                exit(-1);
            }

            //load a properties file from class path, inside static method
            sqlCommands.load(inputStream);
        } catch (IOException ex) {
            logger.error("Sorry, unable to parse sql.properties, exiting application.", ex);
            // Abnormal exit
            exit(-1);
        }
    }

    private final AccountService accountService = new AccountServiceImpl();
    private final MovieService movieService = new MovieServiceImpl();
    private final SeriesService seriesService = new SeriesServiceImpl();

    public static void main(String[] args) {
        // Start database server
        startH2Server();

        AcmeFlix acmeFlix = new AcmeFlix();

        acmeFlix.loadDatabaseDriver();

        acmeFlix.createTables();
        acmeFlix.insertData();
        acmeFlix.displayData();
        //acmeFlix.generateReports();

        stopH2Server();
    }

    private void createTables() {
        try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement()) {
            //@formatter:off
            runCommands(statement,
                    sqlCommands.getProperty("create.accountTable"),
                    sqlCommands.getProperty("create.profileTable"),
                    sqlCommands.getProperty("create.programTable"),
                    sqlCommands.getProperty("create.speakingLanguageTable"),
                    sqlCommands.getProperty("create.subtitleLanguageTable")
            );
            //@formatter:on
        } catch (SQLException ex) {
            logger.error("Error while creating table(s).", ex);
            exit(-1);
        }
    }

    private void insertData() {
        try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement()) {

            // Insert accounts
            Account account = accountService.initiateAccount("Charitonidis", SubscriptionPlan.BASIC);
            accountService.addProfile(account, (short) 1, "Kosmas");
            accountService.addProfile(account, (short) 2, "Litsa");
            accountService.addProfile(account, (short) 3, "Olympia");
            accountService.addProfile(account, (short) 4, "Kate");
            accountService.checkout(account);

            account = accountService.initiateAccount("Papadopoulos", SubscriptionPlan.STANDARD);
            accountService.addProfile(account, (short) 1, "Stelios");
            accountService.addProfile(account, (short) 2, "Xrysa");
            accountService.addProfile(account, (short) 3, "Maria");
            accountService.addProfile(account, (short) 4, "Nastazia");
            accountService.checkout(account);

            // Insert Movies
            Movie movie = movieService.initiateMovie("The Shawshank Redemption", ProgramCategory.DRAMA);
            movieService.addSpeakingLanguage(movie, Language.ENGLISH);
            movieService.addSpeakingLanguage(movie, Language.FRENCH);
            movieService.addSpeakingLanguage(movie, Language.GERMAN);
            movieService.addSubtitleLanguage(movie, Language.ARABIC);
            movieService.addSubtitleLanguage(movie, Language.ITALIAN);
            movieService.addSubtitleLanguage(movie, Language.JAPANESE);
            movieService.checkout(movie);

            movie = movieService.initiateMovie("The Godfather", ProgramCategory.THRILLER);
            movieService.addSpeakingLanguage(movie, Language.SPANISH);
            movieService.addSpeakingLanguage(movie, Language.KOREAN);
            movieService.addSubtitleLanguage(movie, Language.HINDI);
            movieService.addSubtitleLanguage(movie, Language.PORTUGUESE);
            movieService.addSubtitleLanguage(movie, Language.PERSIAN);
            movieService.checkout(movie);

            // Insert Series
            // Series 1
            Series series = seriesService.initiateSeries("Planet Earth", ProgramCategory.DOCUMENTARY);

            Season season = seriesService.initiateSeason((short) 1);
            seriesService.addSeason(series, season);
            Episode episode = seriesService.initiateEpisode((short) 1);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.ENGLISH);
            seriesService.addSpeakingLanguage(episode, Language.VIETNAMESE);
            seriesService.addSubtitleLanguage(episode, Language.ITALIAN);
            seriesService.addSubtitleLanguage(episode, Language.JAPANESE);
            episode = seriesService.initiateEpisode((short) 2);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.BENGALI);
            seriesService.addSpeakingLanguage(episode, Language.GUJARATI);
            seriesService.addSubtitleLanguage(episode, Language.MARATHI);
            seriesService.addSubtitleLanguage(episode, Language.MINNAN);

            season = seriesService.initiateSeason((short) 2);
            seriesService.addSeason(series, season);
            episode = seriesService.initiateEpisode((short) 1);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.HAKKA);
            seriesService.addSubtitleLanguage(episode, Language.PUNJABI);
            episode = seriesService.initiateEpisode((short) 2);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.TELUGU);
            seriesService.addSubtitleLanguage(episode, Language.HAUSA);

            seriesService.checkout(series);

            // Series 2
            series = seriesService.initiateSeries("Breaking Bad", ProgramCategory.THRILLER);

            season = seriesService.initiateSeason((short) 1);
            seriesService.addSeason(series, season);
            episode = seriesService.initiateEpisode((short) 1);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.URDU);
            seriesService.addSpeakingLanguage(episode, Language.TURKISH);
            seriesService.addSubtitleLanguage(episode, Language.TAMIL);
            seriesService.addSubtitleLanguage(episode, Language.RUSSIAN);
            episode = seriesService.initiateEpisode((short) 2);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.JAVANESE);
            seriesService.addSpeakingLanguage(episode, Language.ENGLISH);
            seriesService.addSubtitleLanguage(episode, Language.CHINESE);
            seriesService.addSubtitleLanguage(episode, Language.BHOJPURI);

            season = seriesService.initiateSeason((short) 2);
            seriesService.addSeason(series, season);
            episode = seriesService.initiateEpisode((short) 1);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.FRENCH);
            seriesService.addSubtitleLanguage(episode, Language.GERMAN);
            episode = seriesService.initiateEpisode((short) 2);
            seriesService.addEpisode(season, episode);
            seriesService.addSpeakingLanguage(episode, Language.ARABIC);
            seriesService.addSubtitleLanguage(episode, Language.ITALIAN);

            seriesService.checkout(series);

            logger.info("Eftase edw");
        } catch (SQLException ex) {
            logger.error("Error while inserting data.", ex);
        }
    }

    private void displayData() {
        accountService.getAccounts();
        accountService.getProfiles();
        movieService.getPrograms();
        movieService.getLanguages();
    }

    private void runCommands(Statement statement, String... commands) throws SQLException {
        for (String command : commands) {
            logger.info("Command was successful with {} row(s) affected.", statement.executeUpdate(command));
        }
    }

    private static void startH2Server() {
        try {
            server = Server.createTcpServer("-tcpAllowOthers", "-tcpDaemon");
            server.start();
        } catch (SQLException e) {
            //
            logger.error("Error while starting H2 server.", DbException.convert(e));
            exit(-1);
        }
        logger.info("H2 server has started with status '{}'.", server.getStatus());
    }

    private static void stopH2Server() {
        if (server == null) {
            return;
        }
        if (server.isRunning(true)) {
            server.stop();
            server.shutdown();
            logger.info("H2 server has been shutdown.");
        }
        server = null;
    }

    private void loadDatabaseDriver() {
        org.h2.Driver.load();

        // Traditional way of loading database driver
        // H2 driver from http://www.h2database.com
		/*
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		*/
        logger.info("H2 JDBC driver server has been successfully loaded.");
    }

}