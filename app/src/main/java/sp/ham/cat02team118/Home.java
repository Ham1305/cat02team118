package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private Animation bottomhalf;
    //private LinearLayout LL1;
    private ScrollView homescroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomhalf = AnimationUtils.loadAnimation(this,R.anim.bottom_half);
        //LL1 = findViewById(R.id.linearLayout1);
        homescroller = findViewById(R.id.homescroller);
        //LL1.setAnimation(bottomfast);
        homescroller.setAnimation(bottomhalf);

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), Explore.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.activity:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}