package com.sherchan.pbedemo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<Device> fallDetail;
    Context context;

    // Constructor for initialization
    public Adapter(Context context, ArrayList<Device> fallDetail) {
        this.context = context;
        this.fallDetail = fallDetail;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml layout file into View object)
        View v = LayoutInflater.from(context).inflate(R.layout.list, parent, false);

        // Passing view to ViewHolder
        return new ViewHolder(v);
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // TypeCast Object to int type
        Device device = fallDetail.get(position);
        holder.mFall.setText(device.getFallStatus());
        holder.mTime.setText(device.getCurrentTime());
        holder.mDate.setText(device.getCurrentDate());
    }

    @Override
    public int getItemCount() {
        // Returns number of items currently available in Adapter
        return fallDetail.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mFall;
        TextView mTime;
        TextView mDate;

        public ViewHolder(View view) {
            super(view);
            mFall = (TextView) view.findViewById(R.id.fallTv);
            mTime = (TextView) view.findViewById(R.id.timeTv);
            mDate = (TextView) view.findViewById(R.id.dateTv);

        }
    }
}

