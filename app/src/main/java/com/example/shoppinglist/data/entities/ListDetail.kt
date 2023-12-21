package com.example.shoppinglist.data.entities



data class ListDetail(
    val id: Long = 0L,

    val cateID: Long = 0L,

    val name: String = "",

    val complete: Boolean = false,

    val note: String = ""
)
