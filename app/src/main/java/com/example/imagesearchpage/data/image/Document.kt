package com.example.imagesearchpage.data.image

data class Document(
    val collection: String,
    val datetime: String,
    val display_sitename: String,
    val doc_url: String,
    val height: Int,
    val image_url: String,
    val thumbnail_url: String,
    val width: Int
){
    fun getFormatterTime(): String {
        val formatter = datetime.substring(0,19)
        return formatter
    }
}