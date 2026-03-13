package com.tototo.video_community.di

import com.tototo.video_community.data.local.SearchHistoryRepository
import com.tototo.video_community.data.local.ThemePreferenceRepository
import com.tototo.video_community.data.local.VideoPreferencesRepository
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.data.repository.FakeSubscriptionRepository
import com.tototo.video_community.data.repository.VideoRepository
import com.tototo.video_community.features.main.home.HomeViewModel
import com.tototo.video_community.features.main.subscription.SubscriptionViewModel
import com.tototo.video_community.features.search.SearchViewModel
import com.tototo.video_community.features.setting.SettingsViewModel
import com.tototo.video_community.ui.util.NetworkMonitor
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import com.tototo.video_community.ui.viewmodel.ThemeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { VideoRepository(androidContext()) }

    single { FakeSearchRepository(get()) }
    single { FakeSubscriptionRepository(get()) }

    single { SearchHistoryRepository(androidContext()) }
    single { ThemePreferenceRepository(androidContext()) }
    single { VideoPreferencesRepository(androidContext()) }

    single { NetworkMonitor(androidContext()) }
}

val viewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SubscriptionViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}