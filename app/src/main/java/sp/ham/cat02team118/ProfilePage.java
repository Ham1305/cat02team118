package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String UID;
    private TextView UserName, FirstName, LastName, PhoneNum, Email, Address;
    private Button EditProfileInfo;
    private CircleImageView UserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();

        UserName = findViewById(R.id.username);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        PhoneNum = findViewById(R.id.PhoneNo);
        Email = findViewById(R.id.Email);
        Address = findViewById(R.id.Address);
        EditProfileInfo = findViewById(R.id.editButton);
        UserImage = findViewById(R.id.userImage);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        loadInfomation(UID);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(UserImage);
            }
        }

        EditProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(ProfilePage.this, EditProfile.class);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.profilepage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profilepage:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), Explore.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.activity:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), NewShop.class));
                        overridePendingTransition(0, 0);
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
                        if (documentSnapshot.exists()) {
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
                        } else {
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

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }
}

