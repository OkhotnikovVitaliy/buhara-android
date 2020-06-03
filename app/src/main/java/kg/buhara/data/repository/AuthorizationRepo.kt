package kg.buhara.data.repository

import kg.buhara.data.model.AuthResponseModel
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.data.model.cart.CartCount
import kg.buhara.data.remote.Api
import okhttp3.ResponseBody
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
class AuthorizationRepo(private val api: Api) {

    // Fetch
    suspend fun signUp(phone: String): Response<ResponseBody>? {
        return try {
            api.signUp(phone)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun auth(phone: String, confirmation_code: String): Response<AuthResponseModel>? {
        return try {
            api.auth(phone, confirmation_code)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchProfileDetail(): Response<ProfileResponseModel>? {
        return try {
            api.fetchDetailProfile()
        } catch (e: Exception) {
            null
        }
    }


    suspend fun fetchCartCount(): Response<CartCount>? {
        return try {
            api.fetchCartCount()
        } catch (e: Exception) {
            null
        }
    }


    suspend fun fcmTokenRegister(fcmToken: String): Response<ResponseBody>? {
        return try {
            api.fcmTokenRegister(fcmToken)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateProfile(
        name: String,
        street: String,
        building: String,
        flat: String,
        birthDay: String,
        gender: String
    ): Response<ProfileResponseModel>? {
        return try {
            api.updateProfile(name, street, building, flat, birthDay, gender)
        } catch (e: Exception) {
            null
        }
    }


}