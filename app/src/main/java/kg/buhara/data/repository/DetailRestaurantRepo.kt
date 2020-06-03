package kg.buhara.data.repository

import kg.buhara.data.model.*
import kg.buhara.data.remote.Api
import retrofit2.Response


/**
 * Created by farshid.abazari since 12/27/18
 *
 * Usage: Authentication repository class to handle auth tasks
 *
 * How to call: just create a single object in AppInjector and pass it to viewModel
 *
 * Context is a sample to see how you can pass arguments with Koin
 *
 */
class DetailRestaurantRepo(private val api: Api) {

    suspend fun fetchDetailRestaurant(id: Int): Response<RestaurantModel>? {
        return try {
            api.fetchDetailRestaurant(id)
        }catch (e:Exception){
            null
        }
    }

    suspend fun fetchPromotionsAndDiscounts(id: Int): Response<ArrayList<StockModel>>? {
        return try {
            api.fetchPromotionsAndDiscounts(id)
        }catch (e:Exception){
            null
        }
    }
}