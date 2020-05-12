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
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;

public class ScheduleSlotAdapter extends RecyclerView.Adapter<ScheduleSlotAdapter.ViewHolder>{
    private TimeslotsPojo[] timeslots;
    private final Context context;
    private int checkedPosition=-1;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String name = timeslots[position].getName();
        holder.timeSlot.setText(name);
        holder.timeSlot.setTextColor(context.getColor(R.color.colorBlack));
        holder.cardView.setCardBackgroundColor(context.getColor(R.color.colorWhite));
        if (position==checkedPosition){
            holder.timeSlot.setTextColor(context.getColor(R.color.colorWhite));
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.colorOrange));
        }
    }
    public void updateList(TimeslotsPojo[] timeslots) {
        this.timeslots = timeslots;
        checkedPosition=-1;
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
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.slot_time);
            cardView=itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (checkedPosition==getAdapterPosition())
                        checkedPosition=-1;
                    else
                        checkedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
