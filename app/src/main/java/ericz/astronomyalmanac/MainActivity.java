package ericz.astronomyalmanac;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
        Fragment[] mFragments =  new Fragment[5];

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
            mFragments[0] = (MoonViewFragment.newInstance());
            mFragments[1] = (DayViewFragment.newInstance());
            mFragments[2] = (SaturnViewFragment.newInstance());
            mFragments[3] = (VenusViewFragment.newInstance());
            mFragments[4] = (JupiterViewFragment.newInstance());
            transaction.add(R.id.moonView, mFragments[0]);
            transaction.add(R.id.dayView, mFragments[1]);
            transaction.add(R.id.saturnView, mFragments[2]);

            transaction.commit();


        }


    public void startMoonDetails(View view)
    {
        Intent intent = new Intent(this, MoonDetails.class);
        startActivity(intent);
    }
}
