package com.yeryomenkom.testaudiorecorder;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.yeryomenkom.testaudiorecorder.events.NewRecordStartPlayingEvent;
import com.yeryomenkom.testaudiorecorder.events.PlayNewRecordEvent;
import com.yeryomenkom.testaudiorecorder.events.PlayOrPauseCurrentRecordPlayingEvent;
import com.yeryomenkom.testaudiorecorder.events.RecordRemovedEvent;
import com.yeryomenkom.testaudiorecorder.events.SeekToPositionOfRecordRecordingEvent;
import com.yeryomenkom.testaudiorecorder.events.UpdatePlayingProcessRecordingEvent;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by Misha on 9/5/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private final int NOTIFICATION_ID = 2;
    private MediaPlayer mMediaPlayer;
    private File mCurrentPlayingRecord;

    private boolean needUpdateProgressOfPlaying;
    private android.os.Handler handler = new android.os.Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                EventBus.getDefault().postSticky(UpdatePlayingProcessRecordingEvent
                        .getEvent(mMediaPlayer.getCurrentPosition()));
            }
            if(needUpdateProgressOfPlaying) handler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        needUpdateProgressOfPlaying = true;
        handler.post(mRunnable);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void onEvent(SeekToPositionOfRecordRecordingEvent event) {
        mMediaPlayer.seekTo(event.position);
        EventBus.getDefault().postSticky(UpdatePlayingProcessRecordingEvent
                .getEvent(mMediaPlayer.getCurrentPosition()));
    }

    public void onEvent(PlayOrPauseCurrentRecordPlayingEvent event) {
        if(event.eventType == PlayOrPauseCurrentRecordPlayingEvent.TYPE_PAUSE) {
            mMediaPlayer.pause();
            stopForeground(true);
        } else {
            mMediaPlayer.start();
            startForeground(NOTIFICATION_ID, createPlayerNotification());
        }
    }

    public void onEvent(RecordRemovedEvent event) {
        if(event.record == mCurrentPlayingRecord) {
            mMediaPlayer.reset();
        }
    }

    public void onEvent(PlayNewRecordEvent event) {
        mCurrentPlayingRecord = event.record;
        mMediaPlayer.reset();

        try {
            mMediaPlayer.setDataSource(mCurrentPlayingRecord.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.message_error_while_playing_record, Toast.LENGTH_SHORT).show();
            return;
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.prepareAsync();
    }

    private Notification createPlayerNotification() {
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(this);

        notBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.tittle_playing))
                .setContentText(mCurrentPlayingRecord.getName());

        return notBuilder.build();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().postSticky(NewRecordStartPlayingEvent
                .getEvent(mCurrentPlayingRecord, mp.getDuration()));
        EventBus.getDefault().postSticky(PlayOrPauseCurrentRecordPlayingEvent
                .getEvent(PlayOrPauseCurrentRecordPlayingEvent.TYPE_PLAY));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
