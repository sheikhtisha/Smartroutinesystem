package com.example.smartroutinesystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineHolder extends RecyclerView.ViewHolder {

    TextView teacherView, courseView, roomView;
    public RoutineHolder(@NonNull View itemView) {
        super(itemView);
        teacherView=itemView.findViewById(R.id.textViewName);
        courseView=itemView.findViewById(R.id.textViewCourse);
        roomView=itemView.findViewById(R.id.textViewRoom);
    }
}
