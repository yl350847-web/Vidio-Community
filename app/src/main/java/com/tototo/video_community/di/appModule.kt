package com.tototo.video_community.di

import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import com.tototo.video_community.ui.viewmodel.SharedViewModel
import com.tototo.video_community.data.repository.FakeHomeRepository
import com.tototo.video_community.features.main.home.HomeViewModel
import com.tototo.video_community.data.repository.FakeSearchRepository
import com.tototo.video_community.features.search.SearchViewModel

val appModule = module {
    single { FakeHomeRepository() }
    single { FakeSearchRepository() }
}

val viewModelModule = module {
    viewModel { SharedViewModel() }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}