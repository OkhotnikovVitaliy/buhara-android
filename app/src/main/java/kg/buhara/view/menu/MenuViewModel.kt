package kg.buhara.view.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.CategoryModel
import kg.buhara.data.model.DishModel
import kg.buhara.data.model.RestaurantModel
import kg.buhara.data.repository.MenuRepo
import kg.buhara.data.repository.RestaurantRepo
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

class MenuViewModel(private val repo: MenuRepo) : ViewModel() {

    var categoriesLiveData = MutableLiveData<ArrayList<CategoryModel>>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()

    fun fetchCategories(idRestaurant: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchCategories(idRestaurant)
            if (response != null) {
                if (response.isSuccessful) {
                    categoriesLiveData.value = response.body()
                } else {
                    errorMessageLiveData.value = ""
                }
            }
            loadingVisibility.value = false
        }
    }
}