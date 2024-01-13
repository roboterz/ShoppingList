package com.example.shoppinglist.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.adapter.MainCategoryAdapter
import com.example.shoppinglist.adapter.SubCategoryAdapter
import com.example.shoppinglist.ADD_CATEGORY
import com.example.shoppinglist.EDIT_CATEGORY
import com.example.shoppinglist.KEY_CATEGORY
import com.example.shoppinglist.KEY_CATEGORY_SELECT_MODE
import com.example.shoppinglist.R
import com.example.shoppinglist.data.entities.Category
import com.example.shoppinglist.databinding.FragmentShoppingBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingListFragment: Fragment() {
    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private var _binding: FragmentShoppingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mainCategoryAdapter: MainCategoryAdapter? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingListViewModel = ViewModelProvider(this)[ShoppingListViewModel::class.java]
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)


        return binding.root
    }


    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fab button
        view.findViewById<FloatingActionButton>(R.id.fabCate)?.visibility  = View.VISIBLE

        val fab: View = view.findViewById(R.id.fabShopping)
        // todo change fab icon
        fab.setBackgroundResource(R.drawable.baseline_add_24)

        fab.setOnClickListener{
            // put data before switch
            this.setFragmentResult(
                KEY_CATEGORY, bundleOf(
                    KEY_CATEGORY_SELECT_MODE to true
                )
            )
            // switch to cate fragment
            view.findNavController().navigate(R.id.navigation_CateManagerFragment)
        }


        //---------------------------tool bar--------------------------------
        // toolbar
        binding.toolbarShoppingList.menu.findItem(R.id.action_clear).isVisible = true
        binding.toolbarShoppingList.menu.findItem(R.id.action_cate).isVisible = true


        // category manager menu
        binding.toolbarShoppingList.menu.findItem(R.id.action_cate).setOnMenuItemClickListener {

            findNavController().navigate(R.id.navigation_CateManagerFragment)

            true

        }
        //---------------------------tool bar--------------------------------




        // Load data
        shoppingListViewModel.loadCategory()


        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                view.findViewById<RecyclerView>(R.id.recyclerview_category_main).layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                //binding.recyclerviewCategoryMain.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mainCategoryAdapter = this.context?.let {
                    MainCategoryAdapter(object: MainCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(cateID: Long, addNew: Boolean) {
                            mainCategoryAdapter?.setList(shoppingListViewModel.getMainCategoryList())
                            showSubCategoryItems(cateID)
                        }

                        override fun onItemLongClick(cateID: Long, mainCategoryName: String, nextRowID: Long) {
                            // edit/delete
//                            if (!selectMode)
//                                manageCategory(EDIT_CATEGORY,cateID, 0L, mainCategoryName, nextRowID)
                        }
                    })
                }
                view.findViewById<RecyclerView>(R.id.recyclerview_category_main).adapter = mainCategoryAdapter

            }
        }.start()

        // Sub Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                view.findViewById<RecyclerView>(R.id.recyclerview_category_sub).layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                subCategoryAdapter = this.context?.let {
                    SubCategoryAdapter(object: SubCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(cateID: Long, parentID: Long, checkBox: Boolean) {

                            shoppingListViewModel.saveCheckItem(cateID)

                            // todo refresh sub category list


                        }

                        // long click: delete
                        override fun onItemLongClick(cateID: Long, name: String) {
                            // delete sub category
                            deleteCategory(cateID )
                        }


                    })
                }
                view.findViewById<RecyclerView>(R.id.recyclerview_category_sub).adapter = subCategoryAdapter
            }
        }.start()


    }



    override fun onResume() {
        super.onResume()

        val mainCate = shoppingListViewModel.getMainCategoryList()
        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {
                mainCategoryAdapter?.setList(mainCate)
            }
        }.start()

        // Sub Category Adapter
//        Thread {
//            this.activity?.runOnUiThread {
//                subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategoryList(categoryManagerViewModel.currentActiveMainCategory, selectMode))
//            }
//        }.start()

        showSubCategoryItems(shoppingListViewModel.currentActiveMainCategory)


