package kg.app.test_buhara.data.repositories

import kg.app.test_buhara.data.Cat
import kg.app.test_buhara.data.CatApi
import kg.app.test_buhara.utils.UseCaseResult

// Number of results we want
const val NUMBER_OF_CATS = 30

interface CatRepository {
    // Suspend is used to await the result from Deferred
    suspend fun getCatsList(): UseCaseResult<List<Cat>>
}

class CatRepositoryImpl(private val catApi: CatApi) : CatRepository {
    override suspend fun getCatsList(): UseCaseResult<List<Cat>> {
        /*
         We try to return a list of cats from the API
         Await the result from web service and then return it, catching any error from API
         */
        return try {
            val result = catApi.getCats(limit = NUMBER_OF_CATS).await()
            UseCaseResult.Success(result)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}