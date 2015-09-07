package com.yeryomenkom.testaudiorecorder.events;

import java.io.File;

/**
 * Created by Misha on 9/4/2015.
 */
public class NewRecordStartPlayingEvent {
    private static NewRecordStartPlayingEvent event = new NewRecordStartPlayingEvent();

    public File record;
    public int recordDuration;

    private NewRecordStartPlayingEvent() {
    }

    public static NewRecordStartPlayingEvent getEvent(File record, int recordDuration) {
        event.record = record;
        event.recordDuration = recordDuration;
        return event;
    }
}
