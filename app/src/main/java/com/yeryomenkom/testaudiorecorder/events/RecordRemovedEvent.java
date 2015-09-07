package com.yeryomenkom.testaudiorecorder.events;

import java.io.File;

/**
 * Created by Misha on 9/5/2015.
 */
public class RecordRemovedEvent {
    private static RecordRemovedEvent event = new RecordRemovedEvent();

    public File record;

    private RecordRemovedEvent() {
    }

    public static RecordRemovedEvent getEvent(File record) {
        event.record = record;
        return event;
    }
}
