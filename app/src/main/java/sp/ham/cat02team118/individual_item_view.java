package sp.ham.cat02team118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class individual_item_view extends AppCompatActivity {

    public static final String TAG = "TAG";
    private String prodname, prodprice, proddescription, prodimage;
    private ImageView productImage;
    private Button btn;
    private TextView productname, productprice, productdescription;
    private EditText numofitem;
    /*private FirebaseAuth auth;*/
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_item);

        getSupportActionBar().setTitle("Order");

        productImage = findViewById(R.id.productImage);
        productname = findViewById(R.id.productName);
        productprice = findViewById(R.id.productPrice);
        productdescription = findViewById(R.id.productDescription);
        numofitem = findViewById(R.id.NumofItem);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(individual_item_view.this, "Added to cart", Toast.LENGTH_LONG).show();
            }
        });

       /* auth = FirebaseAuth.getInstance();*/
        firebaseFirestore = FirebaseFirestore.getInstance();
        /*UID = auth.getUid();
        loadShopName(UID);*/
        getValues();
        setTextandImages();
    }

    private void getValues() {
        prodname = getIntent().getStringExtra("prodname");
        prodprice = getIntent().getStringExtra("price");
        proddescription = getIntent().getStringExtra("description");
        prodimage = getIntent().getStringExtra("photo");
    }

    private void setTextandImages() {
        productname.setText(prodname);
        productprice.setText(prodprice);
        productdescription.setText(proddescription);
        Glide.with(this).load(prodimage).into(productImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                DocumentReference phoneRef = firebaseFirestore.collection("shops").document("phoneno");
                phoneRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String mobileNumber = documentSnapshot.getString("phoneno");
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                            intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                            startActivity(intent);
                        } else {
                            Toast.makeText(individual_item_view.this, "Could not contact the seller", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

    /*private void loadShopName(String uid) {
        DocumentReference addRef = firebaseFirestore.collection("shops").document(uid);
        addRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String sname = documentSnapshot.getString("name");
                            getSupportActionBar().setTitle(sname);
                        } else{
                            Toast.makeText(individual_item_view.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(individual_item_view.this, "error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: error");
                    }
                });
    }
}*/