package kg.buhara.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kg.buhara.data.model.ImageModel
import kg.buhara.data.model.ImagesResponseModel
import kg.buhara.data.remote.Api
import retrofit2.Call
import retrofit2.Callback
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
class ImagesRepo(private val imagesApi: Api) {

    fun fetchImages(data: MutableLiveData<ImagesResponseModel>) {
        imagesApi.getImages().enqueue(object : Callback<ImagesResponseModel> {
            override fun onFailure(call: Call<ImagesResponseModel>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }

            override fun onResponse(
                call: Call<ImagesResponseModel>,
                response: Response<ImagesResponseModel>
            ) {
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    data.value =
                        ImagesResponseModel(arrayListOf(ImageModel()))
                }
            }
        })
    }
}