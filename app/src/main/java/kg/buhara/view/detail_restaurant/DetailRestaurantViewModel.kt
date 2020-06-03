package kg.buhara.view.detail_restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.RestaurantModel
import kg.buhara.data.model.StockModel
import kg.buhara.data.repository.DetailRestaurantRepo
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

class DetailRestaurantViewModel(private val repo: DetailRestaurantRepo) : ViewModel() {

    var restaurantsLiveData = MutableLiveData<RestaurantModel>()
    var promoyionsAndDiscountsLiveData = MutableLiveData<ArrayList<StockModel>>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()
    var loadingPromotionsAndDisountsVisibility = MutableLiveData<Boolean>()

    fun fetchDetailRestaurant(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchDetailRestaurant(id)
            if (response != null) {
                if (response.isSuccessful) {
                    restaurantsLiveData.value = response.body()
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun fetchPromotionsAndDiscounts(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingPromotionsAndDisountsVisibility.value = true
            val response = repo.fetchPromotionsAndDiscounts(id)
            if (response != null) {
                if (response.isSuccessful) {
                    promoyionsAndDiscountsLiveData.value = response.body()
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingPromotionsAndDisountsVisibility.value = false
        }

    }

}