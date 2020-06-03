package kg.buhara.view.delivery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.RestaurantModel
import kg.buhara.data.repository.DeliveryRepo
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

class DeliveryViewModel(val context: Context, private val repo: DeliveryRepo) :
    ViewModel() {

    var _restaurantsLiveData = MutableLiveData<ArrayList<RestaurantModel>>()
    var restaurantsLiveData: LiveData<ArrayList<RestaurantModel>> = _restaurantsLiveData
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()
    var isInternetConnect = MutableLiveData<Event<Boolean>>()

    fun fetchRestaurantsCoroutines(page: Int) {
        isInternetConnect.value = Event(Connect.initIntertnet(context))
        if (!Connect.initIntertnet(context)) return
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchRestaurants(page)
            if (response != null) {
                if (response.isSuccessful) {
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