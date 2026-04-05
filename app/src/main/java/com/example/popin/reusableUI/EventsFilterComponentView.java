package com.example.popin.reusableui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.example.popin.R;
import com.example.popin.logic.EventCategory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventsFilterComponentView extends LinearLayout {

    private final Set<EventCategory> selectedCategories = new HashSet<>();
    private boolean next30Days = false;

    private OnFilterChangeListener listener;

    public EventsFilterComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.common_events_filter_component, this);

        setupButtons();
        setupSearch();
    }

    private void setupSearch() {
        EditText searchInput = findViewById(R.id.etSearchEvents);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notifyChange();
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupButtons() {
        Map<LinearLayout, EventCategory> buttonMap = new HashMap<>();
        buttonMap.put(findViewById(R.id.social_category), EventCategory.SOCIAL);
        buttonMap.put(findViewById(R.id.educational_category), EventCategory.EDUCATIONAL);
        buttonMap.put(findViewById(R.id.professional_category), EventCategory.PROFESSIONAL);
        buttonMap.put(findViewById(R.id.sports_category), EventCategory.SPORTS);
        buttonMap.put(findViewById(R.id.entertainment_category), EventCategory.ENTERTAINMENT);

        for (Map.Entry<LinearLayout, EventCategory> entry : buttonMap.entrySet()) {
            LinearLayout button = entry.getKey();
            EventCategory category = entry.getValue();

            button.setOnClickListener(v -> {
                if (selectedCategories.contains(category)) {
                    selectedCategories.remove(category);
                    v.setBackgroundTintList(
                            ContextCompat.getColorStateList(v.getContext(), R.color.black)
                    );
                } else {
                    selectedCategories.add(category);
                    v.setBackgroundTintList(
                            ContextCompat.getColorStateList(v.getContext(), R.color.action)
                    );
                }
                notifyChange();
            });
        }


        Button dateFilter = findViewById(R.id.next_month_filter);
        dateFilter.setOnClickListener(v -> {
            next30Days = !next30Days;

            v.setBackgroundTintList(ContextCompat.getColorStateList(
                    getContext(),
                    next30Days ? R.color.action : R.color.black
            ));

            notifyChange();
        });

        findViewById(R.id.clearFilters).setOnClickListener(v -> {
            selectedCategories.clear();
            next30Days = false;
            for (LinearLayout button : buttonMap.keySet()) {
                button.setBackgroundResource(R.drawable.category_filter_outline);
                button.setBackgroundTintList(
                        ContextCompat.getColorStateList(v.getContext(), R.color.black)
                );
            }
            dateFilter.setBackgroundTintList(
                    ContextCompat.getColorStateList(v.getContext(), R.color.black)
            );

            notifyChange();
        });
    }


    private void notifyChange() {
        if (listener != null) {
            String query = ((EditText) findViewById(R.id.etSearchEvents))
                    .getText().toString().trim().toLowerCase();

            listener.onFilterChanged(query, selectedCategories, next30Days);
        }
    }

    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.listener = listener;
    }

    public interface OnFilterChangeListener {
        void onFilterChanged(
                String query,
                Set<EventCategory> categories,
                boolean next30Days
        );
    }
}
