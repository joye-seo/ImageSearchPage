package com.example.imagesearchpage.data.image

import com.example.imagesearchpage.data.video.Meta

data class ImageResponse(
    val documents: List<Document>,
    val meta: Meta
)