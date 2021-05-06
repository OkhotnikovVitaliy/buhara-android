package kg.app.test_buhara.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kg.app.test_buhara.data.Cat
import kg.app.test_buhara.data.repositories.CatRepository
import kg.app.test_buhara.presentation.BaseViewModel
import kg.app.test_buhara.utils.SingleLiveEvent
import kg.app.test_buhara.utils.UseCaseResult
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel (private val catRepository: CatRepository) : BaseViewModel() {


    val showLoading = MutableLiveData<Boolean>()
    val catsList = MutableLiveData<List<Cat>>()
    val showError = SingleLiveEvent<String>()
    val navigateToDetail = SingleLiveEvent<String>()

    init {
        // Load cats when this ViewModel is instantiated.
        loadCats()
    }
    fun loadCats() {
        // Show progressBar during the operation on the MAIN (default) thread
        showLoading.value = true
        // launch the Coroutine
        launch {
            // Switching from MAIN to IO thread for API operation
            // Update our data list with the new one from API
            val result = withContext(Dispatchers.IO) { catRepository.getCatsList() }
            // Hide progressBar once the operation is done on the MAIN (default) thread
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success -> catsList.value = result.data
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
    }

    fun catClicked(imageUrl: String) {
        navigateToDetail.value = imageUrl

    }
}