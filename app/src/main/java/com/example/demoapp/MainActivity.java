package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demoapp.adapter.ScheduleSlotAdapter;
import com.example.demoapp.models.TimeslotsPojo;
import com.example.demoapp.utilities.AppBarStateChangeListener;
import com.example.demoapp.utilities.NetworkUtils;
import com.example.demoapp.models.SchedulePojo;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", /*Locale.getDefault()*/Locale.ENGLISH);
    private CompactCalendarView compactCalendarView;
    private boolean isExpanded = false;
    private Date selectedDate;
    private boolean doneTimeline=false, doneCalendar=false;
    private String TAG = "MainActivity";
    private ScheduleSlotAdapter adapter;
    private SchedulePojo[] schedulePojos;
    private TimeslotsPojo[] currentTimeslots;
    private TextView errorText;
    private AVLoadingIndicatorView avi;
    private HorizontalCalendar horizontalCalendar;
    private final SimpleDateFormat scheduleDateFormat = new SimpleDateFormat("yyyy-MM-dd", /*Locale.getDefault()*/Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout datePickerButton = findViewById(R.id.date_picker_button);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        avi = findViewById(R.id.avi);
        errorText=findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);
        appBarLayout = findViewById(R.id.app_bar_layout);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        final ImageView arrow = findViewById(R.id.date_picker_arrow);

        selectedDate=new Date();
        avi.show();

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);

        adapter = new ScheduleSlotAdapter(MainActivity.this,currentTimeslots);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, numberOfColumns()));
        recyclerView.setAdapter(adapter);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)   // Number of Dates cells shown on screen (Recommended 5)
                .dayNameFormat("EEE")	  // WeekDay text format
                .dayNumberFormat("dd")    // Date format
                .monthFormat("MMM") 	  // Month format
                .showDayName(true)	      // Show or Hide dayName text
                .showMonthName(true)	  // Show or Hide month text
                .build();

        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);

        horizontalCalendar.setCalendarListener(horizontalCalendarListener);
        compactCalendarView.setListener(compactCalendarViewListener);

        setCurrentDate(selectedDate);

        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rotation = isExpanded ? 0 : 180;
                ViewCompat.animate(arrow).rotation(rotation).start();
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
            }
        });
    }

    private AppBarStateChangeListener appBarStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            final ImageView arrow = findViewById(R.id.date_picker_arrow);
            if (state.name().compareTo("COLLAPSED")==0){
                ViewCompat.animate(arrow).rotation(180).start();
            }else if(state.name().compareTo("EXPANDED")==0){
                ViewCompat.animate(arrow).rotation(0).start();
            }
        }
    };
    private HorizontalCalendarListener horizontalCalendarListener = new HorizontalCalendarListener() {
        @Override
        public void onDateSelected(Date date, int position) {
            Log.d(TAG, "onDateSelected: "+scheduleDateFormat.format(date)+" position: "+position);

            if(!doneTimeline){
                doneTimeline=true;
            }
            if(!doneCalendar){
                compactCalendarView.setCurrentDate(date);
                selectedDate=date;
                updateData();
            }
            if(doneCalendar&&doneTimeline){
                doneCalendar=false;
                doneTimeline=false;
            }
        }
    };
    private CompactCalendarView.CompactCalendarViewListener compactCalendarViewListener = new CompactCalendarView.CompactCalendarViewListener() {
        @Override
        public void onDayClick(Date dateClicked) {
            if(!doneCalendar){
                selectedDate=dateClicked;
                setSubtitle(dateFormat.format(dateClicked));
                doneCalendar=true;
            }
            if(!doneTimeline){
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                horizontalCalendar.selectDate(calendar.getTime(), true);

            }
            updateData();
            if(doneCalendar&&doneTimeline){
                //updateData();
                doneCalendar=false;
                doneTimeline=false;
            }
        }
        @Override
        public void onMonthScroll(Date firstDayOfNewMonth) {
            if(!doneCalendar){
                selectedDate=firstDayOfNewMonth;
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
                doneCalendar=true;
            }
            if(!doneTimeline){
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDayOfNewMonth);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                horizontalCalendar.selectDate(calendar.getTime(), true);
            }
            updateData();
            if(doneCalendar&&doneTimeline){
                //updateData();
                doneCalendar=false;
                doneTimeline=false;
            }
        }
    };

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the item
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

    private void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.title);

        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    private void setSubtitle(String subtitle) {
        TextView datePickerTextView = findViewById(R.id.date_picker_text_view);

        if (datePickerTextView != null) {
            datePickerTextView.setText(subtitle);
        }
    }

    public class NetworkTask extends AsyncTask<Void,Void, SchedulePojo[]>{

        @Override
        protected SchedulePojo[] doInBackground(Void... voids) {
            URL scheduleUrl = NetworkUtils.getSchedulUrl();
            if (isOnline()) {
                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl(scheduleUrl);
                    Log.d("MainActivity", "doInBackground: "+ jsonResponse);
                    return NetworkUtils
                            .getDataFromJson(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }else {
                return null;
            }
        }
        @Override
        protected void onPostExecute(SchedulePojo[] schedulePojos) {
            super.onPostExecute(schedulePojos);
            avi.hide();
            if (schedulePojos==null){
                errorText.setText(getResources().getString(R.string.error_connecting));
                errorText.setVisibility(View.VISIBLE);
                Log.d(TAG, "onPostExecute: NO Values obtained");
                return;
            }
            MainActivity.this.schedulePojos = schedulePojos;
            updateData();
        }

        boolean isOnline() {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
                sock.connect(sockaddr, timeoutMs);
                sock.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void updateData() {
        avi.show();
        currentTimeslots=null;
        for (SchedulePojo schedulePojo : schedulePojos) {
            String date = scheduleDateFormat.format(selectedDate);
            if (date.compareTo(schedulePojo.getFullDate()) == 0) {
                Log.d(TAG, "updateData: Matched, length is "+schedulePojo.getTimeslots().length+" Date is "+date);
                currentTimeslots = schedulePojo.getTimeslots();
                break;
            }
        }
        if (currentTimeslots!=null && currentTimeslots.length!=0) {
            errorText.setVisibility(View.GONE);
            adapter.updateList(currentTimeslots);
        }else{
            errorText.setText(getResources().getString(R.string.no_data));
            errorText.setVisibility(View.VISIBLE);
        }
        avi.hide();
    }
}
