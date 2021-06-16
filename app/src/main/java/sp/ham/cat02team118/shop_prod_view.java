package sp.ham.cat02team118;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class shop_prod_view extends AppCompatActivity {

    private String sname, prodname,prodprice, proddescription, prodimage;
    private ImageView productImage;
    private TextView shopname, productname,productprice, productdescription;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference shopprodref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_prod_view);
        productImage = findViewById(R.id.productImage);
        shopname = findViewById(R.id.ShopName);
        productname = findViewById(R.id.productName);
        productprice = findViewById(R.id.productPrice);
        productdescription = findViewById(R.id.productDescription);

        firebaseFirestore = FirebaseFirestore.getInstance();
        shopprodref = firebaseFirestore.collection("shops");
        //getValues();
        setTextandImages();
    }

    private void getValues() {
        sname = getIntent().getStringExtra("name");
        prodname = getIntent().getStringExtra("product");
        prodprice = getIntent().getStringExtra("price");
        proddescription = getIntent().getStringExtra("productdesc");
        prodimage = getIntent().getStringExtra("image");
    }

    private void setTextandImages() {
        shopname.setText(sname);
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
                CollectionReference mobileNumber = firebaseFirestore.collection("users").document(shopprodref.getId()).collection("phoneno");

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}