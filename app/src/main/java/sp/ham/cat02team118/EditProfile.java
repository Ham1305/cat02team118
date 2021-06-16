package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String UID;
    private EditText Username,Firstname,Lastname, PhoneNum, Email;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Username = findViewById(R.id.editusername);
        Firstname = findViewById(R.id.editfirstname);
        Lastname = findViewById(R.id.editlastname);
        PhoneNum = findViewById(R.id.editphone);
        Email = findViewById(R.id.editemail);
        submit = findViewById(R.id.savechanges);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UID = auth.getCurrentUser().getUid();
        loadInfomation(UID);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = Username.getText().toString();
                String firstStr = Firstname.getText().toString();
                String lastStr = Lastname.getText().toString();
                String phoneStr = PhoneNum.getText().toString();
                String emailStr = Email.getText().toString();

                DocumentReference documentReference = firestore.collection("users").document(UID);
                Map<String ,Object> edit = new HashMap<>();
                edit.put("username",usernameStr);
                edit.put("firstname",firstStr);
                edit.put("lastname",lastStr);
                edit.put("phoneno",phoneStr);
                edit.put("email",emailStr);
                documentReference.update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfile.this,"Update profile Successful!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this,ProfilePage.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });


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
                            String email = documentSnapshot.getString("email");
                            String firstname = documentSnapshot.getString("firstname");
                            String lastname = documentSnapshot.getString("lastname");
                            String username = documentSnapshot.getString("username");
                            String phoneno = documentSnapshot.getString("phoneno");
                            Username.setText(username);
                            Firstname.setText(firstname);
                            Lastname.setText(lastname);
                            PhoneNum.setText(phoneno);
                            Email.setText(email);

                        } else{
                            Toast.makeText(EditProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: error");
                    }
                });
    }
}