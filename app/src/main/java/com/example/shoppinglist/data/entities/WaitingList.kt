package com.example.shoppinglist.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CateList::class,
        parentColumns = ["cateID"],
        childColumns = ["cateID"],
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )], indices = [Index(value = ["id"], unique = true)]
)

data class WaitingList(
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "ID")
    var id: Long = 0L,

    var cateID: Long = 0L,

    var complete: Boolean = false,

    var note: String = ""
) {
}