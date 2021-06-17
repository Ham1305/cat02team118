package sp.ham.cat02team118;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);

        getSupportActionBar().setTitle("List of Products");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        productArrayList = new ArrayList<Product>();
        productAdapter = new ProductAdapter(ProductList.this, productArrayList);

        recyclerView.setAdapter(productAdapter);

        EventChangeListener();

        BottomNavigationView bottomNavigationView = findViewById(R.id.rewardsNavViewBar);

        bottomNavigationView.setSelectedItemId(R.id.explore);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.explore:
                        return true;


                    case R.id.profilepage:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
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

    private void EventChangeListener() {
        String shopname = getIntent().getStringExtra("name");
        db.collection("shops").document(shopname).collection("listings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        productArrayList.add(dc.getDocument().toObject(Product.class));
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

