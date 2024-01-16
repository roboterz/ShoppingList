package com.aerolite.shoppinglist.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aerolite.shoppinglist.data.MyDatabase
import com.aerolite.shoppinglist.data.entities.Category


class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

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

    fun getMainCategoryList(): List<Category>{

        val mainCategory : MutableList<Category> = ArrayList()

        // return Main Category
        for (i in categoryList.indices){

            if (categoryList[i].Category_ParentID == 0L){

                categoryList[i].countSub = getSubCategoryList(categoryList[i].Category_ID).count()
                if (categoryList[i].countSub > 0) {
                    mainCategory.add(categoryList[i])
                }
            }
        }


        // selected section
        mainCategory.add(0, Category(Category_Name = "ALL", countSub = getSubCategoryList(0L).count() ))


        // adjust arrow and show sub categories
        if (currentActiveMainCategory == 0L) currentActiveMainCategory = mainCategory[0].Category_ID

        return mainCategory.toList()
    }

    fun getSubCategoryList(parentID: Long): List<Category>{

        val subCategory: MutableList<Category> = ArrayList()


        // return Sub Category
        for (i in categoryList.indices){
            if (parentID == 0L){
                if (categoryList[i].Category_Completed >1){
                    subCategory.add(categoryList[i])
                }
            }else{
                if (categoryList[i].Category_ParentID == parentID && categoryList[i].Category_Completed > 1){
                    subCategory.add(categoryList[i])
                }
            }
        }

        subCategory.sortBy { it.Category_Completed }
        return subCategory.toList()
    }

    fun getCategory(cateID: Long): Category {
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

    fun clearCompletedList(){
        val cateList: MutableList<Category> = ArrayList()

        for (i in categoryList.indices){
            if (categoryList[i].Category_Completed == 3){
                categoryList[i].Category_Completed = 0
                cateList.add(categoryList[i])
            }
        }

        // save
        insertCategory(cateList.toList())
    }


    fun saveCheckItem(cateID: Long){
        val cate = categoryList.first() { it.Category_ID == cateID }
        when (cate.Category_Completed){
            2 -> cate.Category_Completed = 3
            3 -> cate.Category_Completed = 2
        }

        // save
        myDao.add(cate)
    }

}