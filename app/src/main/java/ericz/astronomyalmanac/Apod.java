package ericz.astronomyalmanac;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;


public class Apod extends Fragment {
    CardView mCardView;
    private String source;

    public Apod() {
        // Required empty public constructor
    }


    public static Apod newInstance() {
        Apod fragment = new Apod();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apod, container, false);
       // ImageView imageView = (ImageView) getView().findViewById(R.id.apodimageview);




    }
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);
        ImageView imageView = (ImageView)view.findViewById(R.id.apodimageview);
        GetApodURL getApodURL = new GetApodURL();

        try {
            String source = getApodURL.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        GetApod getApod = new GetApod(source);
        Bitmap bitmap = null;
        try {
            bitmap = getApod.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (bitmap != null){
            imageView.setImageBitmap(bitmap);

        }
        else
        {
            Toast toast = Toast.makeText(this.getContext(),
                    "Check your internet connection and try again",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public class GetApod extends AsyncTask<Void, String, Bitmap>
    {

        private String src;

        public GetApod(String src)
        {
            this.src = src;
        }



        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                Bitmap bigBitmap = BitmapFactory.decodeStream(input);
                Bitmap resized = Bitmap.createScaledBitmap(bigBitmap,
                        (int)(bigBitmap.getWidth()*0.7),
                        (int)(bigBitmap.getHeight()*0.7),
                        true);
                Log.v("image was created", "echckec");
                return resized;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
    public class GetApodURL extends AsyncTask<Void, String, String>
    {

        private String jsonInfo;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private String url;
        private String JSONObjectURL = "https://api.nasa.gov/planetary/apod?api_key=NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo";
        @Override
        protected String doInBackground(Void... params) {
            try {
                this.jsonInfo = Jsoup.connect(JSONObjectURL).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getContext(), "check your internet connection", Toast.LENGTH_SHORT);
                toast.show();
            }
            try {
                this.jsonObject = new JSONObject(jsonInfo);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                this.url = jsonObject.getString("url");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }


            Log.v("URL Check", this.url);
            return this.url;
        }
    }




}
