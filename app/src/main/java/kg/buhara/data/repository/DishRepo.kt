package kg.buhara.data.repository

import android.util.Log
import kg.buhara.data.model.*
import kg.buhara.data.remote.Api
import okhttp3.ResponseBody
import retrofit2.Response


/**
 * Created by DAS since 12/27/18
 *
 * Usage: Authentication repository class to handle auth tasks
 *
 * How to call: just create a single object in AppInjector and pass it to viewModel
 *
 * Context is a sample to see how you can pass arguments with Koin
 *
 */
class DishRepo(private val api: Api) {

    suspend fun fetchPopularDishes(idRestaurant: Int): Response<ArrayList<DishModel>>? {
        return try {
            return api.fetchPopularDishes(idRestaurant)
        }catch (e:Exception){
            null
        }
    }

    suspend fun fetchDishesByCategory(idCategory: Int, page: Int, q:String?): Response<ApiResults<DishModel>>? {
        return try {
            return api.fetchDishesByCategory(idCategory, page, q)
        }catch (e:Exception){
            null
        }
    }

    suspend fun fetchDishesByRestaurant(idRestaurant: Int, page: Int, q:String?): Response<ApiResults<DishModel>>? {
        return try {
            return api.fetchDishesByRestaurant(idRestaurant, page, q)
        }catch (e:Exception){
            Log.e("EXEP", e.message)
            null
        }
    }

    suspend fun fetchDish(idDish: Int): Response<DishModel>? {
        return try {
            return api.fetchDish(idDish)
        }catch (e:Exception){
            null
        }
    }

    suspend fun favouriteDish(idDish: Int): Response<ResponseBody>? {
        return try {
            return api.favouriteDish(idDish)
        }catch (e:Exception){
            null
        }
    }

    suspend fun cartUpdateDish(idDish: Int, quantity:Int): Response<ResponseBody>? {
        return try {
            return api.cartUpdateDish(idDish, quantity)
        }catch (e:Exception){
            null
        }
    }

    suspend fun fetchFavoriteDishes(): Response<ArrayList<DishModel>>? {
        return try {
            return api.fetchFavoriteDishes()
        }catch (e:Exception){
            null
        }
    }

}