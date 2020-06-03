package kg.buhara.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kg.buhara.R
import kg.buhara.utils.IS_UPDATE_PROFILE
import kg.buhara.utils.UserInfoPref
import kg.buhara.utils.extentions.toast
import kg.buhara.view.delivery.DeliveryFragment
import kg.buhara.view.auth.LoginActivity
import kg.buhara.view.cart.CartFragment
import kg.buhara.view.favorite.FavoriteFragment
import kg.buhara.view.profile.ProfileActivity
import kg.buhara.view.qr.QRFragment
import kg.buhara.view.qr.WaiterActivity
import kg.buhara.view.restaurant.RestaurantFragment
import kotlinx.android.synthetic.main.activity_mainn.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModel()

    internal lateinit var navigation: BottomNavigationView
    internal var itemSelect = 0
    internal var restaurantFragment: Fragment = RestaurantFragment()
    internal var deliveryFragment: Fragment = DeliveryFragment()
    internal var qrFragment: Fragment = QRFragment()
    internal var cartFragment: Fragment = CartFragment()
    internal var favoriteFragment: Fragment = FavoriteFragment()
    internal var active: Fragment? = restaurantFragment
    var fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!UserInfoPref.isAuth(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else if (!UserInfoPref.isUserInfo(this)) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()

        }
        if (UserInfoPref.getAccessToken(this) == "waiter") {
            startActivity(Intent(this, WaiterActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainn)
        navigation = findViewById(R.id.navigation)
        initNavBar()
        configureAuthViewModel()

        status_image_panel.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(IS_UPDATE_PROFILE, true)
            startActivity(intent)
        }

    }

    private fun initNavBar() {
        fm = supportFragmentManager
        navigation.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            true
        }

//        fm.beginTransaction().add(R.id.content, deliveryFragment).hide(deliveryFragment).commit()
//        fm.beginTransaction().add(R.id.content, qrFragment).hide(qrFragment).commit()
//        fm.beginTransaction().add(R.id.content, cartFragment).hide(cartFragment).commit()
//        fm.beginTransaction().add(R.id.content, favoriteFragment).hide(favoriteFragment).commit()
        fm.beginTransaction().add(R.id.content, restaurantFragment).show(restaurantFragment).commit()
    }


    private fun selectFragment(item: MenuItem) {
        when (item.itemId) {
            R.id.navigation_restaurant -> {
                if (itemSelect != 0) {
                    custom_navigation_qr.setImageResource(R.drawable.qr_code)
                    clickNavBar(restaurantFragment, 0)
                    title_toolbar.text = resources.getString(R.string.restaurant)
                }
                return
            }
            R.id.navigation_delivery -> {
                if (itemSelect != 1) {
                    custom_navigation_qr.setImageResource(R.drawable.qr_code)
                    clickNavBar(deliveryFragment, 1)
                    title_toolbar.text = resources.getString(R.string.delivery)
                }
                return
            }
            R.id.navigation_qr -> {
                viewModel.profileDetail()
                if (itemSelect != 2) {
                    custom_navigation_qr.setImageResource(R.drawable.qr_code_selected)
                    clickNavBar(qrFragment, 2)
                    title_toolbar.text = resources.getString(R.string.message_qr)
                }
                return
            }
            R.id.navigation_favorite -> {
                if (itemSelect != 3) {
                    custom_navigation_qr.setImageResource(R.drawable.qr_code)
                    clickNavBar(favoriteFragment, 3)
                    title_toolbar.text = resources.getString(R.string.favorite)
                }
                return
            }
            R.id.navigation_cart -> {
                if (itemSelect != 4) {
                    custom_navigation_qr.setImageResource(R.drawable.qr_code)
                    clickNavBar(cartFragment, 4)
                    title_toolbar.text = resources.getString(R.string.cart)
                }
                return
            }
        }


        for (i in 0 until navigation.menu.size()) {
            val menuItem = navigation.menu.getItem(i)
            menuItem.isChecked = menuItem.itemId == item.itemId
        }


    }

    private fun clickNavBar(fragment: Fragment, item: Int) {
        Log.e("ISADDED", fragment.isAdded.toString())
        if (active == null) {
            fm.beginTransaction().show(fragment).commit()
            active = fragment
        } else {
            if (!fragment.isAdded){
                fm.beginTransaction().add(R.id.content, fragment).hide(fragment).commit()
            }
            fm.beginTransaction().hide(active!!).show(fragment).commit()
            fragment.onResume()
            active = fragment
            itemSelect = item
        }
    }


    override fun onBackPressed() {
        if (itemSelect != 0) {
            val view = navigation.findViewById<View>(R.id.navigation_restaurant)
            view.performClick()
        } else if (itemSelect == 0) {
            super.onBackPressed()
        }
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


    private fun configureAuthViewModel() {

        viewModel.profileDetail()
        viewModel.fetchCartCout()

        viewModel.profileResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val data = it.contentIfNotHandled
                    if (data != null) {
                        Glide.with(this).load(data.user_status?.status_image_panel)
                            .into(status_image_panel)
                    }
                }
            })

        viewModel.cartCountResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val count = it.contentIfNotHandled
                    if (count != null) {
                        showBadge(
                            navigation,
                            R.id.navigation_cart,
                            count.cart_total_items.toString()
                        )
                    }
                }
            })

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCartCout()
        viewModel.profileDetail()
    }

    private fun showBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int, value: String) {
        removeBadge(bottomNavigationView, itemId)
        val itemView = bottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
        val badge = LayoutInflater.from(this)
            .inflate(R.layout.layout_news_badge, bottomNavigationView, false)

        val text = badge.findViewById<TextView>(R.id.badge_text_view)
        text.text = value
        itemView.addView(badge)
    }

    companion object {

        fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
            val itemView = bottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
            if (itemView.childCount == 3) {
                itemView.removeViewAt(2)
            }
        }
    }

}
