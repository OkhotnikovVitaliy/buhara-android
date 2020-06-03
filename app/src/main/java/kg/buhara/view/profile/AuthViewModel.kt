package kg.buhara.view.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.R
import kg.buhara.data.model.AuthResponseModel
import kg.buhara.data.model.ProfileResponseModel
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

class AuthViewModel(val context: Context, private val authRepo: AuthorizationRepo) : ViewModel() {

    val coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)
    var successResponseLiveData = MutableLiveData<Event<Boolean>>()
    var successUpdateLiveData = MutableLiveData<Event<Boolean>>()
//    var profileResponseLiveData = MutableLiveData<ProfileResponseModel>()
    final val profileResponseLiveData: MutableLiveData<ProfileResponseModel> = MutableLiveData() // Note this needs to be MutableLiveData so that you can call setValue

    var errorMessageLiveData = MutableLiveData<Event<String>>()
    var loadingVisibility = MutableLiveData<Event<Boolean>>()

    fun auth(phone: String) {
//        coroutine.launch {
//            loadingVisibility.value = Event(true)
//            val response = authRepo.auth(phone)
//            if (response != null) {
//                if (response.isSuccessful) {
//                    response.body()?.let { saveToken(it) }
//                } else {
//                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
//                }
//            }
//            loadingVisibility.value = Event(false)
//        }

    }

    fun profileDetail() {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.fetchProfileDetail()
            if (response != null) {
                if (response.isSuccessful) {
                    profileResponseLiveData.postValue(response.body())
                } else {
                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
                }
            }
            loadingVisibility.value = Event(false)
        }
    }

    fun getprofileResponseLiveDate(): LiveData<ProfileResponseModel>{
        return profileResponseLiveData
    }

    fun profileUpdate(name: String, street: String, building: String, flat: String, birthDay: String, gender: String) {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.updateProfile(name, street, building, flat, birthDay, gender)
            if (response != null) {
                if (response.isSuccessful) {
                    response.body()?.let { saveUserInfo(it) }
                    fcmTokenRegister()
                } else {
                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
                }
            }
            loadingVisibility.value = Event(false)
        }
    }

    fun fcmTokenRegister() {
        FCMTokenUtils.deleteToken(context)
        val fcmToken = FCMTokenUtils.getTokenFromPrefs(context)
        Log.e("FCM", fcmToken)

        coroutine.launch {
            val response = authRepo.fcmTokenRegister(fcmToken)
            if (response != null) {
                if (response.isSuccessful) {
                    Log.e("TAG", "Succsess")
                } else {
                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
                }
                loadingVisibility.value = Event(false)
            }
        }
    }

    private fun saveToken(data: AuthResponseModel) {
        UserInfoPref.setUserToken(context, "Token ${data.token}")
        successResponseLiveData.value = Event(true)
    }

    private fun saveUserInfo(data: ProfileResponseModel) {
        UserInfoPref.setUserInfo(context, data)
        successUpdateLiveData.value = Event(true)
    }


}