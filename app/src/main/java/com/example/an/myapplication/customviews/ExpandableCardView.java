package com.example.an.myapplication.customviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.an.myapplication.R;

public final class ExpandableCardView extends CardView {
    public ExpandableCardView(@NonNull Context context) {
        super(context);
    }

    public ExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addHeaderView(View view) {
        final LinearLayout layout = findViewById(R.id.expandable_head_views_container);
        layout.addView(view);
    }

    public void addBodyView(View view) {
        final LinearLayout layout = findViewById(R.id.expandable_body_views_container);
        layout.addView(view);
    }

    public void removeAddedChildViews() {
        final LinearLayout head = findViewById(R.id.expandable_head_views_container);
        final LinearLayout body = findViewById(R.id.expandable_body_views_container);
        head.removeAllViews();
        body.removeAllViews();
    }
}
