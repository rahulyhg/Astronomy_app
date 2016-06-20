package ericz.astronomyalmanac;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class ApodDetails extends Activity {
    private String[] dataArray;
    private Bitmap bitmap;
    private String titleText;
    private String descriptionText;
    private String hdURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod_details);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.myFAB);

        floatingActionButton.getBackground().setColorFilter(Color.parseColor("#FFEB3B"),
                PorterDuff.Mode.DARKEN);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf");

        GetAPODDetails getAPODDetails = new GetAPODDetails();
        try {
            this.hdURL = getAPODDetails.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        TextView titleText = (TextView)findViewById(R.id.APODTitleText);
        titleText.setTypeface(typeface);
        GetAPOD getAPOD = new GetAPOD(this.hdURL);
        getAPOD.execute();







    }





    public class GetAPODDetails extends AsyncTask<Void, String, String>

    {
        private String[] finalData;
        private String hdURL;
        private String description;
        private String jsonInfo;
        private JSONObject jsonObject;
        private String title;
        String src = "https://api.nasa.gov/planetary/apod?api_key=NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo";

        @Override
        protected String doInBackground(Void... params) {




            try
            {
                this.jsonInfo = Jsoup.connect(src).ignoreContentType(true).execute().body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                this.jsonObject = new JSONObject(jsonInfo);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            try
            {
                this.hdURL = this.jsonObject.getString("hdurl");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                this.description = this.jsonObject.getString("explanation");
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            try {
                this.title = this.jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return this.hdURL;
        }
        public void onPostExecute(String string)
        {

            TextView titleText = (TextView)findViewById(R.id.APODTitleText);
            titleText.setText(this.title);


            TextView descriptionText = (TextView)findViewById(R.id.descriptionTextView);
            descriptionText.setText(this.description);


        }



    }

    public class GetAPOD extends AsyncTask<String, Void, Bitmap>
    {
        private Bitmap bitmap;
        private String source;
        public GetAPOD(String src)
        {
            this.source = src;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                java.net.URL url = new java.net.URL(this.source);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                this.bitmap =  BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            ImageView imageView = (ImageView)findViewById(R.id.apodimageview);
            imageView.setImageBitmap(this.bitmap);


        }
    }

}
