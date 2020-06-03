package kg.buhara.view.dish

import android.annotation.SuppressLint
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.*


class DishAdapter(val listener: OnItemListener) : RecyclerView.Adapter<DishAdapter.BuildingViewHolder>() {

    private var data: ArrayList<DishModel> = arrayListOf()
    private var isPopular: Boolean = false

    interface OnItemListener {
        fun onItemClick(data: DishModel?, card: ImageView, position: Int)
        fun onItemFavoriteClick(data: DishModel?)
        fun onItemCartUpdateClick(data: DishModel?, isPlus: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        if (isPopular) {
            return BuildingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.rv_item_popular_dish,
                    parent,
                    false
                )
            )
        }
        return BuildingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_item_dish,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position], listener, position)

    fun isPopular(isPopular: Boolean) {
        this.isPopular = isPopular
    }

    fun swapData(data: ArrayList<DishModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun notifyData(position: Int, dishModel: DishModel) {
        Log.e("NO", "TIFY")
        if (data!=null && data[position]!=null) {
            data[position] = dishModel
            notifyItemChanged(position)
        }
    }

    fun insertData(data: ArrayList<DishModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAll() {
        data = ArrayList()
        notifyDataSetChanged()
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DishModel, listener: OnItemListener, position: Int) = with(itemView) {
            title.text = Html.fromHtml(item.title)
            price.text = "${item.price} сом"
            dish_count.text = "${item.in_cart} шт"

            if (item.image != null) {
                Glide.with(context).load(item.image).into(image)
            }else{
                Glide.with(context).load(R.drawable.rounded_shape).into(image)
            }
            if (item.is_favourite!!) {
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
            } else {
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
            }

            favorite.setOnClickListener {
                if (item.is_favourite!!) {
                    favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
                    item.is_favourite = false
                } else {
                    favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
                    item.is_favourite = true
                }
                listener.onItemFavoriteClick(item)
            }

            plus.setOnClickListener {
                item.in_cart = item.in_cart?.plus(1)
                dish_count.text = "${item.in_cart} шт"
                listener.onItemCartUpdateClick(item, true)
            }
            minus.setOnClickListener {
                if (item.in_cart == 0) return@setOnClickListener
                item.in_cart = item.in_cart?.minus(1)
                dish_count.text = "${item.in_cart} шт"
                listener.onItemCartUpdateClick(item, false)
            }

            itemView.setOnClickListener {
                listener.onItemClick(item, image, position)
//                val intent = Intent(context, DishActivity::class.java)
//                intent.putExtra(ID, item.id)
//                context.startActivity(intent)
            }


        }
    }

}