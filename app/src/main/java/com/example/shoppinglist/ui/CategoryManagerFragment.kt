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
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
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
import com.example.shoppinglist.databinding.FragmentCategoryManageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryManagerFragment: Fragment() {
    private lateinit var categoryManagerViewModel: CategoryManagerViewModel
    private var _binding: FragmentCategoryManageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mainCategoryAdapter: MainCategoryAdapter? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null


    private var selectMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setFragmentResultListener(KEY_CATEGORY){ _, bundle ->
            selectMode = bundle.getBoolean(KEY_CATEGORY_SELECT_MODE)

            if (selectMode){

                //binding.fabCate.visibility = View.VISIBLE
                view?.findViewById<FloatingActionButton>(R.id.fabCate)?.visibility  = View.VISIBLE
                //binding.fabCate.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_done_24))
//                view?.findViewById<FloatingActionButton>(R.id.fabCate)?.setOnClickListener {
//                    categoryManagerViewModel.saveShoppingList()
//                    // back to last fragment
//                    NavHostFragment.findNavController(this).navigateUp()
//                }
            }
            //Toast.makeText(context, receivedTransID.toString(),Toast.LENGTH_LONG).show()
        }


    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryManagerViewModel = ViewModelProvider(this)[CategoryManagerViewModel::class.java]
        _binding = FragmentCategoryManageBinding.inflate(inflater, container, false)



        return binding.root
    }


    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //---------------------------tool bar--------------------------------
        // choose items to show
        //toolbar_category.menu.findItem(R.id.action_edit).isVisible = true

        // toolbar
        binding.toolbarCateList.menu.findItem(R.id.action_shopping).isVisible = true

        // click the navigation Icon in the left side of toolbar
        binding.toolbarCateList.setNavigationOnClickListener{
            // call back button event to switch to previous fragment
            NavHostFragment.findNavController(this).navigateUp()
        }

        // shopping list menu
        binding.toolbarCateList.menu.findItem(R.id.action_shopping).setOnMenuItemClickListener {
            //findNavController().navigate(R.id.navigation_WaitingListFragment)

            // back to last fragment
            NavHostFragment.findNavController(this).navigateUp()

            true

        }
        //---------------------------tool bar--------------------------------



        // fab button
        view.findViewById<FloatingActionButton>(R.id.fabCate).setOnClickListener {
            categoryManagerViewModel.saveShoppingList()
            // back to last fragment
            NavHostFragment.findNavController(this).navigateUp()
        }


        // Load data
        categoryManagerViewModel.loadCategory()


        // Main Category Adapter
        Thread {
            this.activity?.runOnUiThread {

                view.findViewById<RecyclerView>(R.id.recyclerview_category_main).layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                //binding.recyclerviewCategoryMain.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)

                mainCategoryAdapter = this.context?.let {
                    MainCategoryAdapter(object: MainCategoryAdapter.OnClickListener {
                        // catch the item click event from adapter
                        override fun onItemClick(cateID: Long, addNew: Boolean) {
                            if (addNew){
                                manageCategory(ADD_CATEGORY, cateID)
                            }else{
                                mainCategoryAdapter?.setList(categoryManagerViewModel.getMainCategoryList(selectMode))
                                showSubCategoryItems(cateID)
                            }
                        }

                        override fun onItemLongClick(cateID: Long, mainCategoryName: String, nextRowID: Long) {
                            // edit/delete
                            if (!selectMode)
                                manageCategory(EDIT_CATEGORY,cateID, 0L, mainCategoryName, nextRowID)
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
                            if (cateID == 0L){
                                manageCategory(ADD_CATEGORY, cateID, categoryManagerViewModel.currentActiveMainCategory)
                            }else {
                                if (selectMode) {
                                    categoryManagerViewModel.saveCheckItem(cateID, checkBox)
                                    //Toast.makeText(context, "sdfdsfdsfdfdafdfd1111111",Toast.LENGTH_LONG).show()
                                }else
                                    // edit sub category
                                    manageCategory(EDIT_CATEGORY, cateID, parentID)
                                }
                            }

                        // long click: delete
                        override fun onItemLongClick(cateID: Long, name: String) {
                            if (!selectMode) {
                                // delete sub category
                                deleteCategory(cateID )
                            }
                        }


                    })
                }
                view.findViewById<RecyclerView>(R.id.recyclerview_category_sub).adapter = subCategoryAdapter
            }
        }.start()


    }



    override fun onResume() {
        super.onResume()

        val mainCate = categoryManagerViewModel.getMainCategoryList(selectMode)
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

        showSubCategoryItems(categoryManagerViewModel.currentActiveMainCategory)


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
                subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategoryList(parentID), selectMode)
            }
        }.start()


        categoryManagerViewModel.currentActiveMainCategory = parentID

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

                        categoryManagerViewModel.addCategory(cate)
                        // refresh
                        refreshMainCategory()
                    }
                    //edit category
                    EDIT_CATEGORY -> {
                        val cate = categoryManagerViewModel.getCategory(cateID)
                        cate.Category_Name = editText.text.toString().trim()

                        categoryManagerViewModel.addCategory(cate)
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

        val cate = categoryManagerViewModel.getCategory(cateID)

        dialogBuilder.setMessage(getString(R.string.msg_content_category_delete) + " \"" + cate.Category_Name + "\"")
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_confirm)) { _, _ ->
                // delete record
                try {

                    if (cate.Category_ParentID == 0L){
                        if (categoryManagerViewModel.getSizeOfSubCategory(cateID) > 0){
                            Toast.makeText(
                                context,
                                getString(R.string.msg_category_delete_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            categoryManagerViewModel.deleteCategory(Category(Category_ID = cateID))
                            refreshMainCategory(true)
                            refreshSubCategory(0L)
                        }

                    }else{
                        categoryManagerViewModel.deleteCategory(Category(Category_ID = cateID))
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
        subCategoryAdapter?.setList(categoryManagerViewModel.getSubCategoryList(parentID), selectMode)
    }

    // refresh MainCategory
    private fun refreshMainCategory(deleted: Boolean = false){
        if (deleted) mainCategoryAdapter?.setArrowAfterDelete()
        categoryManagerViewModel.loadCategory()
        mainCategoryAdapter?.setList(categoryManagerViewModel.getMainCategoryList(selectMode))
        //recyclerview_category_main.adapter = mainCategoryAdapter

    }


}