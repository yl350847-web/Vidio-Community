package com.tototo.video_community.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.android.ext.koin.androidContext
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import com.tototo.video_community.data.repository.FakeHomeRepository
import com.tototo.video_community.features.main.home.HomeViewModel
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.features.search.SearchViewModel
import com.tototo.video_community.data.repository.FakeSubscriptionRepository
import com.tototo.video_community.features.main.subscription.SubscriptionViewModel
import com.tototo.video_community.data.local.SearchHistoryRepository
import com.tototo.video_community.data.local.ThemePreferenceRepository
import com.tototo.video_community.data.local.VideoPreferencesRepository
import com.tototo.video_community.data.repository.VideoRepository
import com.tototo.video_community.features.setting.SettingsViewModel
import com.tototo.video_community.ui.viewmodel.ThemeViewModel

val appModule = module {
    single { FakeHomeRepository() }
    single { FakeSearchRepository(get()) }
    single { FakeSubscriptionRepository(get()) }
    single { SearchHistoryRepository(androidContext()) }
    single { ThemePreferenceRepository(androidContext()) }
    single { VideoPreferencesRepository(androidContext()) }
    single { VideoRepository (androidContext()) }
}

val viewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SubscriptionViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}
