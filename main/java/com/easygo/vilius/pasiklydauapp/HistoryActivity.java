package com.easygo.vilius.pasiklydauapp;

/**
 * Created by Vilius on 2016-11-24.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Klase vietoviu istorijai isvesti
 */
public class HistoryActivity extends AppCompatActivity {

        ArrayList<LocationEntry> mContents;     //isvedami duomenys
        String address;                         //adresas
        LocationAdapter mAdapter;               //Vietoviu adapteris

        Button delete;                          //delete mygtukas
        ListView mListView;                     //Listview isvesti duomenims
        DatabaseHelper dbhandler;               //Duombaze apdorojantis DatabaseHelper objektas

    /**
     * onCreate metodas sukuria ListView su adapteriumi
     * @param savedInstanceState
     */
    @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.activity_leaderboard);
                dbhandler = new DatabaseHelper(this);
                prepareContent();

                String[] from = new String[] { "Address", "Date"};
                int[] to = new int[] {R.id.listview_name, R.id.listview_score};

                mAdapter = new  LocationAdapter(mContents, this);
                mListView = (ListView)findViewById(R.id.leaderboard_listview);

                delete=(Button)findViewById(R.id.delete_btn);

                mListView.setAdapter(mAdapter);
                delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                mListView.setAdapter(null);
                                dbhandler.delete();

                                Toast.makeText(getApplicationContext(), "Succesfully removed", Toast.LENGTH_LONG).show();

                        }
                });
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView textView = (TextView) findViewById(R.id.leaderboard_header);
                                textView.setText(parent.getItemAtPosition(position).toString());
                        }
                });
        }

    /**
     * PrepareContent metodas - isveda duomenis i listview
     */
    private void prepareContent()
        {
                mContents = new ArrayList<>();
                address = getIntent().getStringExtra("tikadresas");
                if(!Objects.equals(address, "")) {
                        LocationEntry loc = new LocationEntry(0, address, "");
                        dbhandler.addEntry(loc);
                        Toast.makeText(getApplicationContext(), "Succesfully added", Toast.LENGTH_LONG).show();
                }
                else
                        Toast.makeText(getApplicationContext(), "Invalid address", Toast.LENGTH_LONG).show();
                        ArrayList<LocationEntry> entries = dbhandler.getAllEntries();
                mContents = entries;

        }
}