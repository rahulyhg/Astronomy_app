package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class DayViewFragment extends Fragment {

    CardView mCardView;
    private String[] finalData;
    public static DayViewFragment newInstance(String location) {
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
        GetSunInfo getSunInfo = new GetSunInfo();
        getSunInfo.execute();

    }

    public class GetSunInfo extends AsyncTask<Void, String, String[]>
    {
        private String[] finalData = new String[6];
        private JSONObject weatherObject;
        private String weatherInfo;
        private JSONArray sunArray;
        private String cloudCoverage;

        private JSONObject sunObject;
        private String sunInfo;

        private TextView sunLabelTextView;
        private TextView sunTextView;
        private ImageView sunImage;
        private TextView weatherText;
        @Override








        protected void onPreExecute()
        {
            this.sunTextView = (TextView)mCardView.findViewById(R.id.sunriseText);
            this.sunImage = (ImageView)mCardView.findViewById(R.id.sunImage);
            this.weatherText = (TextView)mCardView.findViewById(R.id.weatherText);
            this.sunLabelTextView = (TextView)mCardView.findViewById(R.id.sunTextView);

        }


        protected String[] doInBackground(Void... params) {
/*
--------JSON DATA ARRAY KEY ----------
finaldata[0] = SUN RISE TIME
finaldata[1] = SUN SET TIME
finaldata[2] = CLOUD LEVEL
 */
            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)
                    + "/" + Calendar.getInstance().get(Calendar.YEAR)
                    + "&loc=Chicago,%20IL";

            try {
                this.sunInfo = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.sunObject = new JSONObject(sunInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.sunArray = sunObject.getJSONArray("sundata");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> sunOutput = new ArrayList<>();
            for (int i=0; i<sunArray.length(); i++) {
                try
                {
                    sunOutput.add( sunArray.getString(i) );
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            for (int i=0; i<sunOutput.size(); i++ )
            {
                if (sunOutput.get(i).contains("\"phen\":\"R\""))
                    this.finalData[0] = sunOutput.get(i).substring(20, 29);
                if (sunOutput.get(i).contains("\"phen\":\"S\""))
                    this.finalData[1] = sunOutput.get(i).substring(20, 29);
                else
                    Log.v("Find", "Nothing found in array index");
            }



            /*
            CLOUD LEVEL CODE
             */
            String weatherUrl =
                    "http://api.openweathermap.org/data/2.5/weather?q=Chicago,IL&appid=103d3c819cda6f8663f847cb05606357";

            try {
                this.weatherInfo = Jsoup.connect(weatherUrl).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.weatherObject = new JSONObject(weatherInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.cloudCoverage = weatherObject.getString("clouds");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.finalData[2] = cloudCoverage.substring(cloudCoverage.indexOf(":")+1,
                    cloudCoverage.indexOf("}"));
            return finalData;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            this.sunLabelTextView.bringToFront();
            this.sunTextView.setText("The sun will rise at " + finalData[0] + " and set at " + finalData[1]);
            this.weatherText.setText("Today will be " + finalData[2] + "% cloudy");
            this.sunImage.bringToFront();
        }
    }
}
