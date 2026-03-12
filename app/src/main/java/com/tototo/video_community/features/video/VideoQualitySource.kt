package com.tototo.video_community.features.video

import com.tototo.video_community.data.local.VideoQuality

data class QualitySource(
    val quality: VideoQuality,
    val url: String
)

object VideoQualitySource {
    fun sources(): List<QualitySource> = listOf(
        QualitySource(
            quality = VideoQuality.P360,
            url = "https://vjs.zencdn.net/v/oceans.mp4" // 360P 测试视频（国内CDN加速）
        ),
        QualitySource(
            quality = VideoQuality.P480,
            url = "https://media.w3.org/2010/05/sintel/trailer.mp4" // 480P 测试视频
        ),
        QualitySource(
            quality = VideoQuality.P720,
            url = "https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-720p.mp4" // 720P 测试视频
        ),
        QualitySource(
            quality = VideoQuality.P1080,
            url = "https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-1080p.mp4" // 1080P 测试视频
        )
    )
}