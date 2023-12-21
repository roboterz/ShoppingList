package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.CateList

class CateListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application).itemList()



    // LiveData
    fun getWaitingList(): LiveData<List<CateList>> {
        return myDao.getAll()
    }


}

