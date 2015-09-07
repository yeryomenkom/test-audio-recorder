package com.yeryomenkom.testaudiorecorder.events;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Misha on 9/4/2015.
 */
public class PlayOrPauseCurrentRecordPlayingEvent {
    @IntDef({TYPE_PAUSE, TYPE_PLAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayingActions {}

    public static final byte TYPE_PAUSE = 1, TYPE_PLAY = 2;

    public final byte eventType;

    private static SparseArray<PlayOrPauseCurrentRecordPlayingEvent> eventsByType = new SparseArray<>();
    static {
        eventsByType.put(TYPE_PAUSE, new PlayOrPauseCurrentRecordPlayingEvent(TYPE_PAUSE));
        eventsByType.put(TYPE_PLAY, new PlayOrPauseCurrentRecordPlayingEvent(TYPE_PLAY));
    }

    protected PlayOrPauseCurrentRecordPlayingEvent(@PlayingActions byte eventType) {
        this.eventType = eventType;
    }

    public static PlayOrPauseCurrentRecordPlayingEvent getEvent(@PlayingActions byte eventType) {
        return eventsByType.get(eventType);
    }
}
