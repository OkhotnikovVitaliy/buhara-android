package kg.buhara.view.menu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.utils.*
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.hideKeyboard
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kg.buhara.view.cart.CartActivity
import kg.buhara.view.dish.DishActivity
import kg.buhara.view.dish.DishAdapter
import kg.buhara.view.dish.DishViewModel
import kg.buhara.view.main.MainViewModel
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel


class MenuActivity : AppCompatActivity(), DishAdapter.OnItemListener, TextWatcher {

    lateinit var categoryAdapter: CategoryAdapter
    private val viewModel: MenuViewModel by viewModel()
    private val dishViewModel: DishViewModel by viewModel()
    var idRestaurant: Int = 0
    var logoRestaurant: String? = ""
    lateinit var dishAdapter: DishAdapter
    lateinit var popularAdapter: DishAdapter
    var listener: EndlessRecyclerOnScrollListener? = null
    var query: String? = null
    private var data: ArrayList<DishModel> = arrayListOf()
    private var position = 0
    val viewCountBadgeModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        init()
    }


    override fun onResume() {
        super.onResume()
        hideKeyboard()
        viewCountBadgeModel.fetchCartCout()
    }

    private fun init() {
        idRestaurant = intent.getIntExtra(ID, 0)
        logoRestaurant = intent.getStringExtra(LOGO)
        Glide.with(this).load(logoRestaurant).into(logo)

        initToolbar()
        initRVCategory()
        initRVPopular()
        initRVDishes()
        configureViewModel()
        configureAuthViewModel()


        edit_search.addTextChangedListener(this)
        edit_search.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })

        fab.setOnClickListener {
            smoothScrollTop()
        }

        ic_cart.setOnClickListener {
            startActivityForResult(Intent(this, CartActivity::class.java), 222)
        }


    }

    private fun performSearch() {
        query = edit_search.text.toString()
        if (query.isNullOrEmpty()) {
            message_popular.visible()
            recyclerViewPopular.visible()
            message_all.text = resources.getString(R.string.all_products)
        } else {
            message_popular.gone()
            recyclerViewPopular.gone()
            message_all.text = resources.getString(R.string.result_products)
        }
        data = arrayListOf()
        dishAdapter.removeAll()
        load(1)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun configureViewModel() {
        load(1)
        viewModel.fetchCategories(idRestaurant)
        dishViewModel.fetchPopularDishes(idRestaurant)

        viewCountBadgeModel.fetchCartCout()

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it) {
                    progress_layout.visibility = View.VISIBLE
                } else {
                    progress_layout.visibility = View.GONE
                }
            })

        viewModel.categoriesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                categoryAdapter.updateItems(it)
                viewCountBadgeModel.fetchCartCout()
            })

        dishViewModel.popularDishesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                popularAdapter.swapData(it)
                if (it.size == 0){
                    message_popular.gone()
                }else{
                    message_popular.visible()
                }
                if (it.size >= 3) (recyclerViewPopular.layoutManager as LinearLayoutManager).scrollToPosition(
                    0
                )
            })

        dishViewModel.restaurantDishesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                Log.e("SUCCESS", "ASD")
                if (it != null) {
                    val result = it.contentIfNotHandled

                    Log.e("SUCCESS", result?.size.toString())
                    if (result != null) {
                        if (result.size == 0){
                            message_all.gone()
                        }else{
                            message_all.visible()
                        }
                        data.addAll(result)
                        dishAdapter.swapData(data)

                    }
                }
            })

        dishViewModel.isChangeCartLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                viewCountBadgeModel.fetchCartCout()
            })
    }

    private fun smoothScrollTop() {
        recyclerView.smoothScrollToPosition(0)
        appbar.setExpanded(true)
    }

    private fun initRVCategory() {
        categoryAdapter = CategoryAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategory.layoutManager = layoutManager
        recyclerViewCategory.adapter = categoryAdapter
        recyclerViewCategory.isNestedScrollingEnabled = true

        categoryAdapter.setOnItemClickListener { category, position ->
            val intent = Intent(this, SubcategoryMenuActivity::class.java)
            intent.putExtra(CHILD_CATEGORIES, category.child_categories)
            intent.putExtra(ID, idRestaurant)
            intent.putExtra(ID_CATEGORY, category.id)
            intent.putExtra(LOGO, logoRestaurant)
            intent.putExtra(TITLE, category.title)
            startActivity(intent)
        }
    }

    private fun initRVPopular() {
        popularAdapter = DishAdapter(this)
        popularAdapter.isPopular(true)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopular.layoutManager = layoutManager
        recyclerViewPopular.adapter = popularAdapter
        recyclerViewPopular.isNestedScrollingEnabled = true

//        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
//        layoutManager.scrollToPosition(2)
//        recyclerViewPopular.layoutManager = layoutManager

    }

    private fun initRVDishes() {
        dishAdapter = DishAdapter(this)
        dishAdapter.removeAll()
        val glm = GridLayoutManager(this, 2)
        recyclerView.layoutManager = glm
        recyclerView.isNestedScrollingEnabled = true
        listener = object : EndlessRecyclerOnScrollListener(glm) {
            override fun onLoadMore(current_page: Int) {
                load(current_page)
            }
        }

        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(listener!!)
        recyclerView.adapter = dishAdapter

    }


    override fun onItemClick(data: DishModel?, imageView: ImageView, position: Int) {
        this.position = position
        val intent = Intent(this, DishActivity::class.java)
        // Get the transition name from the string
        val transitionName: String = getString(R.string.transition_string)

        // Define the view that the animation will start from
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,  // Starting view
            transitionName // The String
        )
        intent.putExtra(ID, data?.id)
        intent.putExtra(PRODUCT, data)
        //Start the Intent
        ActivityCompat.startActivityForResult(this, intent, RESULT_CODE, options.toBundle())
    }

    override fun onItemFavoriteClick(data: DishModel?) {
        data?.id?.let { dishViewModel.favoriteDish(it) }
    }

    override fun onItemCartUpdateClick(data: DishModel?, isPlus: Boolean) {
        if (data != null) {
            if (isPlus) {
                if (data.in_cart!! == 1) {
                    toast(resources.getString(R.string.product_added_in_cart))
                }
            } else {
                if (data.in_cart!! == 0) {
                    toast(resources.getString(R.string.product_deleted_in_cart))
                }
            }
        }
        dishViewModel.cartUpdateDish(data?.id!!, data.in_cart!!)
    }

    private fun load(current_page: Int) {
        if (current_page == 1) {
            dishAdapter.removeAll()
        }
        dishViewModel.fetchDishesByRestaurant(idRestaurant, current_page, query)
    }

    override fun afterTextChanged(p0: Editable?) {
        performSearch()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {
                dishAdapter.notifyData(position, data?.getSerializableExtra(PRODUCT) as DishModel)
            }
            if (requestCode == 222) {
                configureViewModel()
            }
        }

    }


    private fun configureAuthViewModel() {
        viewCountBadgeModel.fetchCartCout()

        viewCountBadgeModel.cartCountResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val count = it.contentIfNotHandled
                    if (count != null) {
                        badge_text_view.text = "${count.cart_total_items}"
                        if (count.cart_total_items == 0){
                            badge_text_view.gone()
                        }else{
                            badge_text_view.visible()
                        }
                    }
                }
            })

    }


}
