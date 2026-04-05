package com.example.popin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<EventItem> allEvents;
    private final List<EventItem> visibleEvents;

    private String currentQuery = "";
    private String currentDate = "Date";
    private String currentLocation = "Location";
    private String currentCategory = "Category";

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
            intent.putExtra("category", event.getCategory());
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
        applyFilters();
    }

    public void filter(String query, String date, String location, String category) {
        currentQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        currentDate = date == null ? "Date" : date;
        currentLocation = location == null ? "Location" : location;
        currentCategory = category == null ? "Category" : category;
        applyFilters();
    }

    private void applyFilters() {
        visibleEvents.clear();

        for (EventItem event : allEvents) {
            String title = safeLower(event.getTitle());
            String dateTime = safeLower(event.getDateTime());
            String location = safeLower(event.getLocation());
            String details = safeLower(event.getDetails());
            String category = safeLower(event.getCategory());

            String haystack = title + " " + dateTime + " " + location + " " + details + " " + category;

            boolean matchesQuery = currentQuery.isEmpty() || haystack.contains(currentQuery);

            boolean matchesDate = currentDate.equals("Date")
                    || currentDate.equals("All Dates")
                    || safe(event.getDateTime()).contains(currentDate);

            boolean matchesLocation = currentLocation.equals("Location")
                    || currentLocation.equals("All Locations")
                    || safe(event.getLocation()).equalsIgnoreCase(currentLocation);

            boolean matchesCategory = currentCategory.equals("Category")
                    || currentCategory.equals("All Categories")
                    || safe(event.getCategory()).equalsIgnoreCase(currentCategory);

            if (matchesQuery && matchesDate && matchesLocation && matchesCategory) {
                visibleEvents.add(event);
            }
        }

        notifyDataSetChanged();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String safeLower(String value) {
        return safe(value).toLowerCase(Locale.ROOT);
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