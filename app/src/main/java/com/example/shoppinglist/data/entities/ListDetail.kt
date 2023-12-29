package com.example.shoppinglist.data.entities



data class ListDetail(
    var id: Long = 0L,

    var cateID: Long = 0L,

    var name: String = "",

    var complete: Boolean = false,

    var note: String = ""
)
