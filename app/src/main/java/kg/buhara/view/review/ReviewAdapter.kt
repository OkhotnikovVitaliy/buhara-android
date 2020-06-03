package kg.buhara.view.review

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.buhara.R
import kg.buhara.data.model.ReviewModel
import kotlinx.android.synthetic.main.rv_item_review.view.*


class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.BuildingViewHolder>() {

    private var data: ArrayList<ReviewModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        return BuildingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_review, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) =
        holder.bind(data[position])

    fun swapData(data: ArrayList<ReviewModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun insert(data: ReviewModel) {
        this.data.add(data)
        notifyDataSetChanged()
    }

    fun getSize(): Int {
        return data.size
    }

    fun removeAll() {
        data = arrayListOf()
        notifyDataSetChanged()
    }

    class BuildingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: ReviewModel) = with(itemView) {
            if (item.user != null) {
                name.text = item.user?.name
            }
            comment.text = item.body
            date.text = item.created_date
        }
    }
}