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
class ReviewRepo(private val api: Api) {

    suspend fun fetchReviewsRestaurant(idRestaurant: Int): Response<ArrayList<ReviewModel>>? {
        return try {
            return api.fetchReviewsRestaurant(idRestaurant)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createReview(message: String, idRestaurant: Int): Response<ReviewModel>? {
        return try {
            return api.createReview(message, idRestaurant)
        } catch (e: Exception) {
            null
        }
    }

}