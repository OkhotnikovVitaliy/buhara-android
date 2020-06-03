package kg.buhara.view.menu

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dmax.dialog.SpotsDialog
import kg.buhara.R
import kg.buhara.data.model.CategoryModel
import kg.buhara.data.model.DishModel
import kg.buhara.utils.*
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kg.buhara.view.cart.CartActivity
import kg.buhara.view.dish.DishActivity
import kg.buhara.view.dish.DishAdapter
import kg.buhara.view.dish.DishViewModel
import kg.buhara.view.main.MainViewModel
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_menu.badge_text_view
import kotlinx.android.synthetic.main.activity_subcategory_menu.*
import kotlinx.android.synthetic.main.activity_subcategory_menu.appbar
import kotlinx.android.synthetic.main.activity_subcategory_menu.edit_search
import kotlinx.android.synthetic.main.activity_subcategory_menu.fab
import kotlinx.android.synthetic.main.activity_subcategory_menu.ic_cart
import kotlinx.android.synthetic.main.activity_subcategory_menu.logo
import kotlinx.android.synthetic.main.activity_subcategory_menu.message_all
import kotlinx.android.synthetic.main.activity_subcategory_menu.recyclerView
import kotlinx.android.synthetic.main.activity_subcategory_menu.recyclerViewCategory
import kotlinx.android.synthetic.main.activity_subcategory_menu.toolbar
import org.koin.android.viewmodel.ext.android.viewModel

class SubcategoryMenuActivity : AppCompatActivity(), DishAdapter.OnItemListener, TextWatcher {

    lateinit var categoryAdapter: CategoryAdapter
    lateinit var dishAdapter: DishAdapter
    private val dishViewModel: DishViewModel by viewModel()
    var idRestaurant: Int = 0
    var idCategory: Int = 0
    var childCategories: ArrayList<CategoryModel> = ArrayList()
    var logoRestaurant: String = ""
    private var listener: EndlessRecyclerOnScrollListener? = null
    var query: String? = ""
    var categoryName: String? = ""
    var dialog: AlertDialog? = null
    private var position = 0
    val viewCountBadgeModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subcategory_menu)
        init()
    }


    private fun init() {
        configureAuthViewModel()

        dialog = SpotsDialog.Builder().setContext(this).setMessage(R.string.loading_message).build()

        idRestaurant = intent.getIntExtra(ID, 0)
        idCategory = intent.getIntExtra(ID_CATEGORY, 0)
        categoryName = intent.getStringExtra(TITLE)
        childCategories = intent.getSerializableExtra(CHILD_CATEGORIES) as ArrayList<CategoryModel>
        if (childCategories.size == 0) {
            txt_menu.gone()
        }
        message_all.text = categoryName
        logoRestaurant = intent.getStringExtra(LOGO)
        Glide.with(this).load(logoRestaurant).into(logo)
        initToolbar()
        initRVCategory()
        initRV()
        configureViewModel()
        edit_search.addTextChangedListener(this)
        edit_search.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
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

    private fun smoothScrollTop() {
        recyclerView.smoothScrollToPosition(0)
        appbar.setExpanded(true)
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

        dishViewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                //                if (it) {
//                    dialog?.show()
//                } else {
//                    dialog?.dismiss()
//                }
                viewCountBadgeModel.fetchCartCout()
            })

        dishViewModel.dishesLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val list = it.contentIfNotHandled
                    if (list != null) {
                        dishAdapter.insertData(list)
                    }
                }
            })

        categoryAdapter.updateItems(childCategories)

        dishViewModel.isChangeCartLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                viewCountBadgeModel.fetchCartCout()
            })

    }

    private fun performSearch() {
        query = edit_search.text.toString()
        if (query.isNullOrEmpty()) {
            message_all.text = categoryName
        } else {
            message_all.text = resources.getString(R.string.result_products)
        }
        dishAdapter.removeAll()
        load(1)
    }


    private fun initRVCategory() {
        categoryAdapter = CategoryAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategory.layoutManager = layoutManager
        recyclerViewCategory.adapter = categoryAdapter
        recyclerViewCategory.isNestedScrollingEnabled = true

        categoryAdapter.setOnItemClickListener { category, position ->
            if (category.child_categories?.size != 0) {
                val intent = Intent(this, SubcategoryMenuActivity::class.java)
                intent.putExtra(CHILD_CATEGORIES, category.child_categories)
                intent.putExtra(ID, idRestaurant)
                intent.putExtra(LOGO, logoRestaurant)
                intent.putExtra(TITLE, category.title)
                startActivity(intent)
            } else {
                dishAdapter.removeAll()
                idCategory = category.id
                categoryName = category.title
                message_all.text = category.title
                load(1)
            }
        }
    }

    private fun initRV() {
        dishAdapter = DishAdapter(this)
        val glm = GridLayoutManager(this, 2)
        recyclerView.layoutManager = glm
        recyclerView.isNestedScrollingEnabled = true
        listener = object : EndlessRecyclerOnScrollListener(glm) {
            override fun onLoadMore(current_page: Int) {
                load(current_page)
            }
        }
        recyclerView.addOnScrollListener(listener as EndlessRecyclerOnScrollListener)
        recyclerView.adapter = dishAdapter

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onItemClick(data: DishModel?, image: ImageView, position: Int) {
        this.position = position
        val intent = Intent(this, DishActivity::class.java)
        // Get the transition name from the string
        val transitionName: String = getString(R.string.transition_string)
        // Define the view that the animation will start from
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            image,  // Starting view
            transitionName // The String
        )
        intent.putExtra(ID, data?.id)
        intent.putExtra(IMAGE, data?.image)
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
        Log.e("ASDASDASD", current_page.toString())
        dishViewModel.fetchDishesByCategory(idCategory, current_page, query)
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
                dishAdapter.removeAll()
                configureViewModel()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewCountBadgeModel.fetchCartCout()
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
                        if (count.cart_total_items == 0) {
                            badge_text_view.gone()
                        } else {
                            badge_text_view.visible()
                        }
                    }
                }
            })

    }


}
