package kg.buhara.view.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.buhara.data.model.SettingsModel
import kg.buhara.data.repository.SettingsRepo
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

class SettingsViewModel(private val repo: SettingsRepo) : ViewModel() {

    var discountCondutionLiveData = MutableLiveData<SettingsModel>()
    var errorMessageLiveData = MutableLiveData<String>()
    var loadingVisibility = MutableLiveData<Boolean>()

    fun fetchDiscountCondution() {
        CoroutineScope(Dispatchers.Main).launch {
            loadingVisibility.value = true
            val response = repo.fetchDiscountCondution()
            if (response != null) {
                if (response.isSuccessful) {
                    discountCondutionLiveData.value = response.body()
                }
            }
            loadingVisibility.value = false
        }
    }

}