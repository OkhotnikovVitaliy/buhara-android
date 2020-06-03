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

data class Address(
    var street: String? = "",
    var house: String? = "",
    var flat: String? = "",
    var entrance: String? = "",
    var floor: String? = ""
) : Serializable
