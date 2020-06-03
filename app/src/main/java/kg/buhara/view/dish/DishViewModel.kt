package kg.buhara.view.dish

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.CategoryModel
import kg.buhara.data.model.DishModel
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

class DishViewModel(val context: Context, private val repo: DishRepo) : ViewModel() {

    var popularDishesLiveData = MutableLiveData<ArrayList<DishModel>>()
    var restaurantDishesLiveData = MutableLiveData<Event<ArrayList<DishModel>?>>()
    var favoriteDishesLiveData = MutableLiveData<ArrayList<DishModel>>()
    var dishesLiveData = MutableLiveData<Event<ArrayList<DishModel>?>>()
    var dishLiveData = MutableLiveData<DishModel>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()
    var isChangeCartLiveData = MutableLiveData<Boolean>()
    var isFavourite = MutableLiveData<Boolean>()
    var isInternetConnect = MutableLiveData<Event<Boolean>>()

    fun fetchPopularDishes(idRestaurant: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchPopularDishes(idRestaurant)
            if (response != null) {
                if (response.isSuccessful) {
                    popularDishesLiveData.postValue(response.body())
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun fetchDishesByCategory(idCategory: Int, page: Int, q:String?) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchDishesByCategory(idCategory, page, q)
            if (response != null) {
                if (response.isSuccessful) {
                    dishesLiveData.value = Event(response.body()?.results)
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun fetchDishesByRestaurant(idRestaurant: Int, page: Int, q: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchDishesByRestaurant(idRestaurant, page, q)
            Log.e("SUCCESS", response.toString())
            if (response != null) {
                Log.e("SUCCESS", response.body()?.results?.size.toString())
                if (response.isSuccessful) {
                    restaurantDishesLiveData.postValue(Event(response.body()?.results))
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun fetchDish(idDish: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchDish(idDish)
            if (response != null) {
                if (response.isSuccessful) {
                    dishLiveData.postValue(response.body())
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }

    fun favoriteDishes() {
        isInternetConnect.value = Event(Connect.initIntertnet(context))
        if (!Connect.initIntertnet(context)) return
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchFavoriteDishes()
            if (response != null) {
                if (response.isSuccessful) {
                    favoriteDishesLiveData.value = response.body()
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }


    fun favoriteDish(idDish: Int) {
        isInternetConnect.value = Event(Connect.initIntertnet(context))
        if (!Connect.initIntertnet(context)) return
        CoroutineScope(Dispatchers.Main).launch {
//            loadingVisibility.value = true
            val response = repo.favouriteDish(idDish)
            if (response != null) {
                isFavourite.value = response.isSuccessful
            }
//            loadingVisibility.value = false
        }
    }

    fun cartUpdateDish(idDish: Int, quantity:Int) {
        CoroutineScope(Dispatchers.Main).launch {
//            loadingVisibility.value = true
            val response = repo.cartUpdateDish(idDish, quantity)
            if (response != null) {
                isFavourite.value = response.isSuccessful
                isChangeCartLiveData.value = response.isSuccessful
            }
//            loadingVisibility.value = false
        }
    }
}