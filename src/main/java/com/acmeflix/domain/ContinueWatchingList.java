package com.acmeflix.domain;

import lombok.Data;

@Data

public class ContinueWatchingList {
    private int accountId;
    private short profileId;
    private int programId;
    private short seasonId;
    private short episodeId;
    private Language speakingLanguage;
    private Language subtitleLanguage;
    private short minuteOfStop;
}
