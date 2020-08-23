package com.example.noticeboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noticeboard.R;

import java.util.ArrayList;

public class NoticeFilesAdapter extends RecyclerView.Adapter<NoticeFilesAdapter.ViewHolder> {
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    public void update(String filename, String url) {
        items.add(filename);
        urls.add(url);
        notifyDataSetChanged();
    }

    public NoticeFilesAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public NoticeFilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fileview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeFilesAdapter.ViewHolder holder, int position) {
        holder.tvFileName.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildAdapterPosition(v);
                    context.startActivity(new Intent().setDataAndType(Uri.parse(urls.get(position)), Intent.ACTION_VIEW)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
}
