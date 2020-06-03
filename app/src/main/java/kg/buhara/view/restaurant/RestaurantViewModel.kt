package kg.buhara.view.restaurant

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.RestaurantModel
import kg.buhara.data.repository.RestaurantRepo
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

class RestaurantViewModel(val context: Context, private val authRepo: RestaurantRepo) :
    ViewModel() {

    var _restaurantsLiveData = MutableLiveData<ArrayList<RestaurantModel>>()
    var restaurantsLiveData: LiveData<ArrayList<RestaurantModel>> = _restaurantsLiveData

    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()
    var isInternetConnect = MutableLiveData<Boolean>()

    fun fetchRestaurantsCoroutines(page: Int) {
        isInternetConnect.value = Connect.initIntertnet(context)
        if (!Connect.initIntertnet(context)) return
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = authRepo.fetchRestaurants(page)
            if (response != null) {
                if (response.isSuccessful) {
                    isInternetConnect.value = true
                    _restaurantsLiveData.value = response.body()?.results
                }
            }
            loadingVisibility.value = false
        }
    }

//    fun getRestaurantsResponseLiveDate(): LiveData<ArrayList<RestaurantModel>> {
//        return restaurantsLiveData
//    }


}