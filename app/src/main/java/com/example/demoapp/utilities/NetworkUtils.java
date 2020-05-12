package com.example.demoapp.utilities;

import android.net.Uri;

import com.example.demoapp.models.SchedulePojo;
import com.example.demoapp.models.TimeslotsPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String NO = "1";
    final static String WEBURL= "projects.kronostechno.com";
    final static String TYPE = "service-app";
    final static String SCHEDULE = "schedule-dates";

    public static URL getSchedulUrl(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(WEBURL)
                .appendPath(TYPE)
                .appendPath("api")
                .appendPath(SCHEDULE)
                .appendPath(NO);

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    final static String DATA = "data";
    public static SchedulePojo[] getDataFromJson(String json) throws JSONException {
        JSONArray resultArray = new JSONObject(json).getJSONArray(DATA);

        SchedulePojo[] parsedSchedule = new SchedulePojo[resultArray.length()];

        for (int i=0;i<resultArray.length();i++){
            JSONObject data = resultArray.getJSONObject(i);

            String month = data.getString("month");;
            String date = data.getString("date");
            String day= data.getString("day");
            String fulldate = data.getString("fulldate");

            JSONArray timeslotsArray = data.getJSONArray("timeslots");
            TimeslotsPojo[] timeslots = new TimeslotsPojo[timeslotsArray.length()];
            for (int j=0;j<timeslotsArray.length();j++){
                JSONObject timeslotData = timeslotsArray.getJSONObject(j);
                int id = timeslotData.getInt("tslot_id");
                String name = timeslotData.getString("tslot_name");
                int minTime = timeslotData.getInt("tslot_min_time");
                String deleted = timeslotData.getString("tslot_is_deleted");
                String disabled = timeslotData.getString("is_disabled");
                timeslots[j]=new TimeslotsPojo(id,name,minTime,deleted,disabled);
            }
            parsedSchedule[i] = new SchedulePojo(month,date,day,fulldate,timeslots);
        }
        return parsedSchedule;
    }
}
