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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MoonDetails extends AppCompatActivity {
////CITE THIS STACK OVERFLOW
    ////http://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_details);
        ImageView earthView = (ImageView) findViewById(R.id.viewOfEarth);
        getMoonPicture getMoonPicture = new getMoonPicture();
        Bitmap bitmap = null;
        try {
            bitmap = getMoonPicture.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        earthView.setImageBitmap(bitmap);

    }





    public class getMoonPicture extends AsyncTask<Void, String, Bitmap>

    {
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
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
