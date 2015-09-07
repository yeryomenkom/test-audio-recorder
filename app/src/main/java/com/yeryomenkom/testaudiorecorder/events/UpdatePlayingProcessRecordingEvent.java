package com.yeryomenkom.testaudiorecorder.events;

/**
 * Created by Misha on 9/4/2015.
 */
public class UpdatePlayingProcessRecordingEvent {
    private static UpdatePlayingProcessRecordingEvent event = new UpdatePlayingProcessRecordingEvent();

    public int progress;

    private UpdatePlayingProcessRecordingEvent() {
    }

    public static UpdatePlayingProcessRecordingEvent getEvent(int progress) {
        event.progress = progress;
        return event;
    }
}
