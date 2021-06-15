package sp.ham.cat02team118;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    CartData[] CartData;
    Context context;

    public CartAdapter(CartData[] CartData, cart activity) {
        this.CartData = CartData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull  ViewHolder holder, int position) {
        final CartData CartDataList = CartData[position];
        holder.ItemName.setText(CartDataList.getItemName());
        holder.ItemPrice.setText(CartDataList.getItemPrice());
        holder.ItemVaration.setText(CartDataList.getItemVaration());
        holder.NumofItem.setText(CartDataList.getNumofItem());
        holder.ItemImage.setImageResource(CartDataList.getItemImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent shop = new Intent(view.getContext(), Shop.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CartData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ItemImage;
        TextView ItemPrice, ItemName, ItemVaration, NumofItem;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ItemImage = itemView.findViewById(R.id.ItemImage);
            ItemPrice = itemView.findViewById(R.id.ItemPrice);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemVaration = itemView.findViewById(R.id.ItemVar);
            NumofItem = itemView.findViewById(R.id.NumOfItem);
        }
    }
}
