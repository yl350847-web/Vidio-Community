package com.tototo.video_community.data.repository

import android.content.Context
import com.tototo.video_community.model.VideoDto
import com.tototo.video_community.model.VideoListDto
import kotlinx.serialization.json.Json

class VideoRepository(
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getAll(): List<VideoDto> {
        val text = context.assets.open("videos.json").bufferedReader().use { it.readText() }
        return json.decodeFromString(VideoListDto.serializer(), text).videos
    }

    fun getById(id: String): VideoDto? {
        return getAll().firstOrNull { it.id == id }
    }
}