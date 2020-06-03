package kg.buhara.view.cart;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

import kg.buhara.R;
import kg.buhara.data.model.DishModel;
import kg.buhara.data.model.RestaurantModel;
import kg.buhara.data.model.cart.CartModel;
import kg.buhara.view.create_order.CreateOrderActivity;
import kg.buhara.view.menu.MenuActivity;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.OrderViewHolder> {

    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;
    RecyclerView recyclerView;
    private ArrayList<CartModel> cartModels;
    private RestaurantModel restaurant;
    private OnItemListener listener;

    public interface OnItemListener {
        void onItemClick(CartModel category, int position);

        void onItemCartDishFavoriteClick(DishModel dishModel);

        void onItemCartDishUpdateClick(DishModel dishModel);

        void onItemCartDishDeleteClick(DishModel dishModel);
    }

    public void setOnItemClickListener(OnItemListener listener) {
        this.listener = listener;
    }

    public CartAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void removeItem(int position) {
        cartModels.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_cart, parent, false);
        OrderViewHolder holder = new OrderViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CartModel cart = this.cartModels.get(position);
        holder.title.setText(cart.getRestaurant().getTitle());
        holder.address.setText(cart.getRestaurant().getAddress());
        holder.delivery_info.setText(cart.getRestaurant().getDelivery_info());
        holder.total.setText(cart.getCart_total() + " сом");

        boolean isSelected = position == selectedItem;
        holder.expandIcon.setSelected(isSelected);
//        holder.expandableLayout.setExpanded(isSelected, true);
        if (isSelected) {
            holder.expandIcon.setImageResource(R.drawable.ic_down);
        } else {
            holder.expandIcon.setImageResource(R.drawable.ic_next);
        }

        if (Objects.requireNonNull(cart.getRestaurant().getDelivery_info()).isEmpty()) {
            holder.delivery_message.setVisibility(View.GONE);
        } else {
            holder.delivery_message.setVisibility(View.VISIBLE);
        }

        //Dishes
        holder.adapter.swapData(cart.getCart_items());

        holder.btnOrder.setOnClickListener(view -> {
            Intent intent = new Intent(holder.context, CreateOrderActivity.class);
            intent.putExtra("id", cart.getId());
            intent.putExtra("cart", cart);
            intent.putExtra("elsom", cart.getRestaurant().getElsom());
            holder.context.startActivity(intent);
        });

        holder.btnShopOpen.setOnClickListener(view -> {
            Intent intent = new Intent(holder.context, MenuActivity.class);
            intent.putExtra("id", cart.getRestaurant().getId());
            intent.putExtra("logo", cart.getRestaurant().getLogo());
            holder.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartModels == null ? 0 : cartModels.size();
    }

    public void swapData(ArrayList<CartModel> list) {
        cartModels = list;
//        this.restaurant = restaurant;
        notifyDataSetChanged();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder implements ExpandableLayout.OnExpansionUpdateListener, View.OnClickListener, CartDishItemAdapter.OnItemListener {

        ImageView expandIcon;
//        ExpandableLayout expandableLayout;
        TextView title;
        TextView address;

        TextView subtotal;
        TextView total;
        TextView discount;
        TextView delivery_message;
        TextView delivery_info;
        RecyclerView recyclerViewDish;
        Button btnOrder;
        Button btnShopOpen;

        Context context;
        CartDishItemAdapter adapter;
        LayoutInflater mInflater;

        int currentPage;

        OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            title = view.findViewById(R.id.name_store);
            address = view.findViewById(R.id.address_store);
            expandIcon = view.findViewById(R.id.expand_icon);
            total = view.findViewById(R.id.total);
            subtotal = view.findViewById(R.id.subtotal);
            discount = view.findViewById(R.id.discount);
//            expandableLayout = view.findViewById(R.id.expandable_layout);
            recyclerViewDish = view.findViewById(R.id.recyclerViewDishes);
            btnOrder = view.findViewById(R.id.btnOrder);
            btnShopOpen = view.findViewById(R.id.btnShopOpen);
            delivery_message = view.findViewById(R.id.delivery_message);
            delivery_info = view.findViewById(R.id.delivery_info);
//            expandableLayout.setInterpolator(new OvershootInterpolator());
//            expandableLayout.setOnExpansionUpdateListener(this);
            itemView.setOnClickListener(this);
            mInflater = LayoutInflater.from(context);

            adapter = new CartDishItemAdapter(this);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            recyclerViewDish.setLayoutManager(llm);
            recyclerViewDish.setAdapter(adapter);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            Log.d("ExpandableLayout", "State: " + state);
            if (state == ExpandableLayout.State.EXPANDING) {
                try {
                    recyclerView.smoothScrollToPosition(getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onClick(View v) {
//            OrderViewHolder holder = (OrderViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
//
//            if (holder != null) {
//                holder.expandIcon.setSelected(false);
//                expandIcon.setImageResource(R.drawable.ic_next);
//                holder.expandableLayout.collapse();
//            }
//
//            int position = getAdapterPosition();
//            if (position == selectedItem) {
//                selectedItem = UNSELECTED;
//                expandIcon.setImageResource(R.drawable.ic_next);
//            } else {
//                expandIcon.setSelected(true);
//                expandIcon.setImageResource(R.drawable.ic_down);
//                expandableLayout.expand();
//                selectedItem = position;
//            }
//            notifyDataSetChanged();
        }

        @Override
        public void onItemClick(@Nullable DishModel data, @NotNull ImageView card) {

        }

        @Override
        public void onItemFavoriteClick(@Nullable DishModel data) {
            Log.e("FAV", "MS");
            listener.onItemCartDishFavoriteClick(data);
        }

        @Override
        public void onItemCartUpdateClick(@Nullable DishModel data) {
            listener.onItemCartDishUpdateClick(data);
        }

        @Override
        public void onItemCartDeleteClick(@Nullable DishModel data) {
            listener.onItemCartDishDeleteClick(data);
        }
    }

}
