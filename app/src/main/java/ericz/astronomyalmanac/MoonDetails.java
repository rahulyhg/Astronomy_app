package ericz.astronomyalmanac;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MoonDetails extends AppCompatActivity {
////CITE THIS STACK OVERFLOW
    ////http://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android

    private TextView labelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_details);
        getMoonPicture getMoonPicture = new getMoonPicture();

        try {
            getMoonPicture.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        GetMoonInfo getMoonInfo = new GetMoonInfo();
        getMoonInfo.execute();

        this.labelView = (TextView)findViewById(R.id.labelmoontext);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf");
        this.labelView.setTypeface(typeface);


    }





    public class getMoonPicture extends AsyncTask<Void, String, Bitmap>
    {



        private ImageView earthView;

        private Bitmap bitmap;
    String src = "http://api.usno.navy.mil/imagery/moon.png?&date="+
            (Calendar.getInstance().get(Calendar.MONTH) + 1)
            + "/"
            + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            + "/"
            + Calendar.getInstance().get(Calendar.YEAR)
            +"&time="
            +Calendar.getInstance().HOUR_OF_DAY
            +":"
            +Calendar.getInstance().MINUTE;
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                this.bitmap =  BitmapFactory.decodeStream(input);
                return this.bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPreExecute()
        {
            this.earthView = (ImageView) findViewById(R.id.apodimageview);


        }
        protected void onPostExecute(Bitmap result)
        {
            this.earthView.setImageBitmap(this.bitmap);

        }




    }


    public class GetMoonInfo extends AsyncTask<Void, String, String[]>
    {
        private String[] finalData = new String[3];
        private String moonInfo;
        private JSONArray moonArray;
        private JSONObject moonObject;

        private TextView moonRise;
        private TextView moonSet;
        private ImageView moonImageView;
        private TextView moonPhaseText;

        private String moonPhase;
        private boolean gotAltPhase;


        protected void onPreExecute() {
            this.moonRise = (TextView)findViewById(R.id.moonRiseDetail);
            this.moonSet = (TextView)findViewById(R.id.MoonSetDetail);
            this.moonImageView = (ImageView)findViewById(R.id.moonPhaseDetailImage);
            this.moonPhaseText = (TextView)findViewById(R.id.moonPhaseDetailText);
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


        protected void onPostExecute(String[] result) {

            this.moonRise.setText(finalData[0]);
            this.moonSet.setText(finalData[1]);
            this.moonPhaseText.setText(finalData[2]);

            try{
                Log.v("tagy tag", "Ran this section of Code");

                if(this.finalData[2].equals("New Moon"))
                    this.moonImageView.setImageResource(R.drawable.new_moon);

                if(this.finalData[2].equals("Full Moon"))
                    this.moonImageView.setImageResource(R.drawable.full_moon);

                if(this.finalData[2].equals( "Waxing Crescent"))
                    this.moonImageView.setImageResource(R.drawable.waxing_crescent);

                if(this.finalData[2].equals("Waning Crescent"))
                    this.moonImageView.setImageResource(R.drawable.waning_crescent);

                if(this.finalData[2].equals("First Quarter"))
                    this.moonImageView.setImageResource(R.drawable.first_quarter);

                if(this.finalData[2].equals("Last Quarter"))
                    this.moonImageView.setImageResource(R.drawable.third_quarter);

                if(this.finalData[2].equals("Waxing Gibbous"))
                    this.moonImageView.setImageResource(R.drawable.waxing_gibbous);

                if(this.finalData[2].equals("Waning Gibbous"))
                    this.moonImageView.setImageResource(R.drawable.waning_gibbous);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }


}
