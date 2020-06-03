package kg.buhara.data.model

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

data class ReviewModel(
    var id: Int = 0,
    var restaurant: Int? = 0,
    var created_date: String? = "",
    var body: String? = "",
    var user: ProfileResponseModel? = null
)
