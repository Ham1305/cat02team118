package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String UID;
    private TextView UserName, FirstName, LastName, PhoneNum, Email, Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();

        UserName = findViewById(R.id.username);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        PhoneNum = findViewById(R.id.PhoneNum);
        Email = findViewById(R.id.Email);
        Address = findViewById(R.id.Address);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        loadInfomation(UID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.profilepage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profilepage:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), ShopList.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.business:
                        startActivity(new Intent(getApplicationContext(), ProfilepageOwners.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadInfomation(String uid) {
        DocumentReference addRef = firestore.collection("users").document(uid);
        addRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String address = documentSnapshot.getString("address");
                            String email = documentSnapshot.getString("email");
                            String firstname = documentSnapshot.getString("firstname");
                            String lastname = documentSnapshot.getString("lastname");
                            String username = documentSnapshot.getString("username");
                            String phoneno = documentSnapshot.getString("phoneno");
                            UserName.setText(username);
                            FirstName.setText(firstname);
                            LastName.setText(lastname);
                            PhoneNum.setText(phoneno);
                            Email.setText(email);
                            Address.setText(address);
                        } else{
                            Toast.makeText(ProfilePage.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePage.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: error");
                    }
                });
    }
}