package sp.ham.cat02team118;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    Context context;
    ArrayList<Shop> shopArrayList;

    public ShopAdapter(Context context, ArrayList<Shop> shopArrayList) {
        this.context = context;
        this.shopArrayList = shopArrayList;
    }

    @NonNull
    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.shop_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {

        Shop shop = shopArrayList.get(position);

        holder.name.setText(shop.getName());
        holder.description.setText(shop.getDescription());
        Glide.with(holder.photo.getContext()).load(shop.getPhoto()).into(((ViewHolder) holder).photo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductList.class);
                intent.putExtra("name", shop.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, description;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shopName);
            description = itemView.findViewById(R.id.shopDescription);
            photo = itemView.findViewById(R.id.shopImage);
        }
    }
}
