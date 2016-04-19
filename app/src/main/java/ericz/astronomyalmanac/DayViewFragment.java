package ericz.astronomyalmanac;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class DayViewFragment extends Fragment {

    CardView mCardView;
    private String[] finalData;
    public static DayViewFragment newInstance() {
        DayViewFragment fragment = new DayViewFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public DayViewFragment() {
        // singleton
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview1);
        GetSunInfo getSunInfo = new GetSunInfo();
        try {
            this.finalData = getSunInfo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ImageView sunImage = (ImageView)view.findViewById(R.id.sunImage);
        sunImage.bringToFront();
        TextView textView = (TextView)view.findViewById(R.id.sunriseText);
        textView.setText("The sun will rise at " + finalData[1].substring(20, 28) + "and set at "
            + finalData[3].substring(20, 28));
    }

    public class GetSunInfo extends AsyncTask<Void, String, String[]>
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
                    + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1)
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
                this.jsonArray = jsonObject.getJSONArray("sundata");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i<this.jsonArray.length(); i++)
            {
                try {
                    finalData[i] = this.jsonArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return finalData;
        }
    }
}
