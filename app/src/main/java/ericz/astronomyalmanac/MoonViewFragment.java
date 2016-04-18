package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class MoonViewFragment extends Fragment {
    Typeface typeface;
    CardView mCardView;
    private String moonPhase;
    private String[] dataArray;
    public static MoonViewFragment newInstance(Context context)
    {
        Context c = context;
        MoonViewFragment fragment = new MoonViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public MoonViewFragment() {
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
        return inflater.inflate(R.layout.fragment_card_view, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);

        TextView moonRiseText = (TextView)view.findViewById(R.id.moonRiseText);


        GetMoonInfo getMoonInfo = new GetMoonInfo();

        ImageView moonImageView = (ImageView)view.findViewById(R.id.moonImageView);
        moonImageView.bringToFront();


        try {
            this.dataArray = getMoonInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TextView moonLabelText = (TextView)view.findViewById(R.id.moonLabelText);

        moonRiseText.setText("The moon will rise at " + this.dataArray[1] + "and set at "
                + this.dataArray[2]);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf");
        moonLabelText.bringToFront();
        moonLabelText.setTypeface(font);

        TextView moonPhaseText = (TextView)view.findViewById(R.id.moonPhaseText);
        //I tried to use a switch case statement here but it always set the wrong moon phase.. not sure why
        try{
            this.moonPhase = this.dataArray[1];
            moonPhaseText.setText("Tonight's phase is a " + this.moonPhase);

            if(this.moonPhase.equals("New Moon"))
                moonImageView.setImageResource(R.drawable.new_moon);

            if(this.moonPhase.equals("Full Moon"))
                moonImageView.setImageResource(R.drawable.full_moon);

            if(this.moonPhase.equals( "Waxing Crescent"))
                moonImageView.setImageResource(R.drawable.waxing_crescent);

            if(this.moonPhase.equals("Waning Crescent"))
                moonImageView.setImageResource(R.drawable.waning_crescent);

            if(this.moonPhase.equals("First Quarter"))
                moonImageView.setImageResource(R.drawable.first_quarter);

            if(this.moonPhase.equals("Third Quarter"))
                moonImageView.setImageResource(R.drawable.third_quarter);

            if(this.moonPhase.equals("Waxing Gibbous"))
                moonImageView.setImageResource(R.drawable.waxing_gibbous);

            if(this.moonPhase.equals("Waning Gibbous"))
                moonImageView.setImageResource(R.drawable.waning_gibbous);




        }
        catch (Exception e){

        }

        Log.v("tag", this.moonPhase);
    }







    public class GetMoonInfo extends AsyncTask<Void, String, String[]>
    {
        private String phaseJsonString;
        private JSONObject phaseJson;
        private JSONObject moonRiseObject;
        private JSONObject moonSetObject;
        private JSONObject sunRiseObject;
        private JSONObject sunSetObject;
        private String finalPhase;
        private String sunRise;
        private String sunSet;
        private String moonRise;
        private String moonSet;
        private JSONArray jsonMoonArray;
        private JSONArray jsonSunArray;
        String[] returnedArray = new String[5];
        @Override
        protected String[] doInBackground(Void... params) {
            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)
                    + "/" + Calendar.getInstance().get(Calendar.YEAR)
                    + "&loc=Chicago,%20IL";
            try
            {
                this.phaseJsonString = Jsoup.connect(url).ignoreContentType(true).execute().body();
            }
            catch (IOException e)
            {
            }
            try
            {
                this.phaseJson = new JSONObject(phaseJsonString);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try
            {
                this.finalPhase = this.phaseJson.get("curphase").toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try
            {
                this.jsonMoonArray = new JSONArray(phaseJson.getJSONArray("moondata"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.moonRiseObject = jsonMoonArray.getJSONObject(0);
                this.moonRise = moonRiseObject.getString("time").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.moonSetObject = jsonMoonArray.getJSONObject(2);
                this.moonSet = moonSetObject.getString("time").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.jsonSunArray = new JSONArray(phaseJson.getJSONArray("sundata"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.sunRiseObject = this.jsonSunArray.getJSONObject(1);
                this.sunRise = sunRiseObject.getString("time").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.sunSetObject = this.jsonSunArray.getJSONObject(3);
                this.sunSet = this.sunSetObject.getString("time").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            this.returnedArray[0] = this.finalPhase;
            this.returnedArray[1] = this.moonRise;
            this.returnedArray[2] = this.moonSet;
            this.returnedArray[3] = this.sunRise;
            this.returnedArray[4] = this.sunSet;
            return this.returnedArray;
        }




    }

}