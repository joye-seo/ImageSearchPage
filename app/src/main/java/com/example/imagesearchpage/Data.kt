package com.example.imagesearchpage

object Data {
    val searchList = mutableListOf<Search>(
//        Search("이미지",null,"해2이"),
//        Search("이미지1",null,"해이"),
//        Search("이미지3",null,"해f이"),
//        Search("이미지4",null,"해g이"),
    )
    val favoriteList = mutableListOf<Search>()
}

data class Search(
    val type : String,
    val image : String?,
    val title : String,
    val date : String,
)