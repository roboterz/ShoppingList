package com.example.shoppinglist.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(indices = [Index(value = ["cateID"], unique = true)])

data class CateList(
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var cateID: Long = 0L,

    var name: String = ""
)
