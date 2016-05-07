package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

//cite http://stackoverflow.com/questions/
/* 31970751/java-lang-nullpointerexception-while-instantiating-intent-service-in-android*/
public class MoonViewFragment extends Fragment {
    Typeface font;
    View.OnClickListener onClickListener;
    CardView mCardView;
    Button viewButton;
    private String moonPhase;
    String moonRiseTime;
    String moonSetTime;
    private String[] dataArray;
    public static MoonViewFragment newInstance()
    {
        MoonViewFragment fragment = new MoonViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
    public MoonViewFragment()
    {
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
        this.viewButton.setOnClickListener((View.OnClickListener) this.getView());
        TextView moonRiseText = (TextView)view.findViewById(R.id.moonRiseText);

        GetMoonInfo getMoonInfo = new GetMoonInfo();
        try
        {
            dataArray = getMoonInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ImageView moonImageView = (ImageView)view.findViewById(R.id.moonImageView);
        moonImageView.bringToFront();

        TextView moonLabelText = (TextView)view.findViewById(R.id.moonLabelText);

        font = Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf");
        moonLabelText.bringToFront();
        moonLabelText.setTypeface(font);
        this.moonPhase = this.dataArray[2];
        TextView moonPhaseText = (TextView)view.findViewById(R.id.moonPhaseText);

        this.moonRiseTime = dataArray[1].substring(20, dataArray[1].indexOf("DT")-2);
        this.moonSetTime = dataArray[0].substring(20, dataArray[1].indexOf("DT")-2);

        Toast toast = Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_LONG);


        if (this.moonRiseTime == null)
            toast.show();

        if(this.moonRiseTime.contains("a.m") && this.moonSetTime.contains("a.m"))
            moonRiseText.setText("The moon will rise at " + moonRiseTime
                    + ", and set at " + this.moonSetTime+ " tomorrow.");
        if(this.moonRiseTime.contains("a.m") && this.moonSetTime.contains("pm"))
            moonRiseText.setText("The moon will rise at " + moonRiseTime
                    + ", and set at" + this.moonSetTime);
        if(this.moonRiseTime.contains("p.m") && this.moonSetTime.contains("p.m"))
            moonRiseText.setText("The moon will rise at " + moonRiseTime
                    + ", and set at" + this.moonSetTime+ " tomorrow");





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

        private String[] finalData = new String[3];
        @Override
        protected String[] doInBackground(Void... params) {

            String url = "http://api.usno.navy.mil/rstt/oneday?date=" +
                    (Calendar.getInstance().get(Calendar.MONTH) + 1)
                    + "/"
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
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
            for (int i = 0; i<2; i++)
            {
                try {
                    finalData[i] = this.jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                this.finalData[2] = jsonObject.getString("curphase");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (this.finalData[2] == null){
                try {
                    this.finalData[2] = jsonObject.getString("closestphase").substring(9);
                    int moonIndex = this.finalData[2].indexOf(",") - 1;
                    this.finalData[2] = this.finalData[2].substring(0, moonIndex);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return finalData;
        }





    }

}