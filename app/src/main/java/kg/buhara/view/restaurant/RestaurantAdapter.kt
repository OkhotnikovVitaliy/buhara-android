package kg.buhara.view.restaurant

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.data.model.RestaurantModel
import kg.buhara.utils.ID
import kg.buhara.utils.LOGO
import kg.buhara.view.detail_restaurant.DetailRestaurantActivity
import kg.buhara.view.dish.DishAdapter
import kg.buhara.view.menu.MenuActivity
import kotlinx.android.synthetic.main.rv_item_restaurant.view.*


class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.BuildingViewHolder>() {

    private var data: ArrayList<RestaurantModel> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        return BuildingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_restaurant, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: ArrayList<RestaurantModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun updateData(data: ArrayList<RestaurantModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAll() {
        data = ArrayList()
        notifyDataSetChanged()
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: RestaurantModel) = with(itemView) {
            title.text = item.card_title
            if (item.cover != null) {
                Glide.with(context).load(item.cover).into(cover)
            }else{
                Glide.with(context).load(R.drawable.rounded_shape).into(cover)
            }
            if (item.logo != null) {
                Glide.with(context).load(item.logo).into(logo)
            }else{
                Glide.with(context).load(R.drawable.rounded_shape).into(logo)
            }
            itemView.setOnClickListener {
                val intent = Intent(context, DetailRestaurantActivity::class.java)
                intent.putExtra(ID, item.id)
                context.startActivity(intent)
            }

            content_menu.setOnClickListener {
                val intent = Intent(context, MenuActivity::class.java)
                intent.putExtra(ID, item.id)
                intent.putExtra(LOGO, item.logo)
                context.startActivity(intent)
            }
        }
    }
}