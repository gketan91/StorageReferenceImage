package com.example.ketan_studio.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShowActivity extends AppCompatActivity {

    RecyclerView recycler;
    FirebaseRecyclerAdapter<Model,ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        recycler = (RecyclerView)findViewById(R.id.recycerViewID);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        showing();
    }

    private void showing() {
        DatabaseReference mtReff = FirebaseDatabase.getInstance().getReference("Image");
        FirebaseRecyclerOptions opt = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mtReff,Model.class).build();
        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(opt) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                holder.id.setText(model.getTitle());
                String url = model.getImage();

                Picasso.get().load(url).fit().into(holder.img);

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);
                return new ViewHolder(v);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);
    }
}
