package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ProfilepageOwners extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private String UserID;
    String usershop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage_owners);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setBackground(null);
        bottomNavigation.setSelectedItemId(R.id.business);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        UserID = auth.getCurrentUser().getUid();

        DoesUserHaveShop(UserID);

        //LoadShop();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), Explore.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profilepage:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.business:
                        return true;
                }
                return false;
            }
        });

        ExtendedFloatingActionButton sell_fab = findViewById(R.id.sell_fab);
        sell_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final Dialog dialog = new Dialog(ProfilepageOwners.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();

                //Open camera
                Button btn_camera = dialog.findViewById(R.id.btn_cam);
                btn_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Camera Opened", Toast.LENGTH_SHORT).show();
                    }
                });

                //Open gallery
                Button btn_gallery = dialog.findViewById(R.id.btn_gallery);
                btn_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Gallery Opened", Toast.LENGTH_SHORT).show();
                    }
                });*/

                Intent intent = new Intent(ProfilepageOwners.this, NewListing.class);
                startActivity(intent);

            }

        });
    }

    private void DoesUserHaveShop(String uid) {
        CollectionReference cR = firestore.collection("users");
        cR.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().contains("shop")){
                        Log.d(TAG, "onComplete: document contains shop, proceed to new listing page");
                        cR.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                usershop = documentSnapshot.getString("shop");
                            }
                        });

                    } else{
                        new AlertDialog.Builder(ProfilepageOwners.this)
                                .setTitle("Hold on!")
                                .setMessage("Your shop had not been set up yet, set up in order to upload listings")
                                .setPositiveButton("Set up", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ProfilepageOwners.this, NewShop.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoesUserHaveShop(UserID);
    }
}
