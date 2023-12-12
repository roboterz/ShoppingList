package com.example.shoppinglist.data.entities



data class ListDetial(
    val id: Long = 0L,

    val cateID: String = "",

    val name: String = "",

    val complete: Boolean = false,

    val note: String = ""
)
