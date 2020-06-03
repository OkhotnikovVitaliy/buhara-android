package kg.buhara.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kg.buhara.R;
import kg.buhara.data.model.CategoryModel;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.OrderViewHolder> {

    private ArrayList<CategoryModel> products;

    private OnItemListener listener;

    public interface OnItemListener {
        void onItemClick(CategoryModel category, int position);
    }

    public void setOnItemClickListener(OnItemListener listener) {
        this.listener = listener;
    }

    private int selected = -1;


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        OrderViewHolder holder = new OrderViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (listener == null) return;
                listener.onItemClick(products.get(pos), pos);
                selected = pos;
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        CategoryModel category = this.products.get(position);
        holder.title.setText(category.getTitle());
        if (category.getCover() != null) {
            Glide.with(holder.context).load(category.getCover()).into(holder.cover);
        }

        if (selected == position) {
            holder.selected_view.setVisibility(View.VISIBLE);
            holder.blur_view.setVisibility(View.VISIBLE);
        } else {
            holder.selected_view.setVisibility(View.GONE);
            holder.blur_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public void updateItems(ArrayList<CategoryModel> list) {
        products = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView cover;
        CardView card;
        ImageView blur_view;
        View selected_view;

        Context context;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            title = view.findViewById(R.id.title);
            cover = view.findViewById(R.id.cover);
            card = view.findViewById(R.id.card);
            blur_view = view.findViewById(R.id.blur_view);
            selected_view = view.findViewById(R.id.selected_view);
        }
    }

}