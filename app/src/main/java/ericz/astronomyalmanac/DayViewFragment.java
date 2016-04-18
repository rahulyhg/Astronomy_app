package ericz.astronomyalmanac;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DayViewFragment extends Fragment {

    CardView mCardView;

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
        ImageView sunImage = (ImageView)view.findViewById(R.id.sunImage);
        sunImage.bringToFront();
    }
}
