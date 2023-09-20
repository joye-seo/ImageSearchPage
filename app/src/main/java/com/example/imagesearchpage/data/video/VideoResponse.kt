package com.example.imagesearchpage.data.video

import com.example.imagesearchpage.data.image.DocumentX

data class VideoResponse(
    val documents: List<DocumentX>,
    val ds: List<Any>,
    val g: List<Any>,
    val m: M,
    val meta: MetaX
)