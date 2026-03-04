package com.tototo.video_community.di

import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import com.tototo.video_community.ui.viewmodel.SharedViewModel

val appModule = module {
    // 在这里按需提供全局单例，例如网络、DataStore 等
    // 举例：single { SomeRepository(get()) }
}

val viewModelModule = module {
    viewModel { SharedViewModel() }
}