//        // show title/*/**/*/
//        when (transactionTypeID){
//            TRANSACTION_TYPE_EXPENSE -> {
//                toolbar_category.setTitle(if (cateMode == EDIT_MODE) R.string.nav_title_category_expense_manage else R.string.nav_title_category_expense)
//            }
//            TRANSACTION_TYPE_INCOME -> {
//                toolbar_category.setTitle(if (cateMode == EDIT_MODE) R.string.nav_title_category_income_manage else R.string.nav_title_category_income)
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFragmentResult("selected_category", bundleOf("subCategory_Name" to sendString))

        //(activity as MainActivity).setNavBottomBarVisibility(View.VISIBLE)
        _binding = null
    }





    //------------------------------------------Private Function------------------------------------


    private fun showSubCategoryItems(parentID: Long) {

        //if (categoryManagerViewModel.currentActiveMainCategory != parentID){
            // main category item click
            //mainCategoryAdapter?.setList(categoryManagerViewModel.mainCategory)
            //binding.recyclerviewCategorySub.adapter = mainCategoryAdapter
            // show sub category
        Thread {
            this.activity?.runOnUiThread {
                subCategoryAdapter?.setList(shoppingListViewModel.getSubCategoryList(parentID))
            }
        }.start()


        shoppingListViewModel.currentActiveMainCategory = parentID

        //}
    }


    // add/edit main and sub category------------------
    // todo name must be unique
    private fun manageCategory(type: Int, cateID: Long = 0L, parentID:Long = 0L, name: String = "", nextRowID: Long = 0L) {
        val alert = AlertDialog.Builder(context)
        val editText = EditText(context)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        editText.isSingleLine = true
        editText.setText(name)
        //editText.imeOptions = EditorInfo.IME_ACTION_DONE



        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_confirm
            ) { _, _ -> //What ever you want to do with the value

                when (type) {
                    //add category
                    ADD_CATEGORY -> {
                        val cate = Category()
                        cate.Category_ParentID = parentID
                        cate.Category_Name = editText.text.toString().trim()

                        shoppingListViewModel.addCategory(cate)
                        // refresh
                        refreshMainCategory()
                    }
                    //edit category
                    EDIT_CATEGORY -> {
                        val cate = shoppingListViewModel.getCategory(cateID)
                        cate.Category_Name = editText.text.toString().trim()

                        shoppingListViewModel.addCategory(cate)
                        // refresh
                        refreshMainCategory()
                    }

                }

            }
            .setNegativeButton(R.string.msg_cancel
            ) { dialog, _ ->
                dialog.cancel()
            }

        // category edit mode with delete button
            if (type == EDIT_CATEGORY) {
                alert.setNeutralButton(R.string.msg_delete) { _, _ ->
                    deleteCategory(cateID, nextRowID)
                }
            }

            alert.show()
    }


    // delete category---------------------
    @SuppressLint("InflateParams")
    private fun deleteCategory(cateID: Long, nextRowID: Long = 0) {

        val dialogBuilder = AlertDialog.Builder(activity)

        val cate = shoppingListViewModel.getCategory(cateID)

        dialogBuilder.setMessage(getString(R.string.msg_content_category_delete) + " \"" + cate.Category_Name + "\"")
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_confirm)) { _, _ ->
                // delete record
                try {

                    if (cate.Category_ParentID == 0L){
                        if (shoppingListViewModel.getSizeOfSubCategory(cateID) > 0){
                            Toast.makeText(
                                context,
                                getString(R.string.msg_category_delete_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            shoppingListViewModel.deleteCategory(Category(Category_ID = cateID))
                            refreshMainCategory(true)
                            refreshSubCategory(nextRowID)
                        }

                    }else{
                        shoppingListViewModel.deleteCategory(Category(Category_ID = cateID))
                        refreshSubCategory(cate.Category_ParentID)
                    }


                } catch (e: SQLiteException) {
                    Toast.makeText(
                        context,
                        getString(R.string.msg_category_delete_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
            .setNegativeButton(getText(R.string.msg_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getText(R.string.title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }

    // refresh subCategory
    private fun refreshSubCategory(parentID: Long){
        subCategoryAdapter?.setList(shoppingListViewModel.getSubCategoryList(parentID))
    }

    // refresh MainCategory
    private fun refreshMainCategory(deleted: Boolean = false){
        if (deleted) mainCategoryAdapter?.setArrowAfterDelete()
        shoppingListViewModel.loadCategory()
        mainCategoryAdapter?.setList(shoppingListViewModel.getMainCategoryList())
        //recyclerview_category_main.adapter = mainCategoryAdapter

    }


}