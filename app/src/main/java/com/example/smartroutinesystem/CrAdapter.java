package com.example.smartroutinesystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CrAdapter extends RecyclerView.Adapter<CrAdapter.CrViewHolder> {

    private Context context;
    private List<Cr> crList;

    public CrAdapter(Context context, List<Cr> crList) {
        this.context = context;
        this.crList = crList;
    }

    @NonNull
    @Override
    public CrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cr, parent, false);
        return new CrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrViewHolder holder, int position) {
        Cr cr = crList.get(position);
        holder.nameTextView.setText(cr.getName());
        holder.rollTextView.setText(cr.getRollNumber());
        holder.deptTextView.setText("Department: " + cr.getDepartment());
        holder.seriesTextView.setText("Series: " + cr.getSeries());
        holder.sectionTextView.setText("Section: " + cr.getSection());

        holder.removeCrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCr(cr.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return crList.size();
    }

    public class CrViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, rollTextView, deptTextView, seriesTextView, sectionTextView;
        Button removeCrButton;

        public CrViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            rollTextView = itemView.findViewById(R.id.rollTextView);
            deptTextView = itemView.findViewById(R.id.deptTextView);
            seriesTextView = itemView.findViewById(R.id.seriesTextView);
            sectionTextView = itemView.findViewById(R.id.sectionTextView);
            removeCrButton = itemView.findViewById(R.id.removeCrButton);
        }
    }

    private void removeCr(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        databaseReference.child("cr").setValue("No").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "CR role removed successfully", Toast.LENGTH_SHORT).show();
                reloadActivity();
            } else {
                Toast.makeText(context, "Failed to remove CR role", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reloadActivity() {
        Intent intent = new Intent(context, RemoveCrActivity.class);
        context.startActivity(intent);
        ((RemoveCrActivity) context).finish();
    }
}
