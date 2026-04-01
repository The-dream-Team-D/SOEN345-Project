package com.example.popin.logic;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popin.UIpages.EventDetailActivity;
import com.example.popin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<EventItem> allEvents;
    private final List<EventItem> visibleEvents;
    private String currentQuery = "";

    public EventAdapter(List<EventItem> events) {
        this.allEvents = new ArrayList<>(events);
        this.visibleEvents = new ArrayList<>(events);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventItem event = visibleEvents.get(position);
        holder.title.setText(event.getTitle());
        holder.dateTime.setText(event.getDateTime());
        holder.location.setText(event.getLocation());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
            intent.putExtra("title", event.getTitle());
            intent.putExtra("dateTime", event.getDateTime());
            intent.putExtra("location", event.getLocation());
            intent.putExtra("details", event.getDetails());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return visibleEvents.size();
    }

    public void updateList(List<EventItem> newList) {
        allEvents.clear();
        allEvents.addAll(newList);
        filter(currentQuery);
    }

    public void filter(String query) {
        currentQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        visibleEvents.clear();
        if (currentQuery.isEmpty()) {
            visibleEvents.addAll(allEvents);
        } else {
            for (EventItem event : allEvents) {
                String haystack = (event.getTitle() + " " + event.getDateTime() + " " + event.getLocation())
                        .toLowerCase(Locale.ROOT);
                if (haystack.contains(currentQuery)) {
                    visibleEvents.add(event);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dateTime;
        private final TextView location;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvEventTitle);
            dateTime = itemView.findViewById(R.id.tvEventDateTime);
            location = itemView.findViewById(R.id.tvEventLocation);
        }
    }
}