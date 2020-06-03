package kg.buhara.view.delivery

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.RestaurantModel
import kg.buhara.utils.ID
import kg.buhara.utils.LOGO
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.visible
import kg.buhara.view.detail_restaurant.DetailRestaurantActivity
import kg.buhara.view.menu.MenuActivity
import kg.buhara.view.settings.DiscountConditionActivity
import kotlinx.android.synthetic.main.rv_item_delivery.view.*


class DeliveryAdapter : RecyclerView.Adapter<DeliveryAdapter.BuildingViewHolder>() {

    private var data: ArrayList<RestaurantModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        return BuildingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_delivery, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position], data.size, position)

    fun swapData(data: ArrayList<RestaurantModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun updateData(data: ArrayList<RestaurantModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }


    fun removeAll() {
        if (data != null) {
            data = arrayListOf()
            notifyDataSetChanged()
        }
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: RestaurantModel, dataSize:Int, position:Int) = with(itemView) {
            title.text = item.card_title
            address.text = item.address
            if (item.logo != null) {
                Glide.with(context).load(item.logo).into(logo)
            }else{
                Glide.with(context).load(R.drawable.rounded_shape).into(logo)
            }

            if (position == dataSize -1) {
                cardViewDelivery.visible()
            }else{
                cardViewDelivery.gone()
            }

            cardViewDelivery.setOnClickListener {
                val intent = Intent(context, DiscountConditionActivity::class.java)
                intent.putExtra("isDelivery", true)
                context.startActivity(intent)
            }
            itemView.setOnClickListener {
                val intent = Intent(context, MenuActivity::class.java)
                intent.putExtra(ID, item.id)
                intent.putExtra(LOGO, item.logo)
                context.startActivity(intent)
            }
        }
    }
}