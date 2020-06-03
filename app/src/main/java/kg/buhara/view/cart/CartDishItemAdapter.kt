package kg.buhara.view.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.data.model.cart.CartItems
import kg.buhara.utils.ID
import kg.buhara.view.dish.DishActivity
import kotlinx.android.synthetic.main.rv_item_cart_dish.view.*
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.favorite
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.image
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.minus
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.plus
import kotlinx.android.synthetic.main.rv_item_popular_dish.view.price


class CartDishItemAdapter(val listener: OnItemListener) :
    RecyclerView.Adapter<CartDishItemAdapter.BuildingViewHolder>() {

    private var data: ArrayList<CartItems> = arrayListOf()

    interface OnItemListener {
        fun onItemClick(data: DishModel?, card: ImageView)
        fun onItemFavoriteClick(data: DishModel?)
        fun onItemCartUpdateClick(data: DishModel?)
        fun onItemCartDeleteClick(data: DishModel?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        return BuildingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_item_cart_dish,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position], listener)

    fun swapData(data: ArrayList<CartItems>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun insertData(data: ArrayList<CartItems>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartItems, listener: OnItemListener) = with(itemView) {
            name.text = Html.fromHtml(item.dish.title )
            price.text = "${item.dish.price} сом"
            quantity.text = "${item.dish.in_cart} шт"

            if (item.dish.image != null) {
                Glide.with(context).load(item.dish.image).into(image)
            }
            if (item.dish.is_favourite!!) {
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
            } else {
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
            }

            favorite.setOnClickListener {
                if (item.dish.is_favourite!!) {
                    favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
                    item.dish.is_favourite = false
                } else {
                    favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
                    item.dish.is_favourite = true
                }
                listener.onItemFavoriteClick(item.dish)
            }

            plus.setOnClickListener {
                item.dish.in_cart = item.dish.in_cart?.plus(1)
                quantity.text = "${item.dish.in_cart} шт"
                listener.onItemCartUpdateClick(item.dish)
            }

            minus.setOnClickListener {
                if (item.dish.in_cart == 0) return@setOnClickListener
                item.dish.in_cart = item.dish.in_cart?.minus(1)
                quantity.text = "${item.dish.in_cart} шт"
                listener.onItemCartUpdateClick(item.dish)
            }

            delete.setOnClickListener {
                listener.onItemCartDeleteClick(item.dish)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, DishActivity::class.java)
                intent.putExtra(ID, item.dish.id)
                context.startActivity(intent)
            }


        }
    }

}