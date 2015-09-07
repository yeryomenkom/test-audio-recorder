package com.yeryomenkom.testaudiorecorder;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.yeryomenkom.testaudiorecorder.events.RecordingEvent;
import com.yeryomenkom.testaudiorecorder.events.RecordsListChangedRecordingEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecorderService extends Service {
    private final int NOTIFICATION_ID = 1;
    private MediaRecorder mMediaRecorder;
    private File mRecordsDir, mCurrentRecordFile;
    private Date mDate;
    private SimpleDateFormat mSimpleDateFormat;
    private ArrayList<File> mRecords;


    @Override
    public void onCreate() {
        mDate = new Date();
        mSimpleDateFormat = new SimpleDateFormat("HH mm ss dd MM yyyy", Locale.getDefault());
        mRecordsDir = new File(Environment.getExternalStorageDirectory().getPath()+"/TestAudioRecorder");
        mRecordsDir.mkdir();
        loadOldRecords();
        EventBus.getDefault().registerSticky(this);
    }

    private void loadOldRecords() {
        mRecords = new ArrayList<>();
        Collections.addAll(mRecords, mRecordsDir.listFiles());
        EventBus.getDefault().postSticky(RecordsListChangedRecordingEvent.getEvent(mRecords));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void onEvent(RecordingEvent recordingEvent) {
        switch (recordingEvent.eventType) {
            case RecordingEvent.TYPE_NOT_RECORDING_AUDIO_NOW:
                stopRecording();
                break;
            case RecordingEvent.TYPE_RECORDING_AUDIO_NOW:
                try {
                    startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(RecordingEvent.getEvent(RecordingEvent.TYPE_NOT_RECORDING_AUDIO_NOW));
                    Toast.makeText(this, R.string.message_error_while_recording, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void startRecording() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mCurrentRecordFile = new File(mRecordsDir+"/"+getRecordName());
        mCurrentRecordFile.createNewFile();

        mMediaRecorder.setOutputFile(mCurrentRecordFile.getPath());
        mMediaRecorder.prepare();

        startForeground(NOTIFICATION_ID, createRecordNotification());
        mMediaRecorder.start();
    }

    private void stopRecording() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mRecords.add(mCurrentRecordFile);
            EventBus.getDefault().postSticky(RecordsListChangedRecordingEvent.getEvent(mRecords));
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            if(mCurrentRecordFile != null) {
                mCurrentRecordFile.delete();
            }
        }

        mCurrentRecordFile = null;
        stopForeground(true);
    }

    private String getRecordName() {
        mDate.setTime(System.currentTimeMillis());
        return "rec "+mSimpleDateFormat.format(mDate)+".3gp";
    }

    private Notification createRecordNotification() {
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this);

        notBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.tittle_recording))
                .setContentText(mCurrentRecordFile.getName());

        return notBuilder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
