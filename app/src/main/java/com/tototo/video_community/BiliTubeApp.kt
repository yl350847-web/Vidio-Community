package com.tototo.video_community

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.tototo.video_community.di.appModule
import com.tototo.video_community.di.viewModelModule

class BiliTubeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BiliTubeApp)
            modules(
                listOf(
                    appModule,
                    viewModelModule
                )
            )
        }
    }
}