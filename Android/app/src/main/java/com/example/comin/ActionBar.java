package com.example.comin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ActionBar extends Fragment {
    private String title = null;
    private int backBtn = 0;
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.custombar, container, false);

        if (title != null) {
            v.title = title;
        }
        if (leftButton != 0) {
            v.title_button_left.visibility = View.VISIBLE;
            v.title_button_left.setImageResource(leftButton);
        }
        if (rightButton != 0) {
            view.title_button_right.visibility = View.VISIBLE;
            view.title_button_right.setImageResource(rightButton);
        }

        return v;

    }

 */
}
