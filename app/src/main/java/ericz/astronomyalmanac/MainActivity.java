package ericz.astronomyalmanac;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
        Fragment[] mFragments =  new Fragment[5];

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //String hexColor = getBackgroundColor();
            //Toast toast = Toast.makeText(this.getApplicationContext(), "hexcolor is: " + hexColor, Toast.LENGTH_SHORT);
            //toast.show();

            //RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);

            //mainLayout.setBackgroundColor(Color.parseColor(hexColor));


            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            mFragments[0] = (MoonViewFragment.newInstance(getApplicationContext()));
            mFragments[1] = (DayViewFragment.newInstance());
            mFragments[2] = (SaturnViewFragment.newInstance());
            mFragments[3] = (VenusViewFragment.newInstance());
            mFragments[4] = (JupiterViewFragment.newInstance());
            transaction.add(R.id.moonView, mFragments[0]);
            transaction.add(R.id.dayView, mFragments[1]);
            transaction.add(R.id.saturnView, mFragments[2]);

            transaction.commit();


        }
        public String getBackgroundColor()
        {
            Time rightNow = new Time();
            switch (rightNow.month){
                case 0:
                    if (rightNow.hour>=16 || rightNow.hour <=7 )
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 1:
                    if (rightNow.hour>=17 || rightNow.hour <=7)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 2:
                    if (rightNow.hour>=18 || rightNow.hour <=6)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 3:
                    if (rightNow.hour>=18 || rightNow.hour <=6 )
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 4:
                    if (rightNow.hour>=20 || rightNow.hour <=6 )
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 5:
                    if (rightNow.hour>=20 || rightNow.hour <=5)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 6:
                    if (rightNow.hour>=20 || rightNow.hour <=5)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 7:
                    if (rightNow.hour>=20 || rightNow.hour <=5)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 8:
                    if (rightNow.hour>=19 || rightNow.hour <=5)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 9:
                    if (rightNow.hour>=18 || rightNow.hour <=5)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 10:
                    if (rightNow.hour>=16 || rightNow.hour <=4)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
                case 11:
                    if (rightNow.hour>=16 || rightNow.hour <=4)
                    {
                        return "#424242";
                    }
                    else
                    {
                        return "#FFFFFF";
                    }
            }




            return null;
        }

    public void startMoonDetails(View view)
    {
        Intent intent = new Intent(this, MoonDetails.class);
        startActivity(intent);
    }
}
