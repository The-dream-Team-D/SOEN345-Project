package com.example.popin.logic;

import static com.example.popin.logic.EventItem.FormatTime;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.popin.UIpages.EventDetailActivity;
import com.example.popin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<EventItem> allEvents;
    private final List<EventItem> visibleEvents;
    private String currentQuery = "";
    private final Set<EventCategory> selectedCategories = new HashSet<>();
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
        holder.dateTime.setText(FormatTime(event.getDateTime()));
        holder.location.setText(event.getLocation());
        holder.eventCategory.setText(event.getCategory().toString());

        if (holder.eventImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(event.getImgURL())
                    .override(200, 200)
                    .dontAnimate()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.eventImage);

        }
        applyTagColor(holder.eventCategory, event.getCategory().toString());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
            intent.putExtra("title", event.getTitle());
            intent.putExtra("dateTime", FormatTime(event.getDateTime()));
            intent.putExtra("location", event.getLocation());
            intent.putExtra("details", event.getDetails());
            intent.putExtra("imgURL", event.getImgURL());
            intent.putExtra("eventCategory", event.getCategory().toString());
            intent.putExtra("capacity", event.getCapacity());
            intent.putExtra("attendees", event.getAttendeeCount());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return visibleEvents.size();
    }

    public void updateList(List<EventItem> newList, boolean futureOnly) {
        allEvents.clear();
        allEvents.addAll(newList);
        filter(currentQuery, null, false, futureOnly);
    }

    public void filter(
            String query,
            Set<EventCategory> selectedCategories,
            boolean inNextThirtyDaysRequest,
            boolean futureOnly
    ) {
        currentQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        visibleEvents.clear();

        long now = System.currentTimeMillis();

        for (EventItem event : allEvents) {
            if (event.getDateTime() >= now || !futureOnly){
                boolean matchesCategory = selectedCategories == null || selectedCategories.isEmpty()
                        || selectedCategories.contains(event.getCategory());

                boolean matchesThirtyDayWindow;
                long thirtyDaysLater = now + (30L * 24 * 60 * 60 * 1000);

                Log.d("FILTER", "Now: " + new Date(now));
                Log.d("FILTER", "Event: " + new Date(event.getDateTime()));
                Log.d("FILTER", "30 days later: " + new Date(thirtyDaysLater));

                if(inNextThirtyDaysRequest){
                    matchesThirtyDayWindow = event.getDateTime() <= thirtyDaysLater;
                }else{
                    matchesThirtyDayWindow = true;
                }


                // Check search query match
                boolean matchesQuery;
                if (currentQuery.isEmpty()) {
                    matchesQuery = true;
                } else {
                    String haystack = (event.getTitle() + " " + event.getDateTime() + " " + event.getLocation())
                            .toLowerCase(Locale.ROOT);
                    matchesQuery = haystack.contains(currentQuery);
                }

                if (matchesCategory && matchesQuery && matchesThirtyDayWindow) {
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
        private final ImageView eventImage;
        private final TextView eventCategory;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvEventTitle);
            dateTime = itemView.findViewById(R.id.tvEventDateTime);
            location = itemView.findViewById(R.id.tvEventLocation);
            eventImage = itemView.findViewById(R.id.ivEventImage);
            eventCategory = itemView.findViewById(R.id.tvEventCategory);
        }
    }



    private void applyTagColor(TextView tag, String category) {
        String key = category == null ? "" : category.trim().toLowerCase();
        int color;

        switch (key) {
            case "social":        color = ContextCompat.getColor(tag.getContext(), R.color.categorySocialColor); break;
            case "educational":   color = ContextCompat.getColor(tag.getContext(), R.color.categoryEducationalColor); break;
            case "professional":  color = ContextCompat.getColor(tag.getContext(), R.color.categoryProfessionalColor); break;
            case "sports":        color = ContextCompat.getColor(tag.getContext(), R.color.categorySportsColor); break;
            case "entertainment": color = ContextCompat.getColor(tag.getContext(), R.color.categoryEntertainmentColor); break;
            case "community":     color = ContextCompat.getColor(tag.getContext(), R.color.categoryCommunityColor); break;
            default:              color = ContextCompat.getColor(tag.getContext(), R.color.categoryDefaultColor); break;
        }

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(40f);
        bg.setColor(color);

        tag.setText(category);
        tag.setBackground(bg);
    }

}