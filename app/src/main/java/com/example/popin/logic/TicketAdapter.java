package com.example.popin.logic;

import static com.example.popin.logic.EventItem.FormatTime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.popin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    public interface OnCancelClickListener {
        void onCancelClick(TicketItem ticket);
    }

    public interface OnTicketClickListener {
        void onTicketClick(TicketItem ticket);
    }

    private final List<TicketItem> allTickets;
    private final List<TicketItem> visibleTickets;
    private final OnCancelClickListener cancelClickListener;
    private final OnTicketClickListener ticketClickListener;
    private String currentQuery = "";

    private EventFilterDateType currentFilter = EventFilterDateType.ALL;
    public TicketAdapter(
            List<TicketItem> tickets,
            OnCancelClickListener cancelClickListener,
            OnTicketClickListener ticketClickListener
    ) {
        this.allTickets = new ArrayList<>(tickets);
        this.visibleTickets = new ArrayList<>(tickets);
        this.cancelClickListener = cancelClickListener;
        this.ticketClickListener = ticketClickListener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketItem ticket = visibleTickets.get(position);
        long now = System.currentTimeMillis();  // only once per bind

        holder.title.setText(ticket.getTitle());
        holder.dateTime.setText(FormatTime(ticket.getDateTime()));
        holder.location.setText(ticket.getLocation());

        if (ticket.getDateTime() > now) {
            holder.cancelButton.setVisibility(View.VISIBLE);
        } else {
            holder.cancelButton.setVisibility(View.GONE);
        }

        if (holder.eventImage != null) {
            Glide.with(holder.itemView.getContext())
                    .load(ticket.getImgURL())
                    .override(200, 200)
                    .dontAnimate()
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(holder.eventImage);
        }

        holder.cancelButton.setOnClickListener(v -> cancelClickListener.onCancelClick(ticket));
        holder.itemView.setOnClickListener(v -> ticketClickListener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return visibleTickets.size();
    }

    public void updateList(List<TicketItem> newList) {
        allTickets.clear();
        allTickets.addAll(newList);
        filter(currentQuery);
    }

    public void filter(String query) {
        currentQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        visibleTickets.clear();

        long now = System.currentTimeMillis();

        for (TicketItem ticket : allTickets) {

            boolean matchesDate = true;
            long eventTime = ticket.getDateTime();

            if (currentFilter == EventFilterDateType.PAST) {
                matchesDate = eventTime < now;
            } else if (currentFilter == EventFilterDateType.UPCOMING) {
                matchesDate = eventTime >= now;
            }

            String text = (ticket.getTitle() + " " + ticket.getDateTime() + " " + ticket.getLocation())
                    .toLowerCase(Locale.ROOT);
            boolean matchesSearch = currentQuery.isEmpty() || text.contains(currentQuery);

            if (matchesDate && matchesSearch) {
                visibleTickets.add(ticket);
            }
        }

        notifyDataSetChanged();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dateTime;
        private final TextView location;
        private final Button cancelButton;
        private final ImageView eventImage;
        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTicketTitle);
            dateTime = itemView.findViewById(R.id.tvTicketDateTime);
            location = itemView.findViewById(R.id.tvTicketLocation);
            cancelButton = itemView.findViewById(R.id.btnCancelTicket);
            eventImage = itemView.findViewById(R.id.tvEventImage);
        }
    }


    public void setFilter(EventFilterDateType filter) {
        this.currentFilter = filter;
        filter(currentQuery);
    }

}
