package com.tototo.video_community.di

import com.tototo.video_community.ui.viewmodel.SharedViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    // 定义：SharedViewModel 是单例，用它的构造函数创建
    singleOf(::SharedViewModel)
}