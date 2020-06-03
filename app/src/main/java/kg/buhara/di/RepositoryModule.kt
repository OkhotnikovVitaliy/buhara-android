package kg.buhara.di

import kg.buhara.data.repository.*
import kg.buhara.utils.LocationHandler
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

val repositoryModule = module {

    single { ImagesRepo(imagesApi = get()) }
    single { AuthorizationRepo(api = get()) }
    single { SettingsRepo(api = get()) }
    single { RestaurantRepo(api = get()) }
    single { DeliveryRepo(api = get()) }
    single { DetailRestaurantRepo(api = get()) }
    single { ReviewRepo(api = get()) }
    single { MenuRepo(api = get()) }
    single { DishRepo(api = get()) }
    single { CartRepo(api = get()) }
    single { CreateOrderRepo(api = get()) }

    single { LocationHandler() }
}