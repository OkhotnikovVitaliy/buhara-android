package kg.buhara.view.restaurant

import android.content.Intent
import android.os.Bundle
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
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.visible
import kg.buhara.view.main.MainActivity
import kg.buhara.view.profile.ProfileActivity
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.android.synthetic.main.restaurant_shimmer.*
import org.koin.android.viewmodel.ext.android.viewModel


class RestaurantFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: RestaurantViewModel by viewModel()
    lateinit var adapter: RestaurantAdapter
    private var listener: EndlessRecyclerOnScrollListener? = null
    private var restaurants: ArrayList<RestaurantModel> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        swipe_container.setOnRefreshListener(this)
        swipe_container.setOnRefreshListener(this)
        configureViewModel()
        initRV()

        btn_connect_internet.setOnClickListener {
            load(1)
        }
    }

    override fun onResume() {
        super.onResume()
        restaurants = arrayListOf()
        load(1)
    }

    private fun configureViewModel() {
        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                swipe_container.isRefreshing = it
                if (it) {
                    progress_layout.visibility = View.VISIBLE
                } else {
                    progress_layout.visibility = View.GONE
                }
            })

        viewModel.restaurantsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    restaurants.addAll(it)
                    adapter.swapData(restaurants)
                }
            })

        viewModel.isInternetConnect.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    if (it) {
                        container_no_internet.gone()
                    } else {
                        container_no_internet.visible()
                    }
                }
            })
    }

    private fun load(current_page: Int) {
        if (current_page == 1){
            restaurants = arrayListOf()
        }
        viewModel.fetchRestaurantsCoroutines(current_page)
    }

    private fun initRV() {
        adapter = RestaurantAdapter()
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
        load(1)
    }


}
