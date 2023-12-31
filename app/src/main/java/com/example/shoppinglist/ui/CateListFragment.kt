package com.example.shoppinglist.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.KEY_CATEGORY
import com.example.shoppinglist.KEY_CATEGORY_CATE_ID
import com.example.shoppinglist.KEY_CATEGORY_SELECT_MODE
import com.example.shoppinglist.R
import com.example.shoppinglist.adapter.CateListAdapter
import com.example.shoppinglist.adapter.WaitingListAdapter
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.databinding.FragmentCateBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CateListFragment : Fragment() {

    private var _binding: FragmentCateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var cateListViewModel: CateListViewModel

    private var listAdapter: CateListAdapter? = null

    private var editMode = false

    private var selectedList: MutableList<Long> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setFragmentResultListener(KEY_CATEGORY){ _, bundle ->
            editMode = bundle.getBoolean(KEY_CATEGORY_SELECT_MODE)

            if (editMode){
                //binding.fabCate.foreground = ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_done_24)
                binding.fabCate.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.baseline_done_24))
            }
            //Toast.makeText(context, receivedTransID.toString(),Toast.LENGTH_LONG).show()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        cateListViewModel =
            ViewModelProvider(this)[CateListViewModel::class.java]

        _binding = FragmentCateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /****** Adapter ********/

        binding.rvCateList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        listAdapter = this.context?.let {
            CateListAdapter(object : CateListAdapter.OnClickListener {
                // catch the item click event from adapter
                override fun onItemClick(cateID: Long, checked: Boolean) {
                    if (editMode) {
                        if (checked){
                            // selected
                            selectedList.add(cateID)
                        }else{
                            // select canceled
                            selectedList.remove(cateID)
                        }

                    }else{
                        // normal mode
                        showEditWindow(cateID)
                    }
                }

                override fun onItemLongClick(cateID: Long, name: String) {
                    // delete
                    deleteRecord(cateID, name)
                }
            })
        }
        binding.rvCateList.adapter = listAdapter


        // load list with LiveData
        cateListViewModel.getWaitingList().observe(viewLifecycleOwner, Observer { it ->
            listAdapter?.setList(it, editMode)
        })

        /**************************/



//        // toolbar
//        binding.toolbarCateList.menu.findItem(R.id.action_waiting).isVisible = true
        // click the navigation Icon in the left side of toolbar
        binding.toolbarCateList.setNavigationOnClickListener{
            // call back button event to switch to previous fragment
            NavHostFragment.findNavController(this).navigateUp()
        }

        // shopping list menu
        binding.toolbarCateList.menu.findItem(R.id.action_waiting).setOnMenuItemClickListener {
            //findNavController().navigate(R.id.navigation_WaitingListFragment)

            // back to last fragment
            NavHostFragment.findNavController(this).navigateUp()

            true

        }



        // fab button

        val fab: View = view.findViewById(R.id.fabCate)
        fab.setOnClickListener{
            if (editMode){
                // save
                saveShoppingList(selectedList)
                // back to last fragment
                NavHostFragment.findNavController(this).navigateUp()
            }else{
                showEditWindow()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showEditWindow(cateID: Long = 0L){
        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)

        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.title_category)

        // Edit mode
        if (cateID > 0L){
            editText.setText( cateListViewModel.getRecord(cateID).name )
        }

        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_save
            ) { _, _ -> //What ever you want to do with the value
                val cate = CateList()
                cate.cateID = cateID
                cate.name = editText.text.toString().trim()
                // save
                saveRecord(cate)

            }.setNegativeButton(R.string.msg_cancel
            ) { dialog, _ ->
                dialog.cancel()
            }

//                alert.setNeutralButton(R.string.msg_button_delete) { _, _ ->
//                    deleteCategory(MAIN_CATEGORY, rID, nextRowID)
//                }

        alert.show()
    }

    private fun saveRecord(cateList: CateList) : Int {

        if (cateList.name.isNotEmpty()) {
            cateListViewModel.addRecord(cateList)
        }

        return 0
    }

    private fun saveShoppingList(cateIDList: List<Long>){
        cateListViewModel.saveShoppingList(cateIDList)
    }

    private fun deleteRecord(cateID: Long, name: String) {

        val dialogBuilder = AlertDialog.Builder(activity)

        dialogBuilder.setMessage(getString(R.string.msg_delete) + " " +  name + " ?")
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_confirm)) { _, _ ->

                // delete record
                cateListViewModel.deleteRecord(cateID)

            }
            .setNegativeButton(getString(R.string.msg_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()
    }
}