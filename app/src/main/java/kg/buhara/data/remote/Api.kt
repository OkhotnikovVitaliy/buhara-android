package kg.buhara.data.remote

import kg.buhara.data.model.*
import kg.buhara.data.model.cart.CartCount
import kg.buhara.data.model.cart.CartModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by DAS since 11/14/19
 *
 * Usage: a interface to define end points
 *
 * How to call: just add in appInjector and repositoryImpl that you wanna use
 *
 */
interface Api {

    @GET("images/search?query=l")
    fun getImages(): Call<ImagesResponseModel>

    @FormUrlEncoded
    @POST("users/sign_up/")
    suspend fun signUp(@Field("phone_number") name: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST("users/auth/")
    suspend fun auth(
        @Field("phone_number") name: String,
        @Field("confirmation_code") confirmation_code: String
    ): Response<AuthResponseModel>

    @FormUrlEncoded
    @POST("users/device_register/")
    suspend fun fcmTokenRegister(@Field("registration_id") registration_id: String): Response<ResponseBody>

    @GET("users/profile_detail/")
    suspend fun fetchDetailProfile(): Response<ProfileResponseModel>

    @GET("cart/total_count/")
    suspend fun fetchCartCount(): Response<CartCount>

    @GET("buhara_settings/")
    suspend fun fetchDiscountCondution(): Response<SettingsModel>

    @GET("restaurants/list/")
    suspend fun fetchRestaurants(@Query("page") page: Int): Response<ApiResults<RestaurantModel>>

    @GET("restaurants/detail/{id}/")
    suspend fun fetchDetailRestaurant(@Path("id") id: Int): Response<RestaurantModel>

    @GET("restaurants/stocks/")
    suspend fun getAllPromotionsAndDiscounts(): Response<ArrayList<StockModel>>

    @GET("restaurants/stocks/")
    suspend fun fetchPromotionsAndDiscounts(@Query("restaurant") restaurantId: Int): Response<ArrayList<StockModel>>

    @FormUrlEncoded
    @PUT("users/profile_detail/")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("street") street: String,
        @Field("building") building: String,
        @Field("flat") flat: String,
        @Field("birth_date") birth_date: String,
        @Field("gender") gender: String
    ): Response<ProfileResponseModel>

    @FormUrlEncoded
    @POST("orders/create/")
    suspend fun createOrder(
        @Field("user_name") name: String,
        @Field("order_phone") order_phone: String,
        @Field("address") address: String,
        @Field("building") building: String,
        @Field("flat") flat: String,
        @Field("entrance") entrance: String,
        @Field("floor") floor: String,
        @Field("comment") comment: String,
        @Field("cart_id") cart_id: Int,
        @Field("pay_method") pay_method: String,
        @Field("change") change: Int): Response<ResponseBody>

    @GET("restaurants/reviews/")
    suspend fun fetchReviewsRestaurant(@Query("restaurant") restaurantId: Int): Response<ArrayList<ReviewModel>>

    @GET("restaurants/categories/")
    suspend fun fetchCategories(@Query("restaurant") restaurantId: Int): Response<ArrayList<CategoryModel>>

    @GET("restaurants/popular/dishes/")
    suspend fun fetchPopularDishes(@Query("restaurant") restaurantId: Int): Response<ArrayList<DishModel>>

    @GET("restaurants/dishes/")
    suspend fun fetchDishesByCategory(
        @Query("category") idCategory: Int, @Query("page") page: Int, @Query(
            "search"
        ) q: String?
    ): Response<ApiResults<DishModel>>

    @GET("restaurants/dishes/")
    suspend fun fetchDishesByRestaurant(
        @Query("restaurant") idCategory: Int, @Query("page") page: Int, @Query("search") q: String?
    ): Response<ApiResults<DishModel>>

    @GET("restaurants/dishes/{id}")
    suspend fun fetchDish(@Path("id") id: Int): Response<DishModel>

    @GET("restaurants/favourite/dishes/")
    suspend fun fetchFavoriteDishes(): Response<ArrayList<DishModel>>

    @FormUrlEncoded
    @POST("restaurants/favourite/dishes/")
    suspend fun favouriteDish(@Field("id") id: Int): Response<ResponseBody>

    @FormUrlEncoded
    @POST("cart/update/")
    suspend fun cartUpdateDish(
        @Field("id") id: Int,
        @Field("quantity") quantity: Int
    ): Response<ResponseBody>

    @DELETE("cart/update/")
    suspend fun cartDeleteDish(@Field("id") id: Int): Response<ResponseBody>

    @FormUrlEncoded
    @POST("restaurants/reviews/")
    suspend fun createReview(
        @Field("body") body: String,
        @Field("restaurant") restaurantId: Int
    ): Response<ReviewModel>

    @GET("cart/list/")
    suspend fun fetchCarts(): Response<ArrayList<CartModel>>


}