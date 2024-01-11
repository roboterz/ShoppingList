package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.Category

class WaitingListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application).category()



    // LiveData
    fun getWaitingList(): List<Category> {
        return myDao.getAll()
    }

    fun insertRecord(waitingList: Category){
        myDao.add(waitingList)
    }

    fun getDetailRecord(id: Long): Category {
        return myDao.getRecordByID(id)
    }

    fun getRecord(id: Long): Category {
        return myDao.getRecordByID(id)
    }

    fun deleteRecord(id: Long){
        myDao.delete(Category(Category_ID = id))
    }
}

