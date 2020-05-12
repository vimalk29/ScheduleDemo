package com.example.demoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.models.TimeslotsPojo;

public class ScheduleSlotAdapter extends RecyclerView.Adapter<ScheduleSlotAdapter.ViewHolder>{
    private TimeslotsPojo[] timeslots;
    private final Context context;

    public ScheduleSlotAdapter(Context context, TimeslotsPojo[] timeslots) {
        this.timeslots = timeslots;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_slot,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeSlot.setText(timeslots[position].getName());
    }
    public void updateList(TimeslotsPojo[] timeslots) {
        this.timeslots = timeslots;
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (timeslots==null)
            return 0;
        return timeslots.length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeSlot;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.slot_time);
        }
    }
}
