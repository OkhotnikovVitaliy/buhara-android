package kg.buhara.data.repository

import kg.buhara.data.model.cart.CartModel
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
class CartRepo(private val api: Api) {

    suspend fun fetchCarts(): Response<ArrayList<CartModel>>? {
        return try {
            api.fetchCarts()
        }catch (e:Exception){
            null
        }
    }

    suspend fun cartDeleteDish(idDish: Int): Response<ResponseBody>? {
        return try {
            api.cartDeleteDish(idDish)
        } catch (e:Exception){
            null
        }
    }

}