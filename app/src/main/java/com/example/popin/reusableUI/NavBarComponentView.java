package com.example.popin.reusableUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.popin.UIpages.AdminDashboardActivity;
import com.example.popin.UIpages.EventsPageActivity;
import com.example.popin.UIpages.MyTicketsActivity;
import com.example.popin.UIpages.ProfileActivity;
import com.example.popin.R;
import com.example.popin.logic.UserInSession;

public class NavBarComponentView extends LinearLayout {

    public NavBarComponentView(Context context) {
        super(context);
        inflate(context, R.layout.common_nav_bar, this);
    }

    public NavBarComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.common_nav_bar, this);

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

        findViewById(R.id.nav_adminDashboard_container).setOnClickListener(v -> {
            if (!(v.getContext() instanceof AdminDashboardActivity)) {

                Intent intent = new Intent(v.getContext(), AdminDashboardActivity.class);

                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public static void setup(View navBarView) {

        View adminOnlyNav = navBarView.findViewById(R.id.nav_adminDashboard_container);
        UserInSession session = UserInSession.getInstance();
        if(session == null || session.getUser() == null) {
            adminOnlyNav.setVisibility(View.GONE);
        }
        else {
            boolean isAdmin = session.getUser().getIsAdmin();
            if (!isAdmin) {
                adminOnlyNav.setVisibility(View.GONE);
            }
        }

    }

}

