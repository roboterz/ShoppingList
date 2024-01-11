package com.example.shoppinglist.data


import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Transaction
import com.example.shoppinglist.data.entities.Category
import java.util.*



@Dao
interface CateDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(category: Category)

    @Delete
    fun delete(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg category: List<Category>)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Category WHERE Category_ID = :cateID")
    fun getRecordByID(cateID:Long): Category

    @Transaction
    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Transaction
    @Query("SELECT * FROM Category WHERE Category_ParentID = 0")
    fun getMainCategories(): List<Category>

    @Transaction
    @Query("SELECT * FROM Category WHERE Category_ParentID = :parentID ")
    fun getSubCategories(parentID: Long): List<Category>

    @Transaction
    @Query("SELECT * FROM Category WHERE Category_Completed > 0")
    fun getShoppingList(): List<Category>

    @Transaction
    @Query("SELECT * FROM Category WHERE Category_Completed = :status")
    fun getShoppingListByStatus(status: Int): List<Category>

    @Transaction
    @Query("SELECT COUNT() FROM Category WHERE Category_ParentID = :parentID")
    fun getSizeOfSubCategory(parentID: Long): Int
}