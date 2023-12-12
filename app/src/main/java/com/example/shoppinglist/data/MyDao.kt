package com.example.shoppinglist.data


import androidx.room.*
import androidx.room.Transaction
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.WaitingList
import java.util.*



@Dao
interface WaitingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(waitingList: WaitingList)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM WaitingList WHERE id = :itemID")
    fun getRecordByID(itemID:Long): WaitingList

    @Transaction
    @Query("SELECT * FROM WaitingList")
    fun getAll(): List<WaitingList>
}



@Dao
interface CateListDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cateList: CateList)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM CateList WHERE cateID = :itemID")
    fun getRecordByID(itemID:Long): CateList

    @Transaction
    @Query("SELECT * FROM CateList")
    fun getAll(): List<CateList>
}