package kg.buhara.view.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.ReviewModel
import kg.buhara.data.repository.ReviewRepo
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

class ReviewViewModel(private val repo: ReviewRepo) : ViewModel() {

    var reviewsListLiveData = MutableLiveData<ArrayList<ReviewModel>>()
    var reviewLiveData = MutableLiveData<ReviewModel>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()

    fun fetchReviewsRestaurant(page: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchReviewsRestaurant(page)
            if (response != null) {
                if (response.isSuccessful) {
                    reviewsListLiveData.value = response.body()
                }
            }
            loadingVisibility.value = false
        }
    }

    fun createReview(id: Int, message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.createReview(message, id)
            if (response != null) {
                if (response.isSuccessful) {
                    reviewLiveData.value = response.body()
                }
            }
            loadingVisibility.value = false
        }
    }

}