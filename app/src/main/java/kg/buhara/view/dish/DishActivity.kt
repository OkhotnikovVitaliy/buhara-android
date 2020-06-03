package kg.buhara.view.dish

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.DishModel
import kg.buhara.utils.ID
import kg.buhara.utils.PRODUCT
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.toast
import kg.buhara.utils.extentions.visible
import kg.buhara.view.cart.CartActivity
import kotlinx.android.synthetic.main.activity_dish.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.layout_progress.*
import org.koin.android.viewmodel.ext.android.viewModel


class DishActivity : AppCompatActivity() {

    private val viewModel: DishViewModel by viewModel()

    var idDish = 0
    var dishData = DishModel()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dish)
        init()
    }

    private fun init() {
        idDish = intent.getIntExtra(ID, 0)
//        dishData = intent.getSerializableExtra(PRODUCT) as DishModel
        initToolbar()
        configureViewModel()

        img_like.setOnClickListener {
            onItemFavoriteClick()
        }

        plus.setOnClickListener {
            dishData.in_cart = dishData.in_cart?.plus(1)
            dish_count.text = "${dishData.in_cart} шт"

            if (dishData.in_cart!! == 1) {
                toast(resources.getString(R.string.product_added_in_cart))
            }

            onItemCartUpdateClick()
        }

        minus.setOnClickListener {
            if (dishData.in_cart == 0) return@setOnClickListener
            dishData.in_cart = dishData.in_cart?.minus(1)
            dish_count.text = "${dishData.in_cart} шт"

            if (dishData.in_cart!! == 0) {
                toast(resources.getString(R.string.product_deleted_in_cart))
            }
            onItemCartUpdateClick()
        }

        btnGoToBasket.setOnClickListener {
            finish()
//            startActivity(Intent(this, CartActivity::class.java))
        }

    }

    private fun configureViewModel() {
        viewModel.fetchDish(idDish)
        viewModel.dishLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                setData(it)
            })

        viewModel.loadingVisibility.observe(
            this,
            androidx.lifecycle.Observer {
                if (it) {
                    progress_layout.visible()
                } else {
                    progress_layout.gone()
                }
            })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun setData(data: DishModel) {
        this.dishData = data
        titleDishToolbar.text = Html.fromHtml(data.title)
        description.text = data.description
        weight.text = data.weight
        price.text = "${data.price} сом"
        dish_count.text = "${data.in_cart} шт"
        Glide.with(this).load(data.image).into(image)

        if (data.is_favourite!!) {
            img_like.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
        } else {
            img_like.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
        }

    }

    private fun onItemCartUpdateClick() {
        viewModel.cartUpdateDish(dishData?.id!!, dishData.in_cart!!)
    }

    private fun onItemFavoriteClick() {
        if (dishData.is_favourite!!) {
            dishData.is_favourite = false
            img_like.setImageDrawable(resources.getDrawable(R.drawable.ic_like_false))
        } else {
            dishData.is_favourite = true
            img_like.setImageDrawable(resources.getDrawable(R.drawable.ic_like_true))
        }
        dishData.id?.let { viewModel.favoriteDish(it) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra(PRODUCT, dishData)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}
