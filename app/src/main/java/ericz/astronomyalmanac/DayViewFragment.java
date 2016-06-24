package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class DayViewFragment extends Fragment {

    CardView mCardView;
    private String[] finalData;
    public static DayViewFragment newInstance() {
        DayViewFragment fragment = new DayViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public DayViewFragment() {
        // singleton
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview1);
        //Creates a sun info class; this is required because you cannot run network on the main thread
        //so a new class that extends Async Task is used to let it run in the background
        //there is a method called runInBackgroudn that returns a var of your choice to compute
        //network stuff in the background
        //I used a string array to get the needed data from multiple sources
        GetSunInfo getSunInfo = new GetSunInfo();
        try {
            this.finalData = getSunInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        ImageView sunImage = (ImageView)view.findViewById(R.id.sunImage);
        sunImage.bringToFront();

        TextView sunTextView = (TextView)view.findViewById(R.id.sunTextView);
        Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "RobotoSlab-Regular.ttf");



        sunTextView.bringToFront();
        sunTextView.setTypeface(font);
        TextView weatherText = (TextView)view.findViewById(R.id.weatherText);
        weatherText.setText("It is expected to be " + finalData[5].substring(7,
                finalData[5].indexOf("}"))+ " % cloudy today");
        TextView textView = (TextView)view.findViewById(R.id.sunriseText);
        textView.setText("The sun will rise at " + finalData[1].substring(20, 28) + " and set at "
                + finalData[3].substring(20, 28));
    }

    public class GetSunInfo extends AsyncTask<Void, String, String[]>
    {
        private String jsonInfo;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private String[] finalData = new String[6];
        private JSONObject jsonWeather;
        private String weatherInfo;
        @Override
        protected String[] doInBackground(Void... params) {

            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)
                    + "/" + Calendar.getInstance().get(Calendar.YEAR)
                    + "&loc=Chicago,%20IL";
            String weatherUrl =
                    "http://api.openweathermap.org/data/2.5/weather?q=Chicago,IL&appid=103d3c819cda6f8663f847cb05606357";
            try {
                this.jsonInfo = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.jsonObject = new JSONObject(jsonInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.jsonArray = jsonObject.getJSONArray("sundata");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i<this.jsonArray.length(); i++)
            {
                try {
                    finalData[i] = this.jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                this.weatherInfo = Jsoup.connect(weatherUrl).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.jsonWeather = new JSONObject(weatherInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.finalData[5] = jsonWeather.getString("clouds");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return finalData;
        }
    }
}
