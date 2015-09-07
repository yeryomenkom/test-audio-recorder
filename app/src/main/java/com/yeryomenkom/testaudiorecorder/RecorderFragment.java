package com.yeryomenkom.testaudiorecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeryomenkom.testaudiorecorder.events.RecordingEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecorderFragment extends Fragment implements View.OnClickListener {
    private TextView mInfoTextView;
    private boolean mRecordingNow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        mInfoTextView = (TextView) view.findViewById(R.id.tv_info_FRR);
        mRecordingNow = false;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnClickListener(this);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().postSticky(RecordingEvent.getEvent(mRecordingNow ? RecordingEvent.TYPE_NOT_RECORDING_AUDIO_NOW
                : RecordingEvent.TYPE_RECORDING_AUDIO_NOW));
    }

    public void onEvent(RecordingEvent recordingEvent) {
        switch (recordingEvent.eventType) {
            case RecordingEvent.TYPE_NOT_RECORDING_AUDIO_NOW:
                mRecordingNow = false;
                mInfoTextView.setText(R.string.message_tap_to_recording);
                break;
            case RecordingEvent.TYPE_RECORDING_AUDIO_NOW:
                mRecordingNow = true;
                mInfoTextView.setText(R.string.message_recording);
                break;
        }
    }
}
