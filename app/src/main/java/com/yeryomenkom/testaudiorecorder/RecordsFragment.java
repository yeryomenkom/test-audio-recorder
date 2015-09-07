package com.yeryomenkom.testaudiorecorder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yeryomenkom.testaudiorecorder.events.NewRecordStartPlayingEvent;
import com.yeryomenkom.testaudiorecorder.events.PlayNewRecordEvent;
import com.yeryomenkom.testaudiorecorder.events.PlayOrPauseCurrentRecordPlayingEvent;
import com.yeryomenkom.testaudiorecorder.events.RecordRemovedEvent;
import com.yeryomenkom.testaudiorecorder.events.RecordsListChangedRecordingEvent;
import com.yeryomenkom.testaudiorecorder.events.SeekToPositionOfRecordRecordingEvent;
import com.yeryomenkom.testaudiorecorder.events.UpdatePlayingProcessRecordingEvent;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecordsFragment extends Fragment implements RecordsAdapter.RecordsAdapterListener {
    private RecyclerView mRecyclerView;
    private RecordsAdapter mRecordsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_records_list_FRS);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecordsAdapter = new RecordsAdapter();
        mRecyclerView.setAdapter(mRecordsAdapter);
        mRecordsAdapter.setListener(this);

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(RecordsListChangedRecordingEvent event) {
        mRecordsAdapter.setRecords(event.recordsList);
        mRecordsAdapter.notifyDataSetChanged();
    }

    public void onEvent(UpdatePlayingProcessRecordingEvent event) {
        mRecordsAdapter.setCurrentPlayingProgress(event.progress);
        mRecordsAdapter.notifyDataSetChanged();
    }

    public void onEvent(NewRecordStartPlayingEvent event) {
        mRecordsAdapter.setCurrentRecordDuration(event.recordDuration);
        mRecordsAdapter.setCurrentPlayingRecord(event.record);
        mRecordsAdapter.setCurrentPlayingProgress(0);
        mRecordsAdapter.notifyDataSetChanged();
    }

    public void onEvent(PlayOrPauseCurrentRecordPlayingEvent event) {
        mRecordsAdapter.setIsPlayingNow(event.eventType == PlayOrPauseCurrentRecordPlayingEvent.TYPE_PLAY);
        mRecordsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayBtnClicked(File file) {
        if(mRecordsAdapter.getCurrentPlayingRecord() == file) {
            Log.d(getTag(), "Pause current record playing "+mRecordsAdapter.isPlayingNow());
            EventBus.getDefault().postSticky(PlayOrPauseCurrentRecordPlayingEvent
                    .getEvent(mRecordsAdapter.isPlayingNow() ?
                            PlayOrPauseCurrentRecordPlayingEvent.TYPE_PAUSE :
                            PlayOrPauseCurrentRecordPlayingEvent.TYPE_PLAY));
        } else {
            EventBus.getDefault().postSticky(PlayNewRecordEvent.getEvent(file));
        }
    }


    @Override
    public void onPauseBtnClicked(File file) {
        EventBus.getDefault().postSticky(PlayOrPauseCurrentRecordPlayingEvent
                .getEvent(PlayOrPauseCurrentRecordPlayingEvent.TYPE_PAUSE));
    }

    @Override
    public void onDeleteBtnClicked(File file, int position) {
        EventBus.getDefault().post(RecordRemovedEvent.getEvent(file));
        Toast.makeText(getActivity(), file.getName() + " has been removed", Toast.LENGTH_SHORT).show();
        mRecordsAdapter.getRecords().remove(file);
        mRecordsAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onSeekBarChangedFromUser(int position) {
        EventBus.getDefault().post(SeekToPositionOfRecordRecordingEvent.getEvent(position));
    }
}
