package com.tototo.video_community

import android.app.Application
import com.tototo.video_community.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BiliTubeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BiliTubeApp)
            modules(appModule)
        }
    }
}