package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {

    public static final String TAG = "TAG";
    private Animation bottomhalf;
    //private LinearLayout LL1;
    private ScrollView homescroller;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String UID;
    private TextView homename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomhalf = AnimationUtils.loadAnimation(this, R.anim.bottom_half);
        homename = findViewById(R.id.homename);
        bottomhalf = AnimationUtils.loadAnimation(this,R.anim.bottom_half);
        //LL1 = findViewById(R.id.linearLayout1);
        homescroller = findViewById(R.id.homescroller);
        //LL1.setAnimation(bottomfast);
        homescroller.setAnimation(bottomhalf);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        loadAddress(UID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), ShopList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.activity:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profilepage:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        overridePendingTransition(0,0);
                        return true;

                    /*case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0, 0);
                        startActivity(new Intent(getApplicationContext(), NewShop.class));*/

                    case R.id.business:
                        startActivity(new Intent(getApplicationContext(), ProfilepageOwners.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                //startActivity(new Intent(getApplicationContext(), cart.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadAddress(String uid) {
        DocumentReference addRef = firestore.collection("users").document(uid);
        addRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String address = documentSnapshot.getString("address");
                            String lastname = documentSnapshot.getString("lastname");
                            getSupportActionBar().setTitle(address);
                            homename.setText(lastname);
                        } else{
                            Toast.makeText(Home.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: error");
                    }
                });
    }
}