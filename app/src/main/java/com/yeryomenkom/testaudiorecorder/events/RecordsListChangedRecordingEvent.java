package com.yeryomenkom.testaudiorecorder.events;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecordsListChangedRecordingEvent {
    private static RecordsListChangedRecordingEvent event = new RecordsListChangedRecordingEvent();

    public ArrayList<File> recordsList;

    private RecordsListChangedRecordingEvent() {
    }

    public static RecordsListChangedRecordingEvent getEvent(ArrayList<File> recordsList) {
        event.recordsList = recordsList;
        return event;
    }

}
