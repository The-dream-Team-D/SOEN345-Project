package com.example.popin.reusableUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.popin.R;
import com.example.popin.UIpages.MainActivity;

public class BackButtonComponentView extends ConstraintLayout {

    public BackButtonComponentView(Context context) {
        super(context);
        inflate(context, R.layout.common_back_button, this);
    }

    public BackButtonComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.common_back_button, this);

        findViewById(R.id.backButton).setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), MainActivity.class);

            v.getContext().startActivity(intent);
            ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });
    }

}
