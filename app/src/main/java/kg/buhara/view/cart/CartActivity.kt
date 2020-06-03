package kg.buhara.view.cart

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kg.buhara.R
import kg.buhara.utils.extentions.gone
import kg.buhara.utils.extentions.visible
import kg.buhara.view.main.MainViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.badge_text_view
import kotlinx.android.synthetic.main.activity_cart.toolbar
import kotlinx.android.synthetic.main.activity_menu.*
import org.koin.android.viewmodel.ext.android.viewModel


class CartActivity : AppCompatActivity() {

    private var cartFragment: Fragment = CartFragment()
    private val fm = supportFragmentManager

    val viewCountBadgeModel: MainViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initToolbar()
        setFragment()
        configureAuthViewModel()

    }

    override fun onResume() {
        super.onResume()
        viewCountBadgeModel.fetchCartCout()
    }

    private fun setFragment() {
        val args = Bundle()
        args.putBoolean("isCartActivity", true)
        cartFragment.arguments = args
        fm.beginTransaction().add(R.id.frame_container, cartFragment).show(cartFragment).commit()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (fragment in supportFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
                Log.d("Activity", "ON RESULT CALLED")
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
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
