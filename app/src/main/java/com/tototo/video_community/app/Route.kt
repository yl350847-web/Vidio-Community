package com.tototo.video_community.app

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    // @Serializable 让路由参数能被 Navigation 自动序列化
    @Serializable
    // data object : 一个唯一的、不需要传参的、但需要具备数据类特性（如方便打印、比较）的单例对象
    // data 告诉这个类，主要作用就是存数据，实现更多方法，toString()，equals()，hashCode()，copy()等等
    // data 在这里加 data 是为了让编译器帮我们生成标准的“身份识别”能力，让导航系统能准确地认出它
    data object Login : Route() {
        @Serializable
        data object SMS : Route()

        @Serializable
        data object Password: Route()

        @Serializable
        data object QRCode: Route()
    }

    @Serializable
    data object HomeGraph : Route() {
        @Serializable
        data object Home : Route()

        @Serializable
        data object Mine : Route()

        @Serializable
        data object Dynamic : Route()

        @Serializable
        data object Subscription : Route()
    }

    @Serializable
    data object Splash : Route()

    @Serializable
    data object History : Route()

    @Serializable
    data object Playlist : Route()

    @Serializable
    data class PlaylistDetail(
        val cover: String,
        val title: String,
        val count: Int,
        val isPrivate: Boolean,
        val isToView: Boolean = true,
        val fid: Long? = null
    ) : Route()

    @Serializable
    data object DownloadManagement : Route()

    @Serializable
    data object Search : Route()

    @Serializable
    data object Settings : Route() {
        @Serializable
        data object MainSetting : Route()

        @Serializable
        data object VideoSetting : Route()

        @Serializable
        data object AudioSetting : Route()

        @Serializable
        data object PlaySetting : Route()
    }

    @Serializable
    data object Play : Route()

    @Serializable
    data object ImagesBrowser: Route()
}