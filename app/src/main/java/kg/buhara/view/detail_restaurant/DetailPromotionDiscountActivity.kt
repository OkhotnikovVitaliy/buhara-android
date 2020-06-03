package kg.buhara.view.detail_restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kg.buhara.R
import kg.buhara.data.model.StockModel
import kg.buhara.utils.ADDRESS
import kg.buhara.utils.STOCK
import kg.buhara.utils.TITLE
import kotlinx.android.synthetic.main.activity_detail_promotion_discount.*
import kotlinx.android.synthetic.main.activity_profile.toolbar

class DetailPromotionDiscountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promotion_discount)
        initToolbar()
        setData()
    }

    private fun setData() {
        var stock = intent.getSerializableExtra(STOCK) as StockModel
        titleToolbar.text = stock.title
        addressToolbar.text = stock.expired
        titlePromotion.text = stock.title
        description.text = stock.description
        Glide.with(this).load(stock.cover).into(cover)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
