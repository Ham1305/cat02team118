package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText firstname,lastname,username,password,address,email,phone;
    private Button regconfirm;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private double uLat=0,uLon=0;
    private String country = "SG";
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Registration Page");

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.usernamereg);
        email = findViewById(R.id.emailreg);
        password = findViewById(R.id.passreg);
        address = findViewById(R.id.enter_address);
        regconfirm = findViewById(R.id.regconfirm);
        phone = findViewById(R.id.phonereg);
        EditText confirm = findViewById(R.id.confirmpass);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Places.initialize(getApplicationContext(),"AIzaSyATnQKwhcuyoLSZpDTIMnESETadwd0pyYU");

        address.setFocusable(false);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize place filed list
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);
                //Create intent
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                )
                        .setCountry(country)
                        .build(Register.this);
                //Start Activity result
                startActivityForResult(intent, 100);
            }
        });

        regconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passStr = password.getText().toString();
                String confirmStr = confirm.getText().toString();
                String firstnameStr = firstname.getText().toString();
                String lastnameStr = lastname.getText().toString();
                String addressStr = address.getText().toString();
                String usernameStr = username.getText().toString();
                String phoneStr = phone.getText().toString();

                String uLatStr = String.valueOf(uLat);
                String uLonStr = String.valueOf(uLon);
                //Toast.makeText(getApplicationContext(), uLatStr + "  ,  " + uLonStr,Toast.LENGTH_SHORT).show();

                if(TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passStr)){
                    Toast.makeText(Register.this," Please enter blank fields! ", Toast.LENGTH_LONG).show();
                }
                else if (!passStr.equals(confirmStr)){
                    Toast.makeText(Register.this," Passwords do not match! ", Toast.LENGTH_LONG).show();
                }
                else{
                    registerUser(emailStr , passStr,firstnameStr,lastnameStr,addressStr,usernameStr,phoneStr,uLatStr,uLonStr);//wip

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //When Success
            //INitialise place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //check condition
                //Set adress on edittext
                address.setText(place.getAddress());
                //Get latitude and longitude
                String sSource = String.valueOf(place.getLatLng());
                sSource = sSource.replaceAll("lat/lng: ", "");
                sSource = sSource.replace("(", "");
                sSource = sSource.replace(")", "");
                String[] split = sSource.split(",");
                uLat = Double.parseDouble(split[0]);
                uLon = Double.parseDouble(split[1]);
                String uLatStr = String.valueOf(uLat);
                String uLonStr = String.valueOf(uLon);

                Toast.makeText(getApplicationContext(), uLatStr + "  ,  " + uLonStr,Toast.LENGTH_SHORT).show();


        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(), status.getStatusMessage()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(String email, String password, String firstname, String lastname, String address, String username, String phoneno, String mLatStr, String mLonStr){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = firestore.collection("users").document(UserID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("email",email);
                    user.put("firstname",firstname);
                    user.put("lastname",lastname);
                    user.put("username",username);
                    user.put("phoneno",phoneno);
                    user.put("address",address);
                    user.put("uLat",mLatStr);
                    user.put("uLon",mLonStr);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: user Profile is created for "+ UserID);
                        }
                    });
                    Toast.makeText(Register.this, "Register successful!", Toast.LENGTH_SHORT).show();
                    finish();
                } else  {
                    Toast.makeText(Register.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}