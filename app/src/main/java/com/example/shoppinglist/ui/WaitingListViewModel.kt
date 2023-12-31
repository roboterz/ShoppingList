package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.ListDetail
import com.example.shoppinglist.data.entities.WaitingList

class WaitingListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application).waitingList()



    // LiveData
    fun getWaitingList(): LiveData<List<ListDetail>> {
        return myDao.getWaitingList()
    }

    fun insertRecord(waitingList: WaitingList){
        myDao.add(waitingList)
    }

    fun getDetailRecord(id: Long): ListDetail {
        return myDao.getDetailRecordByID(id)
    }

    fun getRecord(id: Long): WaitingList {
        return myDao.getRecordByID(id)
    }

    fun deleteRecord(id: Long){
        myDao.delete(WaitingList(id = id))
    }
}

