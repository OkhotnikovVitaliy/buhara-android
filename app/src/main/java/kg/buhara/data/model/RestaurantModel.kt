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

data class RestaurantModel(
    var id: Int = 0,
    var title: String? = "",
    var card_title: String? = "",
    var description: String? = "",
    var address: String? = "",
    var start_time: String? = "",
    var end_time: String? = "",
    var phone: String? = "",
    var cover: String? = "",
    var logo: String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    var delivery_info: String? = "",
    var elsom: String? = "",
    var images: ArrayList<String>? = ArrayList()
):Serializable
