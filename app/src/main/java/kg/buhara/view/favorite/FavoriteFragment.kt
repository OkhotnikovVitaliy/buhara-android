package kg.buhara.view.favorite

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dmax.dialog.SpotsDialog
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.utils.EndlessRecyclerOnScrollListener
import kg.buhara.utils.ID
import kg.buhara.utils.PRODUCT
import kg.buhara.utils.RESULT_CODE
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kg.buhara.view.dish.DishActivity
import kg.buhara.view.dish.DishAdapter
import kg.buhara.view.dish.DishViewModel
import kg.buhara.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    DishAdapter.OnItemListener {

    private val viewModel: DishViewModel by viewModel()
    lateinit var adapter: DishAdapter
    private var listener: EndlessRecyclerOnScrollListener? = null
    var dialog: AlertDialog? = null
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        dialog = SpotsDialog.Builder().setContext(activity).setMessage(R.string.loading_message).build()
        swipe_container.setOnRefreshListener(this)
        configureViewModel()
        initRV()

    }

    override fun onResume() {
        super.onResume()
        viewModel.favoriteDishes()
    }
    private fun configureViewModel() {

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    swipe_container.isRefreshing = it
                    if (it) {
                        progress_layout.visible()
                    } else {
                        progress_layout.gone()
                    }
                }
            })

//        viewModel.errorMessageLiveData.observe(
//            this,
//            androidx.lifecycle.Observer {
//                activity?.toast(it)
//            })

        viewModel.isChangeCartLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                (activity as MainActivity?)?.viewModel?.fetchCartCout()
            })


        viewModel.favoriteDishesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                adapter.swapData(it)
            })
    }


    private fun initRV() {
        adapter = DishAdapter(this)
        val glm = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = glm
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.adapter = adapter
    }

    override fun onRefresh() {
        viewModel.favoriteDishes()
    }

    override fun onItemClick(data: DishModel?, card: ImageView, position: Int) {
        this.position = position
        val intent = Intent(activity, DishActivity::class.java)
        // Get the transition name from the string
        val transitionName: String = getString(R.string.transition_string)

        // Define the view that the animation will start from
        val options = activity?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                it,
                card,  // Starting view
                transitionName // The String
            )
        }
        intent.putExtra(ID, data?.id)
        //Start the Intent
        activity?.let {
            if (options != null) {
                ActivityCompat.startActivityForResult(it, intent, RESULT_CODE, options.toBundle())

            }
        }
    }

    override fun onItemFavoriteClick(data: DishModel?) {
        data?.id?.let { viewModel.favoriteDish(it) }
    }

    override fun onItemCartUpdateClick(data: DishModel?, isPlus:Boolean) {
        if (data != null) {
            if (isPlus) {
                if (data.in_cart!! == 1) {
                    activity?.toast(resources.getString(R.string.product_added_in_cart))
                }
            } else {
                if (data.in_cart!! == 0) {
                    activity?.toast(resources.getString(R.string.product_deleted_in_cart))
                }
            }
        }
        viewModel.cartUpdateDish(data?.id!!, data.in_cart!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            adapter.notifyData(position, data?.getSerializableExtra(PRODUCT) as DishModel)
        }
    }


}
