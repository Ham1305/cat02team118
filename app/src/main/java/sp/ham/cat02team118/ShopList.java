package sp.ham.cat02team118;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Shop> shopArrayList;
    ShopAdapter shopAdapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);

        getSupportActionBar().setTitle("Shops near you");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        shopArrayList = new ArrayList<Shop>();
        shopAdapter = new ShopAdapter(ShopList.this, shopArrayList);

        recyclerView.setAdapter(shopAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("shops").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        shopArrayList.add(dc.getDocument().toObject(Shop.class));
                    }
                    shopAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

