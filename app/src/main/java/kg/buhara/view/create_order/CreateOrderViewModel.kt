package kg.buhara.view.create_order

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.R
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.data.repository.AuthorizationRepo
import kg.buhara.data.repository.CreateOrderRepo
import kg.buhara.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


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

class CreateOrderViewModel(
    val context: Context,
    private val repo: CreateOrderRepo,
    private val authRepo: AuthorizationRepo
) :
    ViewModel(), CoroutineScope {

    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Event<Boolean>>()
    var profileResponseLiveData = MutableLiveData<Event<ProfileResponseModel?>>()
    val successCreateLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    val coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun profileDetail() {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.fetchProfileDetail()
            if (response != null) {
                if (response.isSuccessful) {
                    profileResponseLiveData.value = Event(response.body())
                }
            }
            loadingVisibility.value = Event(false)
        }
    }


    fun createOrder(
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
    ) {
        launch(Dispatchers.Main) {
            loadingVisibility.value = Event(true)
            val response = repo.createOrder(
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
                change,
                bonuses
            )
            if (response != null) {
                if (response.isSuccessful) {
                    successCreateLiveData.postValue(Event(true))
                } else {
                    errorMessageLiveData.postValue(context.getString(R.string.server_error))
                }
            }
            loadingVisibility.value = Event(false)
        }
    }


}