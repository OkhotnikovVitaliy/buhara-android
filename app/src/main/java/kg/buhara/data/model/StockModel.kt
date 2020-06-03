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

data class StockModel(
    var id: Int = 0,
    var title: String? = "",
    var description: String? = "",
    var cover: String? = "",
    var expired: String? = "",
    var restaurant: String? = ""
):Serializable
