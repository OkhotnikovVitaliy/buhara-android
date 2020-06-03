package kg.buhara.view.qr

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.R
import kg.buhara.data.model.AuthResponseModel
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.data.model.cart.CartCount
import kg.buhara.data.repository.AuthorizationRepo
import kg.buhara.utils.Event
import kg.buhara.utils.UserInfoPref
import kg.buhara.utils.gsm.FCMTokenUtils
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

class QRViewModel(val context: Context, private val authRepo: AuthorizationRepo) : ViewModel() {

    val coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)
    var qrResponseLiveData = MutableLiveData<Event<ProfileResponseModel?>>()
    var errorMessageLiveData = MutableLiveData<Event<String>>()
    var loadingVisibility = MutableLiveData<Event<Boolean>>()


    fun profileDetail() {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.fetchProfileDetail()
            if (response != null) {
                if (response.isSuccessful) {
                    qrResponseLiveData.value = Event(response.body())
                } else {
                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
                }
            }
            loadingVisibility.value = Event(false)
        }
    }


}