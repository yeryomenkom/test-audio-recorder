package com.yeryomenkom.testaudiorecorder.events;

import java.io.File;

/**
 * Created by Misha on 9/4/2015.
 */
public class PlayNewRecordEvent {
    private static PlayNewRecordEvent event = new PlayNewRecordEvent();

    public File record;

    private PlayNewRecordEvent() {
    }

    public static PlayNewRecordEvent getEvent(File record) {
        event.record = record;
        return event;
    }
}
