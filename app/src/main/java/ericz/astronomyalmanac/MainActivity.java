package ericz.astronomyalmanac;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {
        //fragment array
        Fragment[] mFragments =  new Fragment[5];

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        //A fragment transcation class lets FragmentActivity files (Like MoonViewFragment,
        //DayViewFragment) get moved into views
        //this section adds multiple fragment views into a fragment array
        //the transactions at the bottom add some of the fragments into the main view you see
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
            mFragments[0] = (MoonViewFragment.newInstance());
            mFragments[1] = (DayViewFragment.newInstance());
            mFragments[2] = (Apod.newInstance());

        //some classes are left un implemented becaues I didn't have time to finish them
            transaction.add(R.id.moonView, mFragments[0]);
            transaction.add(R.id.dayView, mFragments[1]);
            transaction.add(R.id.apodView, mFragments[2]);


            transaction.commit();


        }



}
