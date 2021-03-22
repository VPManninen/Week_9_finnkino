package com.example.week9finnkino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    Context context = null;
    Spinner dates;
    Spinner months;
    Spinner years;
    Spinner startTime;
    Spinner endTime;
    Spinner theatreListView;
    TextView infoView;
    int currentDay = Integer.parseInt(new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
    ListView displayMovies;
    Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // defining the activity variables
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dates = findViewById(R.id.dateSpinner);
        months = findViewById(R.id.monthSpinner);
        years = findViewById(R.id.yearSpinner);
        startTime = findViewById(R.id.timeSSpinner);
        endTime = findViewById(R.id.timeESpinner);
        theatreListView = findViewById(R.id.spinner);
        findButton = findViewById(R.id.searchButton);
        displayMovies = findViewById(R.id.movieDisplay);
        infoView = findViewById(R.id.infoField);
        context = MainActivity.this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Filling the spinners with required information
        ArrayAdapter<String> theatreAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getTheatreNames());
        theatreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theatreListView.setAdapter(theatreAdapter);

        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getDates());
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dates.setAdapter(dayAdapter);

        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getMonths());
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        months.setAdapter(monthAdapter);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getYears());
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(yearAdapter);

        ArrayAdapter<Integer> startTimeAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getTimes());
        startTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTime.setAdapter(startTimeAdapter);

        // End time spinner filled with values from starttime + 1 to 24
        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Integer> endTimeAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_dropdown_item, MainClass.getInstance().getTimesEnd(Integer.parseInt(startTime.getSelectedItem().toString())));
                endTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                endTime.setAdapter(endTimeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Onclick find movies...
    public void findByCriteria(View v) {
        int start = Integer.parseInt(startTime.getSelectedItem().toString());
        int end = Integer.parseInt(endTime.getSelectedItem().toString());
        int day = Integer.parseInt(dates.getSelectedItem().toString());
        int month = Integer.parseInt(months.getSelectedItem().toString());
        int year = Integer.parseInt(years.getSelectedItem().toString());
        int dateValue = Integer.parseInt(String.format("%02d%02d%04d", day, month, year));
        System.out.println(dateValue + " , " + currentDay);
        String theatre = theatreListView.getSelectedItem().toString();
        if (dateValue < currentDay) {
            infoView.setText("Tarkista päivämäärä. Ei voi olla menneisyydessä.");
        } else {
            try {
                displayMovies.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainClass.getInstance().getMoviesByCriteria(theatre, day, month, year, start, end)));
                infoView.setText("Haku suoritettu!");
            } catch (NullPointerException e) {
                infoView.setText("Hakua vastaavia elokuvia ei löytynyt.");
            }
        }
    }
}