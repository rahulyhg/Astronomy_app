package ericz.astronomyalmanac;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class LocationFragment extends Fragment {
    CardView mCardView;
    private String source;
    private String location;
    public LocationFragment() {
        // Required empty public constructor
    }


    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = (Button)mCardView.findViewById(R.id.enterLocationButton);
        Button button1 = (Button)mCardView.findViewById(R.id.saveLocationButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditText locationText;
                locationText = (EditText)mCardView.findViewById(R.id.locEditText);
                LocationFragment.this.location = locationText.getText().toString();

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);




    }
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.cardview);





    }

    public String getLocation()
    {
        return this.location;
    }




}
