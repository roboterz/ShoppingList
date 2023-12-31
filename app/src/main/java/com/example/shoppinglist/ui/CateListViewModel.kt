package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.WaitingList

class CateListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application)



    // LiveData
    fun getWaitingList(): LiveData<List<CateList>> {
        return myDao.cateList().getAll()
    }

    fun addRecord(cateList: CateList){
        myDao.cateList().add(cateList)
    }

    fun getRecord(cateID: Long): CateList{
        return myDao.cateList().getRecordByID(cateID)
    }

    fun saveShoppingList(cateIDList: List<Long>){

        var waitingList: MutableList<WaitingList> = ArrayList()

        for (i in cateIDList.indices){
            waitingList.add(WaitingList(cateID = cateIDList[i]))
        }
        myDao.waitingList().insertAll(waitingList)
    }

}

