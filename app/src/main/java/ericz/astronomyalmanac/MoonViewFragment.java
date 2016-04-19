package ericz.astronomyalmanac;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
    Typeface font;
    CardView mCardView;
    private String moonPhase;
    private String[] dataArray;
    public static MoonViewFragment newInstance()
    {

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
        try {
            dataArray = getMoonInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ImageView moonImageView = (ImageView)view.findViewById(R.id.moonImageView);
        moonImageView.bringToFront();



        TextView moonLabelText = (TextView)view.findViewById(R.id.moonLabelText);

       // moonRiseText.setText("The moon will rise at " + this.dataArray[1] + "and set at "
         //       + this.dataArray[2]);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf");
        moonLabelText.bringToFront();
        moonLabelText.setTypeface(font);
        this.moonPhase = this.dataArray[2];
        TextView moonPhaseText = (TextView)view.findViewById(R.id.moonPhaseText);

        //I tried to use a switch case statement here but it always set the wrong moon phase.. not sure why

        moonRiseText.setText("The moon will rise at " + dataArray[0].substring(20, 28)
                + " and set at " + dataArray[1].substring(20, 28));

        try{

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

    }







    public class GetMoonInfo extends AsyncTask<Void, String, String[]>
    {
        private String jsonInfo;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private String moonPhase;
        private String[] finalData = new String[3];
        @Override
        protected String[] doInBackground(Void... params) {

            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)
                    + "/" + Calendar.getInstance().get(Calendar.YEAR)
                    + "&loc=Chicago,%20IL";
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
                this.jsonArray = jsonObject.getJSONArray("moondata");
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
                this.moonPhase = jsonObject.getString("curphase");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.finalData[2] = moonPhase;
            return finalData;
        }




    }

}