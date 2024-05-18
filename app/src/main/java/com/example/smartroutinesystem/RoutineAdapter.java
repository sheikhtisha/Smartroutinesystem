package com.example.smartroutinesystem;

import android.content.ClipData;
import android.content.Context;
import android.media.RouteListingPreference;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineHolder> {
    Context context;
    List<ViewData>items;

    public RoutineAdapter(Context context, List<ViewData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RoutineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoutineHolder(LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineHolder holder, int position) {

        holder.roomView.setText(items.get(position).getRoom());
        holder.courseView.setText(items.get(position).getCourse());
        holder.teacherView.setText(items.get(position).getName());
        holder.timeView.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
