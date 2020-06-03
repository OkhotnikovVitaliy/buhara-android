package kg.buhara.view.detail_restaurant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kg.buhara.R
import kg.buhara.data.full_photos.FullPhotoActivity
import kg.buhara.data.model.RestaurantModel
import kg.buhara.utils.FILES
import kg.buhara.utils.ID
import kg.buhara.utils.LOGO
import kg.buhara.utils.POSTITION
import kg.buhara.utils.extentions.actionCall
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kg.buhara.view.menu.MenuActivity
import kg.buhara.view.review.ReviewActivity
import kotlinx.android.synthetic.main.activity_detail_restaurant.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel


class DetailRestaurantActivity : AppCompatActivity() {

    private val viewModel: DetailRestaurantViewModel by viewModel()
    var dialog: AlertDialog? = null
    var id: Int = 0
    private var data = RestaurantModel()
    lateinit var adapter: PromotionDiscountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_restaurant)
        id = intent.getIntExtra(ID, 0)
        init()
    }

    private fun init() {
        initToolbar()
        initRV()
        configureAuthViewModel()
        onClickActions()
    }

    private fun onClickActions() {
        call_container.setOnClickListener {
            data.phone?.let { it1 -> actionCall(it1) }
        }

        comment_container.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra(ID, data.id)
            startActivity(intent)
        }

        map_container.setOnClickListener {
            if (data.latitude != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${data.longitude},${data.latitude} ("+data.address+")"))
                startActivity(intent)
            }
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra(ID, data.id)
            intent.putExtra(LOGO, data.logo)
            startActivity(intent)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun configureAuthViewModel() {

        viewModel.fetchDetailRestaurant(id)

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it) {
                    progress_layout.visible()
                    dialog?.show()
                } else {
                    progress_layout.gone()
                    dialog?.dismiss()
                }
            })

        viewModel.errorMessageLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                toast(it)
            })

        viewModel.restaurantsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                setData(data = it)
            })

        viewModel.promoyionsAndDiscountsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it.size == 0){
                    messagePromotionsAndDiscounts.gone()
                }
                adapter.swapData(it)
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: RestaurantModel) {
        this.data = data
        description.text = data.description
        if (data.start_time != null) {
            time.visible()
            messageTime.visible()
            time.text = "${data.start_time?.substring(0, 5)} - ${data.end_time?.substring(0, 5)}"
        }else{
//            time.gone()
//            messageTime.gone()
            time.text = "24/7"
        }
        Log.e("CARD_TITILE", data.title)
        address.text = data.address
        titleToolbar.text = data.title
//        addressToolbar.text = data.address
        data.images?.let { initGallaries(it) }
        viewModel.fetchPromotionsAndDiscounts(data.id)
        if (data.latitude == null) {
            map_container.gone()
        }
    }

    private fun initRV() {
        adapter = PromotionDiscountAdapter()
        val llm = LinearLayoutManager(this)
        recyclerViewPromotionsDiscounts.layoutManager = llm
        recyclerViewPromotionsDiscounts.isNestedScrollingEnabled = false
        recyclerViewPromotionsDiscounts.adapter = adapter
    }

    private fun initGallaries(sliders: ArrayList<String>) {
        val pageAdapter = PagerGalaryAdapter(this, sliders, false)
        pager.adapter = pageAdapter

        indicator.setViewPager(pager)
        if (sliders.size == 1) {
            indicator.visibility = View.GONE
        } else {
            indicator.visibility = View.VISIBLE
        }

        pageAdapter.setOnClickListener() { position ->
            val intent = Intent(this, FullPhotoActivity::class.java)
            intent.putExtra(POSTITION, position)
            intent.putExtra(FILES, sliders)
            startActivity(intent)
        }
    }


}
