package com.yeryomenkom.testaudiorecorder.events;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecordingEvent {
    @IntDef({TYPE_RECORDING_AUDIO_NOW, TYPE_NOT_RECORDING_AUDIO_NOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventTypes {}

    public static final byte TYPE_RECORDING_AUDIO_NOW = 1,
                            TYPE_NOT_RECORDING_AUDIO_NOW = 2;

    public final byte eventType;

    private static SparseArray<RecordingEvent> eventsByType = new SparseArray<>();
    static {
        eventsByType.put(TYPE_NOT_RECORDING_AUDIO_NOW, new RecordingEvent(TYPE_NOT_RECORDING_AUDIO_NOW));
        eventsByType.put(TYPE_RECORDING_AUDIO_NOW, new RecordingEvent(TYPE_RECORDING_AUDIO_NOW));
    }

    protected RecordingEvent(@EventTypes byte eventType) {
        this.eventType = eventType;
    }

    public static RecordingEvent getEvent(@EventTypes byte eventType) {
        return eventsByType.get(eventType);
    }
}
