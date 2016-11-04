package ericz.astronomyalmanac;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ApodDetails extends AppCompatActivity {
    private String title;
    private String hdURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod_details);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.myFAB);

        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            Log.v("tag tagy", "actionbar was created");
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Astronomy Picture");
        actionBar.setHomeButtonEnabled(true);


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


        this.title = getAPODDetails.getTitle();
        TextView titleText = (TextView)findViewById(R.id.APODTitleText);
        titleText.setTypeface(typeface);
        GetAPOD getAPOD = new GetAPOD(this.hdURL);
        getAPOD.execute();



    }
    public void shareImage(View view)
    {
        String shareBody = this.hdURL;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));

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
                this.hdURL = this.jsonObject.getString("url");
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
        public String getTitle()
        {
            return this.title;
        }



    }

    public class GetAPOD extends AsyncTask<String, Void, Bitmap>
    {
        private Bitmap bitmap;
        private Bitmap resized;
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
    class DownloadImageAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private String hdURLString;
        private String titleString;
        public DownloadImageAsyncTask(String title, String hdURL)
        {
            this.hdURLString = hdURL;
            this.titleString = title + ".png";
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            // TODO Auto-generated method stub
            downloadImages(this.titleString);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

        }

    }
    private void downloadImages(String title)
    {

        URL imageUrl; //your URL from which image to be downloaded
        String domain;
        try
        {
            imageUrl = new URL(ApodDetails.this.hdURL);

            HttpURLConnection urlConnection;
            try
            {
                urlConnection = (HttpURLConnection) imageUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                String path = getApplication().getApplicationContext().getExternalCacheDir()
                        + File.separator
                        + getApplication().getApplicationContext().getResources().getString(R.string.app_name);
                File f = new File(path);
                if (!f.exists())
                {
                    f.mkdirs();
                }
                File file = new File(f, title+".png");
                if (!file.exists())
                {
                    file.createNewFile();
                }
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int bufferLength = 0; // used to store a temporary size of the
                // buffer
                while ((bufferLength = inputStream.read(buffer)) > 0)
                {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();


    }

}

}

