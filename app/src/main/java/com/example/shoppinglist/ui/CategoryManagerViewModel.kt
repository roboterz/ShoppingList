package com.example.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.data.MyDatabase
import com.example.shoppinglist.data.entities.Category


class CategoryManagerViewModel(application: Application) : AndroidViewModel(application) {

//    var subCategory: MutableList<Category> = ArrayList()
//
//    var mainCategory: MutableList<Category> = ArrayList()
    //private var commonCategory: List<SubCategory> = ArrayList()
    var currentActiveMainCategory: Long = 0L

    private val myDao = MyDatabase.getDatabase(application).category()

    private var categoryList: List<Category> = ArrayList()


    fun loadCategory(){
        categoryList = myDao.getAll()

    }

    fun getMainCategoryList(selectMode: Boolean): List<Category>{

        val mainCategory : MutableList<Category> = ArrayList()

        // return Main Category
        for (i in categoryList.indices){
            if (categoryList[i].Category_ParentID == 0L){
                mainCategory.add(categoryList[i])
            }
        }

        if (selectMode) {
            // selected section
            mainCategory.add(0, Category(Category_Name = "SELECTED"))
        }

        // +Add item
        mainCategory.add(Category(Category_Name = "+ Add", Category_Completed = -1))

        // adjust arrow and show sub categories
        if (currentActiveMainCategory == 0L) currentActiveMainCategory = mainCategory[0].Category_ID

        return mainCategory.toList()
    }


    fun getSubCategoryList(parentID: Long): List<Category>{

        val subCategory: MutableList<Category> = ArrayList()

        // return Sub Category
        for (i in categoryList.indices){
            if (parentID == 0L){
                if (categoryList[i].Category_Completed == 1){
                    subCategory.add(categoryList[i])
                }
            }else{
                if (categoryList[i].Category_ParentID == parentID){
                    subCategory.add(categoryList[i])
                }
            }
        }

        if (parentID > 0L){
            // add "+Add" item
            subCategory.add(Category(Category_Name = "+ Add", Category_Completed = -1))
        }

        return subCategory.toList()
    }


    fun getCategory(cateID: Long): Category{
        return myDao.getRecordByID(cateID)
    }

    fun addCategory(mCategory: Category) {
        myDao.add(mCategory)
    }


    private fun insertCategory(category: List<Category>){
        myDao.insert(category)
    }


    fun deleteCategory( category: Category){
        myDao.delete(category)
    }

    fun getSizeOfSubCategory(parentID: Long): Int{
        return myDao.getSizeOfSubCategory(parentID)
    }

    fun saveShoppingList(){
        val cateList: MutableList<Category> = ArrayList()

        for (i in categoryList.indices){
            if (categoryList[i].Category_Completed == 1){
                categoryList[i].Category_Completed = 2
                cateList.add(categoryList[i])
            }
        }


        // save
        insertCategory(cateList.toList())
    }

    fun saveCheckItem(cateID: Long, checked: Boolean){

        val idx = categoryList.indexOfFirst { it.Category_ID == cateID }

        if (checked){
            categoryList[idx].Category_Completed = 1
        }else{
            categoryList[idx].Category_Completed = 0
        }
    }

}