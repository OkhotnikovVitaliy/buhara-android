package kg.buhara.view.detail_restaurant

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import kg.buhara.R
import kg.buhara.data.model.StockModel
import kg.buhara.utils.STOCK
import kg.buhara.view.detail_restaurant.DetailPromotionDiscountActivity
import kotlinx.android.synthetic.main.rv_item_promotion_and_discount.view.*
import kotlinx.android.synthetic.main.rv_item_restaurant.view.title


class PromotionDiscountAdapter :
    RecyclerView.Adapter<PromotionDiscountAdapter.BuildingViewHolder>() {

    private var data: List<StockModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        return BuildingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_promotion_and_discount, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: ArrayList<StockModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun removeAll() {
        data = arrayListOf()
        notifyDataSetChanged()
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: StockModel) = with(itemView) {
            title.text = Html.fromHtml(item.title)
            if (item.cover != null) {
                Log.e("COVERR", item.cover)
                Glide.with(context).load(item.cover).into(image)
//                Picasso.get().load(item.cover).into(image);
            }
            itemView.setOnClickListener {
                val intent = Intent(context, DetailPromotionDiscountActivity::class.java)
                intent.putExtra(STOCK, item)
                context.startActivity(intent)
            }
        }
    }
}