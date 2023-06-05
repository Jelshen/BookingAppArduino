package com.example.seapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView parkText[] = new TextView[12];
    private TextView unfocus;
    private String id = "";
    private int rid;
    private int parkingId[] = {R.id.A1, R.id.A2, R.id.A3, R.id.A4, R.id.A5, R.id.A6,
            R.id.B1, R.id.B2, R.id.B3, R.id.B4, R.id.B5, R.id.B6};

    private String slot[] = {"slot_1", "slot_2", "slot_3", "slot_4", "slot_5", "slot_6",
            "slot_7", "slot_8", "slot_9", "slot_10", "slot_11", "slot_12"};

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://seapp-6f4a5-default-rtdb.asia-southeast1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < parkText.length; i++) {
            parkText[i] = (TextView) findViewById(parkingId[i]);
            parkText[i].setOnClickListener(this);
        }

        setColor();

        unfocus = parkText[0];

        Button confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.equals("")){
                    Toast.makeText(MainActivity.this, "nu balek pilihna goblog", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference slotsRef = database.getReference("Parking_slots");

                    for(int i=0; i < parkingId.length; i++){
                        Log.d("view id", String.valueOf(rid) + " " + String.valueOf(parkingId[i]));

                        if(rid == parkingId[i]){
                            String index = slot[i];
                            Log.d("Slot print", "onClick: " + slot[i]);
                            slotsRef.child(index).setValue("booked");
                        }
                    }

                    Booking displayData = new Booking();
                    Bundle data = new Bundle();
                    data.putString("ID", id);

                    displayData.setArguments(data);
                    displayData.show(getSupportFragmentManager(), "display");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        setFocus(unfocus, (TextView) findViewById(view.getId()));
        TextView bookedSlot = (TextView) findViewById(view.getId());
        id = bookedSlot.getText().toString();
        rid = view.getId();
    }

    private void setFocus(TextView unfocus, TextView focus){
        unfocus.setBackground(getDrawable(R.drawable.button));
        unfocus.setElevation(0);
        setColor();
        focus.setBackground(getDrawable(R.drawable.button_selected));
        focus.setElevation(50);
        this.unfocus = focus;
    }

    private void setColor(){
        DatabaseReference slotsRef = database.getReference("Parking_slots");
        for (int i = 0; i < parkText.length; i++) {
            final int index = i;
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String status = dataSnapshot.getValue(String.class);
                    if (status != null) {
                        if (status.equals("taken")) {
                            parkText[index].setBackground(getDrawable(R.drawable.button_taken));
                            parkText[index].setTextColor(Color.BLACK);
                            parkText[index].setEnabled(false);
                        } else if (status.equals("empty")) {
                            parkText[index].setEnabled(true);
                        } else if (status.equals("booked")){
                            parkText[index].setBackground(getDrawable(R.drawable.button_booked));
                            parkText[index].setEnabled(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error retrieving data: " + databaseError.getMessage());
                }
            };
            DatabaseReference slotRef = slotsRef.child("slot_" + (i + 1));
            slotRef.addValueEventListener(valueEventListener);
        }
    }
}