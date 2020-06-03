package kg.buhara.data.repository

import kg.buhara.data.model.AuthResponseModel
import kg.buhara.data.model.ProfileResponseModel
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
class CreateOrderRepo(private val api: Api) {

    suspend fun createOrder(
        user_name: String,
        order_phone: String,
        address: String,
        building: String,
        flat: String,
        entrance: String,
        floor: String,
        comment: String,
        cart_id: Int,
        pay_method: String,
        change: Int,
        bonuses: Int
    ): Response<ResponseBody>? {
        return try {
            api.createOrder(
                user_name,
                order_phone,
                address,
                building,
                flat,
                entrance,
                floor,
                comment,
                cart_id,
                pay_method,
                change)
        } catch (e: Exception) {
            null
        }
    }

}