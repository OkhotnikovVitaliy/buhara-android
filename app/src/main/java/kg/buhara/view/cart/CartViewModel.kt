package kg.buhara.view.cart

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.CategoryModel
import kg.buhara.data.model.DishModel
import kg.buhara.data.model.cart.CartModel
import kg.buhara.data.repository.CartRepo
import kg.buhara.data.repository.DishRepo
import kg.buhara.utils.Connect
import kg.buhara.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by DAS since 2019-11-14
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class CartViewModel(val context: Context, private val repo: CartRepo, private val dishRepo: DishRepo) : ViewModel() {

    var cartsLiveData = MutableLiveData<ArrayList<CartModel>>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()
    var loadingAlertVisibility = MutableLiveData<Boolean>()
    var isDeleteLiveData = MutableLiveData<Boolean>()
    var isFavourite = MutableLiveData<Boolean>()
    var isInternetConnect = MutableLiveData<Event<Boolean>>()

    fun fetchCarts() {
        isInternetConnect.value = Event(Connect.initIntertnet(context))
        if (!Connect.initIntertnet(context)) return
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchCarts()
            if (response != null) {
                if (response.isSuccessful) {
                    cartsLiveData.value = response.body()
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun favoriteDish(idDish: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = dishRepo.favouriteDish(idDish)
            if (response != null) {
                isFavourite.value = response.isSuccessful
            }
        }
    }

    fun cartUpdateDish(idDish: Int, quantity:Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = dishRepo.cartUpdateDish(idDish, quantity)
            if (response != null) {
                isDeleteLiveData.value = response.isSuccessful
            }
        }
    }

    fun cartDeleteDish(idDish: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = dishRepo.cartUpdateDish(idDish, 0)
            if (response != null) {
                isDeleteLiveData.value = response.isSuccessful
            }
        }
    }
}