package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

//cite http://stackoverflow.com/questions/
/* 31970751/java-lang-nullpointerexception-while-instantiating-intent-service-in-android*/
public class MoonViewFragment extends Fragment {
    Typeface font;
    View.OnClickListener onClickListener;
    CardView mCardView;
    Button viewButton;
    private String moonPhase;
    Context context;
    String moonRiseTime;
    String moonSetTime;
    private String[] dataArray;
    public static MoonViewFragment newInstance(String location)
    {
        MoonViewFragment fragment = new MoonViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    public MoonViewFragment()
    {   }

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
        GetMoonInfo getMoonInfo = new GetMoonInfo();

        mCardView = (CardView) view.findViewById(R.id.cardview);

            getMoonInfo.execute();



        this.viewButton = (Button)view.findViewById(R.id.moonDetailsButton);
        this.viewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MoonDetails.class);

                startActivity(intent);
            }
        });

    }


    public class GetMoonInfo extends AsyncTask<Void, String, String[]>
    {
        private String[] finalData = new String[3];
        private String moonInfo;
        private JSONArray moonArray;
        private JSONObject moonObject;

        private TextView moonPhaseText;
        private TextView moonTextView;
        private ImageView moonImageView;
        private TextView moonText;

        private String moonPhase;
        private boolean gotAltPhase;


        protected void onPreExecute()
        {
            this.moonTextView = (TextView)mCardView.findViewById(R.id.moonRiseText);
            this.moonText = (TextView)mCardView.findViewById(R.id.moonLabelText);
            this.moonImageView  = (ImageView)mCardView.findViewById(R.id.apodimageview);
            this.moonPhaseText = (TextView)mCardView.findViewById(R.id.moonPhaseText);
        }


        @Override
        protected String[] doInBackground(Void... params) {

            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                    + "/" + Calendar.getInstance().get(Calendar.YEAR)
                    + "&loc=Chicago,%20IL";
            try {
                this.moonInfo = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try {
                this.moonObject = new JSONObject(moonInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                this.moonArray = moonObject.getJSONArray("moondata");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            List<String> moonOutput = new ArrayList<>();
            for (int i=0; i<moonArray.length(); i++) {
                try
                {
                    moonOutput.add( moonArray.getString(i) );
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            for (int i=0; i<moonOutput.size(); i++ )
            {
                if (moonOutput.get(i).contains("\"phen\":\"R\""))
                    this.finalData[0] = moonOutput.get(i).substring(20, 29);
                if (moonOutput.get(i).contains("\"phen\":\"S\""))
                    this.finalData[1] = moonOutput.get(i).substring(20, 29);
                else
                    Log.v("Find", "Nothing found in array index");
            }

            try
            {
                this.finalData[2] = moonObject.getString("curphase");
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }
            if (this.finalData[2] == null)
            {

                try {
                    this.moonPhase = moonObject.getJSONObject("closestphase").getString("phase");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            return finalData;
        }


        protected void onPostExecute(String[] result)
        {
            this.moonText.bringToFront();
            this.moonTextView.setText("the moon will rise at "+ finalData[0] +
                    " and set at " + finalData[1]);
            this.moonPhaseText.setText("Tonights phase is a " + this.finalData[2]);
            try{


                if(this.finalData[2].equals("New Moon"))
                    moonImageView.setImageResource(R.drawable.new_moon);

                if(this.finalData[2].equals("Full Moon"))
                    moonImageView.setImageResource(R.drawable.full_moon);

                if(this.finalData[2].equals( "Waxing Crescent"))
                    moonImageView.setImageResource(R.drawable.waxing_crescent);

                if(this.finalData[2].equals("Waning Crescent"))
                    moonImageView.setImageResource(R.drawable.waning_crescent);

                if(this.finalData[2].equals("First Quarter"))
                    moonImageView.setImageResource(R.drawable.first_quarter);

                if(this.finalData[2].equals("Last Quarter"))
                    moonImageView.setImageResource(R.drawable.third_quarter);

                if(this.finalData[2].equals("Waxing Gibbous"))
                    moonImageView.setImageResource(R.drawable.waxing_gibbous);

                if(this.finalData[2].equals("Waning Gibbous"))
                    moonImageView.setImageResource(R.drawable.waning_gibbous);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }

}