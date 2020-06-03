package kg.buhara.di

import kg.buhara.data.repository.CreateOrderRepo
import kg.buhara.view.restaurant.RestaurantViewModel
import kg.buhara.view.settings.SettingsViewModel
import kg.buhara.view.profile.AuthViewModel
import kg.buhara.view.auth.VerificationViewModel
import kg.buhara.view.cart.CartViewModel
import kg.buhara.view.create_order.CreateOrderViewModel
import kg.buhara.view.delivery.DeliveryViewModel
import kg.buhara.view.detail_restaurant.DetailRestaurantViewModel
import kg.buhara.view.dish.DishViewModel
import kg.buhara.view.main.MainActivityViewModel
import kg.buhara.view.main.MainViewModel
import kg.buhara.view.menu.MenuViewModel
import kg.buhara.view.qr.QRViewModel
import kg.buhara.view.review.ReviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by DAS since 4/9/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

val viewModelModule = module {
    viewModel { MainActivityViewModel(get()) }
    single { VerificationViewModel(androidContext(), get(), get()) }
    single { AuthViewModel(androidContext(), get()) }
    single { SettingsViewModel(get()) }
    single { RestaurantViewModel(androidContext(), get()) }
    single { DeliveryViewModel(androidContext(), get()) }
    single { DetailRestaurantViewModel(get()) }
    single { ReviewViewModel(get()) }
    single { MenuViewModel(get()) }
    single { MainViewModel(androidContext(), get()) }
    single { QRViewModel(androidContext(), get()) }
    single { DishViewModel(androidContext(),get()) }
    single { CartViewModel(androidContext(), get(), get()) }
    single { CreateOrderViewModel(androidContext(), get(), get()) }
}
