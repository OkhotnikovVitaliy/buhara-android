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

data class ProfileResponseModel(
    var name: String? = "",
    var phone_number: String? = "",
    var address: String? = "",
    var birth_date: String? = "",
    var bonuses: Int? = 0,
    var total_sum: Int? = 0,
    var gender: String? = "",
    var street: String? = "",
    var building: String? = "",
    var flat: String? = "",
    var user_status: UserStatus? = UserStatus()
)

data class UserStatus(
    var title: String? = "",
    var discount: Int? = 0,
    var status_image: String? = "",
    var status_image_panel: String? = ""
)

