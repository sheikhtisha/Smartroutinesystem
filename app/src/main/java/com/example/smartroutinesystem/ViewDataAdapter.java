package com.example.smartroutinesystem;

// ViewDataAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.smartroutinesystem.ViewData;


public class ViewDataAdapter extends RecyclerView.Adapter<ViewDataAdapter.ViewHolder> {

    private List<ViewData> dataList;
    private Context context;

    public ViewDataAdapter(Context context, List<ViewData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewData data = dataList.get(position);
        holder.textViewName.setText(data.getName());
        holder.textViewCourse.setText(data.getCourse());
        holder.textViewRoom.setText(data.getRoom());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCourse, textViewRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCourse = itemView.findViewById(R.id.textViewCourse);
            textViewRoom = itemView.findViewById(R.id.textViewRoom);
        }
    }
}
