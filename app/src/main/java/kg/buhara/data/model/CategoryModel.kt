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

data class CategoryModel(
    var id: Int = 0,
    var title: String? = "",
    var cover: String? = "",
    var restaurant: Int? = 0,
    var child_categories: ArrayList<CategoryModel>? = ArrayList()
) : Serializable
