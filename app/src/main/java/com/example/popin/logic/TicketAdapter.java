package com.example.popin.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.title.setText(ticket.getTitle());
        holder.dateTime.setText(ticket.getDateTime());
        holder.location.setText(ticket.getLocation());

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

        if (currentQuery.isEmpty()) {
            visibleTickets.addAll(allTickets);
        } else {
            for (TicketItem ticket : allTickets) {
                String text = (ticket.getTitle() + " " + ticket.getDateTime() + " " + ticket.getLocation())
                        .toLowerCase(Locale.ROOT);
                if (text.contains(currentQuery)) {
                    visibleTickets.add(ticket);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dateTime;
        private final TextView location;
        private final Button cancelButton;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTicketTitle);
            dateTime = itemView.findViewById(R.id.tvTicketDateTime);
            location = itemView.findViewById(R.id.tvTicketLocation);
            cancelButton = itemView.findViewById(R.id.btnCancelTicket);
        }
    }
}
