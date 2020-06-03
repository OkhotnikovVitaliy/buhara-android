package kg.buhara.view.delivery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kg.buhara.R
import kg.buhara.data.model.RestaurantModel
import kg.buhara.utils.EndlessRecyclerOnScrollListener
import kg.buhara.utils.IS_UPDATE_PROFILE
import kg.buhara.utils.UserInfoPref
import kg.buhara.utils.extentions.toast
import kg.buhara.view.profile.ProfileActivity
import kg.buhara.view.restaurant.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.restaurant_shimmer.*
import org.koin.android.viewmodel.ext.android.viewModel


class DeliveryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: DeliveryViewModel by viewModel()
    lateinit var adapter: DeliveryAdapter
    private var listener: EndlessRecyclerOnScrollListener? = null
    private var restaurants: ArrayList<RestaurantModel> = arrayListOf()
    var page = 1;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        swipe_container.setOnRefreshListener(this)
        configureViewModel()
        initRV()
    }

    override fun onResume() {
        super.onResume()
        restaurants = arrayListOf()
        viewModel.fetchRestaurantsCoroutines(1)
    }

    private fun configureViewModel() {
        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                swipe_container.isRefreshing = it
            })

        viewModel.restaurantsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    if (page == 1){
                        restaurants = arrayListOf()
                    }
                    Log.e("DELIVERY", "${restaurants.size} ::: ${page}")
                    restaurants.addAll(it)
                    adapter.swapData(it)
                }
            })

    }

    private fun load12(current_page: Int) {
        Log.e("LOAD_DELIVERY", "${current_page} - ${restaurants.size}")
        if (current_page == 1) {
            restaurants = arrayListOf()
        }
        page = current_page
        viewModel.fetchRestaurantsCoroutines(current_page)
    }

    private fun initRV() {
        adapter = DeliveryAdapter()
        val llm = LinearLayoutManager(activity)
        recyclerView.layoutManager = llm
        recyclerView.isNestedScrollingEnabled = true
//        listener = object : EndlessRecyclerOnScrollListener(llm) {
//            override fun onLoadMore(current_page: Int) {
//                load(current_page)
//            }
//        }
//        recyclerView.addOnScrollListener(listener as EndlessRecyclerOnScrollListener)
        recyclerView.adapter = adapter
    }

    override fun onRefresh() {
        adapter.removeAll()
        restaurants = arrayListOf()
        initRV()
        viewModel.fetchRestaurantsCoroutines(1)
    }

}
