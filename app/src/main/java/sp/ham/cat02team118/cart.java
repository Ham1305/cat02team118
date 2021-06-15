package sp.ham.cat02team118;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView cartrecyclerView = findViewById(R.id.cartrecyclerView);
        cartrecyclerView.setHasFixedSize(true);
        cartrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CartData[] cartData = new CartData[] {
                //new CartData()
        };

        CartAdapter cartAdapter = new CartAdapter(cartData,cart.this);
        cartrecyclerView.setAdapter(cartAdapter);

    }
}