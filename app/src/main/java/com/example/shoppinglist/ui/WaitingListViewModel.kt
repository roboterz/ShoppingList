package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.ListDetail

class WaitingListViewModel(application: Application) : AndroidViewModel(application) {


    private val myDao = MyDatabase.getDatabase(application).waitingList()



    // LiveData
    fun getWaitingList(): LiveData<List<ListDetail>> {
        return myDao.getWaitingList()
    }


}

