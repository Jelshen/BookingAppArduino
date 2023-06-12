package com.example.seapp;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Booking extends androidx.fragment.app.DialogFragment {

    private TextView name;
    private TextView plateNo;
    private TextView bookNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.booking_fragment, container, false);

        TextView details = (TextView) view.findViewById(R.id.details);
        SpannableString content = new SpannableString("Your details");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        details.setText(content);

        // name = view.findViewById(R.id.bookerName);
        // plateNo = view.findViewById(R.id.bookerPlateNo);
        bookNo = view.findViewById(R.id.bookerSlotNo);
        Bundle data = getArguments();
        bookNo.setText(data.getString("ID"));

        return view;
    }
}
