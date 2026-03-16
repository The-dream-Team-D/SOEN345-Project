package com.example.popin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NavBarComponentView extends LinearLayout {

    public NavBarComponentView(Context context) {
        super(context);
        inflate(context, R.layout.common_nav_bar, this);
    }

    public NavBarComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Inflate your existing XML into this view
        inflate(context, R.layout.common_nav_bar, this);

        // Set click listeners — navigation lives HERE, once, forever
        findViewById(R.id.nav_explore_container).setOnClickListener(v -> {

            if (!(v.getContext() instanceof EventsPageActivity)) {

                Intent intent = new Intent(v.getContext(), EventsPageActivity.class);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        findViewById(R.id.nav_tickets_container).setOnClickListener(v -> {
            if (!(v.getContext() instanceof MyTicketsActivity)) {

                Intent intent = new Intent(v.getContext(), MyTicketsActivity.class);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        findViewById(R.id.nav_profile_container).setOnClickListener(v -> {
            if (!(v.getContext() instanceof ProfileActivity)) {

                Intent intent = new Intent(v.getContext(), ProfileActivity.class);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

}
