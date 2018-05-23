package com.example.admin.infinitystones;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class InfinityStones extends AppCompatActivity {

    ArrayList<String> aList = new ArrayList<>();
    ArrayAdapter<String> retrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_stones);

        final String[] Stones = {"Power", "Space", "Time", "Reality", "Soul", "Mind"};
        final int[] Colors = {R.color.Purple, R.color.Blue, R.color.Green, R.color.Red, R.color.Orange, R.color.Yellow};

        retrieved = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, aList);

        final ConstraintLayout layout = findViewById(R.id.background);
        final TextView StoneName = findViewById(R.id.t3);
        final TextView Congrats = findViewById(R.id.t1);
        final Button Stone = findViewById(R.id.GetStone);
        Button Reset = findViewById(R.id.Reset);
        Button ViewList = findViewById(R.id.ViewList);

        aList=getSavedArrayList();

        List<String> StonesList = new ArrayList<>(aList);
        Collections.sort(StonesList);
        int Count = 1;
        for (int i = 1; i < StonesList.size(); ++i) {
            if (!StonesList.get(i).equals(StonesList.get(i - 1))) {
                Count++;
            }
            if (Count == 6) {
                Congrats.setVisibility(View.VISIBLE);
            } else {
                Congrats.setVisibility(View.INVISIBLE);
            }
        }

        Stone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random value = new Random();
                int n = value.nextInt(6);
                StoneName.setText(String.format("%s%s%s", Stones[n], getString(R.string.Space), getString(R.string.Stone)));
                aList.add(Stones[n] + " " + "Stone");
                layout.setBackgroundColor(getResources().getColor(Colors[n]));

                List<String> StonesList = new ArrayList<>(aList);
                Collections.sort(StonesList);
                int Count = 1;
                for (int i = 1; i < StonesList.size(); ++i) {
                    if (!StonesList.get(i).equals(StonesList.get(i - 1))) {
                        Count++;
                    }
                    if (Count == 6) {
                        Congrats.setVisibility(View.VISIBLE);
                    } else {
                        Congrats.setVisibility(View.INVISIBLE);
                    }
                }
                saveArrayList(aList);
            }
        });

        ViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aList=getSavedArrayList();

                AlertDialog.Builder overlay = new AlertDialog.Builder(InfinityStones.this);
                retrieved = new ArrayAdapter<>(InfinityStones.this, android.R.layout.simple_list_item_1, aList);

                if(retrieved.getCount()==0) {
                    overlay.setTitle("You haven't retrieved any stones yet!");
                }else{
                    overlay.setTitle("Stones Retrieved");
                }

                overlay.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                overlay.setAdapter(retrieved, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = (String) retrieved.getItem(which);
                    }
                });
                overlay.show();
            }
        });

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Congrats.setVisibility(View.INVISIBLE);
                StoneName.setText(R.string.Initial);
                layout.setBackgroundColor(getResources().getColor(R.color.White));
                aList.clear();
                saveArrayList(aList);
                Toast.makeText(InfinityStones.this, "Infinity Stones have been Reset!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveArrayList(ArrayList<String> arrayList) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("infinitystones.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arrayList);
            out.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getSavedArrayList() {
        ArrayList savedArrayList = null;
        try {
            FileInputStream inputStream = openFileInput("infinitystones.txt");
            ObjectInputStream in = new ObjectInputStream(inputStream);
            savedArrayList = (ArrayList) in.readObject();
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return savedArrayList;
    }
}
