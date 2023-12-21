package com.example.shoppinglist.data


import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Transaction
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.ListDetail
import com.example.shoppinglist.data.entities.WaitingList
import java.util.*



@Dao
interface WaitingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(waitingList: WaitingList)

    @Delete
    fun delete(waitingList: WaitingList)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM WaitingList WHERE id = :itemID")
    fun getRecordByID(itemID:Long): WaitingList

    @Transaction
    @Query("SELECT * FROM WaitingList")
    fun getAll(): List<WaitingList>

    @Transaction
    @Query("""
            SELECT WaitingList.id, WaitingList.cateID, CateList.name, WaitingList.complete, WaitingList.note
            FROM WaitingList, CateList
            WHERE WaitingList.cateID = CateList.cateID
            ORDER BY CateList.name
            """)
    fun getWaitingList(): LiveData<List<ListDetail>>
}



@Dao
interface CateListDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cateList: CateList)

    @Delete
    fun delete(cateList: CateList)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM CateList WHERE cateID = :itemID")
    fun getRecordByID(itemID:Long): CateList

    @Transaction
    @Query("SELECT * FROM CateList")
    fun getAll(): LiveData<List<CateList>>
}