package com.example.shoppinglist.data.entities

import androidx.room.*
import java.time.LocalDateTime

@Entity( indices = [Index(value = ["Category_ID", "Category_Name"], unique = true)]
)

data class Category(
    @PrimaryKey(autoGenerate = true)
    var Category_ID: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var Category_ParentID: Long = 0L,
    @ColumnInfo(defaultValue = "")
    var Category_Name: String = "",

    // 0: not selected  1: selected  2: not complete  3: completed
    @ColumnInfo(defaultValue = "0")
    var Category_Completed: Int = 0,

    @ColumnInfo(defaultValue = "")
    var note: String = ""
)
