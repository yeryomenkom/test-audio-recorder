package com.yeryomenkom.testaudiorecorder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Misha on 9/4/2015.
 */
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private ArrayList<File> records;
    private File currentPlayingRecord;
    private boolean isPlayingNow;
    private int currentPlayingProgress, currentRecordDuration;
    private RecordsAdapterListener listener;

    public RecordsAdapter() {
        records = new ArrayList<>(0);
    }

    public boolean isPlayingNow() {
        return isPlayingNow;
    }

    public File getCurrentPlayingRecord() {
        return currentPlayingRecord;
    }

    public void setCurrentPlayingProgress(int currentPlayingProgress) {
        this.currentPlayingProgress = currentPlayingProgress;
    }

    public ArrayList<File> getRecords() {
        return records;
    }

    public void setCurrentRecordDuration(int currentRecordDuration) {
        this.currentRecordDuration = currentRecordDuration;
    }

    public void setIsPlayingNow(boolean isPlayingNow) {
        this.isPlayingNow = isPlayingNow;
    }

    public void setListener(RecordsAdapterListener listener) {
        this.listener = listener;
    }

    public void setRecords(ArrayList<File> records) {
        this.records = records;
    }

    public void setCurrentPlayingRecord(@Nullable File currentPlayingRecord) {
        this.currentPlayingRecord = currentPlayingRecord;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_in_records_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File record = records.get(position);

        holder.tvRecordName.setText(record.getName());
        if(record == currentPlayingRecord) {
            holder.ivPlay.setImageResource(isPlayingNow ? R.drawable.ic_pause_grey600_48dp
                    : R.drawable.ic_play_grey600_48dp);
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.seekBar.setMax(currentRecordDuration);
            holder.seekBar.setProgress(currentPlayingProgress);
        } else {
            holder.ivPlay.setImageResource(R.drawable.ic_play_grey600_48dp);
            holder.seekBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private void onPlayBtnClicked(int position) {
        if(listener != null) {
            if(!isPlayingNow || currentPlayingRecord != records.get(position)) {
                listener.onPlayBtnClicked(records.get(position));
            } else {
                listener.onPauseBtnClicked(records.get(position));
            }
        }
    }

    private void onDeleteBtnClicked(int position) {
        if(listener != null) {
            listener.onDeleteBtnClicked(records.get(position), position);
        }
    }

    void onSeekBarChangedFromUser(int position) {
        if(listener != null) {
            listener.onSeekBarChangedFromUser(position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        SeekBar seekBar;
        TextView tvRecordName;
        ImageView ivPlay;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRecordName = (TextView) itemView.findViewById(R.id.tv_record_name_IR);
            ivPlay = (ImageView) itemView.findViewById(R.id.iv_play_IR);
            ivPlay.setOnClickListener(this);
            itemView.findViewById(R.id.iv_delete_IR).setOnClickListener(this);

            seekBar = (SeekBar) itemView.findViewById(R.id.seek_bar_IR);
            seekBar.setMax(100);
            seekBar.setOnSeekBarChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_delete_IR:
                    onDeleteBtnClicked(getAdapterPosition());
                    break;
                case R.id.iv_play_IR:
                    onPlayBtnClicked(getAdapterPosition());
                    break;
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) onSeekBarChangedFromUser(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    interface RecordsAdapterListener {
        void onPlayBtnClicked(File file);
        void onPauseBtnClicked(File file);
        void onDeleteBtnClicked(File file, int position);
        void onSeekBarChangedFromUser(int position);
    }
}
