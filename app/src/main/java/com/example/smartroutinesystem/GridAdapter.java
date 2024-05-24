package com.example.smartroutinesystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final Context context;
    private final List<ViewData> items;

    public GridAdapter(Context context, List<ViewData> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textViewTime;
        TextView textViewName;
        TextView textViewCourse;
        TextView textViewRoom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.textViewTime = convertView.findViewById(R.id.textViewTime);
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            holder.textViewCourse = convertView.findViewById(R.id.textViewCourse);
            holder.textViewRoom = convertView.findViewById(R.id.textViewRoom);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewData item = items.get(position);
        holder.textViewTime.setText(item.getTime());
        holder.textViewName.setText(item.getName());
        holder.textViewCourse.setText(item.getCourse());
        holder.textViewRoom.setText(item.getRoom());

        return convertView;
    }
}
