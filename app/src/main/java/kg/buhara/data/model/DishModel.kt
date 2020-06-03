package kg.buhara.data.model

import java.io.Serializable


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

data class DishModel(
    var id: Int? = 0,
    var title: String? = "",
    var price: Int? = 0,
    var weight: String? = "",
    var description: String? = "",
    var image: String? = "",
    var is_popular: Boolean? = false,
    var category: Int? = 0,
    var restaurant: Int? = 0,
    var is_favourite: Boolean? = false,
    var in_cart: Int? = 0
) : Serializable
