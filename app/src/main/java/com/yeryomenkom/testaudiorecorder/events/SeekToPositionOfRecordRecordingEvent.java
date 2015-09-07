package com.yeryomenkom.testaudiorecorder.events;

/**
 * Created by Misha on 9/4/2015.
 */
public class SeekToPositionOfRecordRecordingEvent {
    static private SeekToPositionOfRecordRecordingEvent event = new SeekToPositionOfRecordRecordingEvent();

    public int position;

    private SeekToPositionOfRecordRecordingEvent() {
    }

    public static SeekToPositionOfRecordRecordingEvent getEvent(int position) {
        event.position = position;
        return event;
    }
}
