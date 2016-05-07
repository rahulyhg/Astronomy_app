package ericz.astronomyalmanac;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class MoonDetails extends AppCompatActivity {
////CITE THIS STACK OVERFLOW
    ////http://stackoverflow.com/questions/18210700/best-method-to-download-image-from-url-in-android



    String URL = "http://aa.usno.navy.mil/imagery/earth?view=moon&year=2016&month=5&day=4&hour=19&minute=42";
    Bitmap earthViewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_details);
        ImageView earthView = (ImageView)findViewById(R.id.viewOfEarth);
        this.earthViewBitmap = getBitmapFromURL(this.URL);


        if (this.earthViewBitmap == null) {
            earthView.setImageBitmap(earthViewBitmap);
        }
        else{
            Toast toast = Toast.makeText(this.getApplicationContext(),
                    "Check your internet connection",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    public Bitmap getBitmapFromURL(String src) {
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
