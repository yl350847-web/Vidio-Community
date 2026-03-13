package com.tototo.video_community.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val id: String,
    val title: String,
    val coverUrl: String,
    val playUrl: String,
    val desc: String,
    val sources: Map<String, String> = emptyMap()
)

@Serializable
data class VideoListDto(
    val videos: List<VideoDto>
)