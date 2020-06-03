package kg.buhara.view.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kg.buhara.R
import kg.buhara.data.model.AuthResponseModel
import kg.buhara.data.model.ImagesResponseModel
import kg.buhara.data.model.ProfileResponseModel
import kg.buhara.data.repository.AuthorizationRepo
import kg.buhara.data.repository.ImagesRepo
import kg.buhara.utils.Event
import kg.buhara.utils.UserInfoPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


/**
 * Created DAS since 2019-11-14
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class VerificationViewModel(
    val context: Context,
    private val imagesRepo: ImagesRepo,
    private val authRepo: AuthorizationRepo
) : ViewModel() {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private val TAG = this::class.java.simpleName
    private var auth = FirebaseAuth.getInstance()

    val isSuccessVerifyCode: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private var storedVerificationId: String? = ""
    var loadingVisibility = MutableLiveData<Event<Boolean>>()
    var errorMessageLiveData = MutableLiveData<Event<String>>()
    var successCodeLiveData = MutableLiveData<Event<Boolean>>()

    private val coroutine: CoroutineScope = CoroutineScope(Dispatchers.Main)


    fun signUp(phone: String) {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.signUp(phone)
            if (response != null) {
                if (response.isSuccessful) {
//                    response.body()?.let { saveToken(it) }
                } else if (response.code() == 429) {
                    errorMessageLiveData.postValue(Event(context.getString(R.string.request_wait)))
                }else{
                    errorMessageLiveData.postValue(Event("Cannot send SMS"))
                }
            }
            loadingVisibility.value = Event(false)
        }

    }

    fun auth(phone: String, confirmation_code: String) {
        coroutine.launch {
            loadingVisibility.value = Event(true)
            val response = authRepo.auth(phone, confirmation_code)
            if (response != null) {
                if (response.isSuccessful) {
                    response.body()?.let { saveToken(it) }
                } else if (response.code() == 403){
                    errorMessageLiveData.postValue(Event(context.getString(R.string.confirm_code)))
                }else{
                    errorMessageLiveData.postValue(Event(context.getString(R.string.server_error)))
                }
            }
            loadingVisibility.value = Event(false)
        }

    }


    private fun saveToken(data: AuthResponseModel) {
        UserInfoPref.setUserToken(context, "Token ${data.token}")
        successCodeLiveData.value = Event(true)
    }

}