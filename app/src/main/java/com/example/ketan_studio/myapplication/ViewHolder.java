package com.example.ketan_studio.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView id;
    ImageView img;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        id = (TextView)itemView.findViewById(R.id.titleid);
        img = (ImageView)itemView.findViewById(R.id.img);
    }
}
