package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.CateList

class CateListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application).cateList()



    // LiveData
    fun getWaitingList(): LiveData<List<CateList>> {
        return myDao.getAll()
    }

    fun addRecord(cateList: CateList){
        myDao.add(cateList)
    }

    fun getRecord(cateID: Long): CateList{
        return myDao.getRecordByID(cateID)
    }
}

