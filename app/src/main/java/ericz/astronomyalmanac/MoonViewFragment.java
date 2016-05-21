package ericz.astronomyalmanac;

import android.app.Fragment;
import android.content.Context;
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
    Context context;
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
    {    }

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



    /*
    The onViewCreated class specifies what happens as soon as a View (which is like a s
    screen) is created, and a Bundle(Can hold things like where you were scrolling in a
    list) is passed in. IT is used to instantiate and populate TextViews and Buttons.
    It is also used to call a second class GetMoonInfo to grab necessary data to populate
    text and images and buttons.
     */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);
        TextView moonRiseText = (TextView)view.findViewById(R.id.moonRiseText);

        this.viewButton = (Button)view.findViewById(R.id.moonDetailsButton);
        this.viewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MoonDetails.class);
                startActivity(intent);
            }
        });


        //this is the place where getmoon info is created; in the background
        GetMoonInfo getMoonInfo = new GetMoonInfo();


        //this section grabs the data from getmooninfo by executing its body and
        //getting the return value
        //A new class needs to be created for this data because Android does not allow
        //you to do internet data transactions on the main thread; That would make everything
        //slow and frustrating
        try
        {
            dataArray = getMoonInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //creation of the imageview in the center containing the moon
        ImageView moonImageView = (ImageView)view.findViewById(R.id.moonImageView);
        moonImageView.bringToFront();

        TextView moonLabelText = (TextView)view.findViewById(R.id.moonLabelText);

        font = Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf");
        //Brings the moon label in yellow to the front because that cannot be done with XML
        //layout
        moonLabelText.bringToFront();
        //Sets that labels font
        moonLabelText.setTypeface(font);
        //takes some of the data in the array from the GetMoonInfo classes return value
        //(an array) and sets it to a normal string
        this.moonPhase = this.dataArray[3];
        TextView moonPhaseText = (TextView)view.findViewById(R.id.moonPhaseText);

        this.moonRiseTime = dataArray[1].substring(20, dataArray[1].indexOf("DT")-2);
        this.moonSetTime = dataArray[0].substring(20, dataArray[0].indexOf("DT")-2);






            moonRiseText.setText("The moon will rise at " + moonRiseTime
                    + ", and set at " + this.moonSetTime);







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
            Toast toast1 = Toast.makeText(this.getContext(),
                    "Pleaes check your internet connection", Toast.LENGTH_SHORT);
            toast1.show();
        }

    }




    public class GetMoonInfo extends AsyncTask<Void, String, String[]>
    {
        private String jsonInfo;
        private JSONObject jsonObject;
        private JSONArray jsonArray;

        private String[] finalData = new String[5];
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

            try{
                finalData[1] = this.jsonArray.getString(0);
            }
            catch (Exception e){

            }
            try{
                finalData[0] = this.jsonArray.getString(2);
            }
            catch (Exception e){

            }
            try {
                this.finalData[3] = jsonObject.getString("curphase");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (this.finalData[3] == null){
                try {
                    this.finalData[3] = jsonObject.getString("closestphase").substring(9);
                    int moonIndex = this.finalData[3].indexOf(",") - 1;
                    this.finalData[3] = this.finalData[3].substring(1, moonIndex);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {

                this.finalData[4] = jsonObject.getString("nextmoondata");
            }
                catch (JSONException e) {
                e.printStackTrace();
            }


            return finalData;
        }


    }

}