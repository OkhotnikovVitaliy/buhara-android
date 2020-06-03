package kg.buhara.view.cart

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dmax.dialog.SpotsDialog
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.data.model.cart.CartModel
import kg.buhara.utils.EndlessRecyclerOnScrollListener
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.visible
import kg.buhara.view.main.MainActivity
import kg.buhara.view.settings.DiscountConditionActivity
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_restaurant.recyclerView
import kotlinx.android.synthetic.main.fragment_restaurant.swipe_container
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel


class CartFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, CartAdapter.OnItemListener {

    private val viewModel: CartViewModel by viewModel()
    lateinit var adapter: CartAdapter
    private var listener: EndlessRecyclerOnScrollListener? = null
    var dialog: AlertDialog? = null
    var isCartActivity:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val args = arguments
        if (args != null) {
            isCartActivity = args.getBoolean("isCartActivity", false) as Boolean
        }

        dialog =
            SpotsDialog.Builder().setContext(activity).setMessage(R.string.loading_message).build()
        swipe_container.setOnRefreshListener(this)
        configureViewModel()
        initRV()

        cardViewDelivery.setOnClickListener {
            val intent = Intent(context, DiscountConditionActivity::class.java)
            intent.putExtra("isDelivery", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCarts()
    }

    private fun configureViewModel() {
        viewModel.loadingAlertVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                swipe_container.isRefreshing = it
//                if (it){
////                   dialog?.show()
////               }else{
////                   dialog?.dismiss()
////               }

            })

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    swipe_container.isRefreshing = it
//                    if (it) {
//                        progress_layout.visible()
//                    } else {
//                        progress_layout.gone()
//                    }
                }
            })


        viewModel.isDeleteLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                viewModel.fetchCarts()
                if (isCartActivity) {
                    (activity as CartActivity?)?.viewCountBadgeModel?.fetchCartCout()
                }else{
                    (activity as MainActivity?)?.viewModel?.fetchCartCout()
                }
            })

        viewModel.cartsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                adapter.swapData(it)
            })
    }


    private fun initRV() {
        adapter = CartAdapter(recyclerView)
        val glm = LinearLayoutManager(activity)
        recyclerView.layoutManager = glm
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    override fun onRefresh() {
        viewModel.fetchCarts()
    }

    override fun onItemCartDishFavoriteClick(dishModel: DishModel?) {
        if (dishModel != null) {
            dishModel.id?.let { viewModel.favoriteDish(it) }
        }
    }

    override fun onItemCartDishUpdateClick(dishModel: DishModel?) {
        if (dishModel != null) {
            dishModel.in_cart?.let { dishModel.id?.let { it1 -> viewModel.cartUpdateDish(it1, it) } }
        }
    }

    override fun onItemCartDishDeleteClick(dishModel: DishModel?) {
        if (dishModel != null) {
            deleteDishAlert(dishModel)
        }
    }

    fun deleteDishAlert(dishModel: DishModel) {
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setMessage("Вы уверерны что хотите удалить?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                run {
                    dishModel.id?.let { viewModel.cartDeleteDish(it) }
                }
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Удалить")
        alert.show()
    }


    override fun onItemClick(category: CartModel?, position: Int) {
    }

}
