package ericz.astronomyalmanac;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

public class MoonDetails extends AppCompatActivity {
////CITE THIS STACK OVERFLOW
    ////http://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android


    String URL = "http://aa.usno.navy.mil/imagery/earth?view=moon&year=2016&month=5&day=4&hour=19&minute=42";
    Bitmap earthViewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_details);
        ImageView earthView = (ImageView) findViewById(R.id.viewOfEarth);
        getMoonPicture getMoonPicture = new getMoonPicture();
        Bitmap bitmap;
        try {
            bitmap = getMoonPicture.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        try {
            earthView.setImageBitmap(earthViewBitmap);

        } catch (Exception e) {
            Toast toast = Toast.makeText(this.getApplicationContext(),
                    "Please check your internet connection", Toast.LENGTH_LONG);
            toast.show();
        }

    }


    public class getMoonPicture extends AsyncTask<Void, String, Bitmap>


    {

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
