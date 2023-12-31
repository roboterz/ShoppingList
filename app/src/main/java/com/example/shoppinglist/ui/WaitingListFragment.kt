package com.example.shoppinglist.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.KEY_CATEGORY
import com.example.shoppinglist.KEY_CATEGORY_CATE_ID
import com.example.shoppinglist.KEY_CATEGORY_SELECT_MODE
import com.example.shoppinglist.R
import com.example.shoppinglist.adapter.WaitingListAdapter
import com.example.shoppinglist.data.entities.CateList
import com.example.shoppinglist.data.entities.ListDetail
import com.example.shoppinglist.data.entities.WaitingList
import com.example.shoppinglist.databinding.FragmentWaitingBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WaitingListFragment : Fragment() {

    private var _binding: FragmentWaitingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var waitViewModel: WaitingListViewModel
    private val binding get() = _binding!!
    private var listAdapter: WaitingListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        waitViewModel =
            ViewModelProvider(this)[WaitingListViewModel::class.java]

        _binding = FragmentWaitingBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        /****** Adapter ********/

        binding.rvWaitingList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        listAdapter = this.context?.let {
            WaitingListAdapter(object : WaitingListAdapter.OnClickListener {
                // catch the item click event from adapter
                override fun onItemClick(id: Long) {
                    // edit note
                    showEditWindow(id)
                }

                override fun onItemLongClick(id: Long, name: String) {
                    // delete
                    deleteRecord(id, name)
                }

                override fun onClickBoxClick(id: Long, checked: Boolean) {
                    // update clickBox
                    updateRecord(id, checked)
                }
            })
        }
        binding.rvWaitingList.adapter = listAdapter


        // load list with LiveData
        waitViewModel.getWaitingList().observe(viewLifecycleOwner, Observer { it ->
            listAdapter?.setList(it)
        })

        /**************************/


//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        // toolbar
        binding.toolbarWaitingList.menu.findItem(R.id.action_cate).isVisible = true

        // categories menu
        binding.toolbarWaitingList.menu.findItem(R.id.action_cate).setOnMenuItemClickListener {
            findNavController().navigate(R.id.navigation_CateListFragment)
            true
        }


        // fab button
        val fab: View = view.findViewById(R.id.fabWaiting)
        fab.setOnClickListener{

            // put data before switch
            this.setFragmentResult(
                KEY_CATEGORY, bundleOf(
                    KEY_CATEGORY_SELECT_MODE to true
                )
            )
            // switch to record fragment
            view.findNavController().navigate(R.id.navigation_CateListFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEditWindow(id: Long = 0L){
        if (id <= 0L){
            return
        }

        val alert = AlertDialog.Builder(context)
        val editText = EditText(activity)
        val titleView = View.inflate(context, R.layout.popup_title, null)
        val item = waitViewModel.getDetailRecord(id)

        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = item.name

        // Edit mode
        editText.setText( item.note)

        alert.setView(editText)
            .setCustomTitle(titleView)
            .setPositiveButton(R.string.msg_save
            ) { _, _ -> //What ever you want to do with the value

                var waitList = waitViewModel.getRecord(id)
                waitList.note =  editText.text.toString().trim()
                // save
                saveRecord(waitList)

            }.setNegativeButton(R.string.msg_cancel
            ) { dialog, _ ->
                dialog.cancel()
            }

//                alert.setNeutralButton(R.string.msg_button_delete) { _, _ ->
//                    deleteCategory(MAIN_CATEGORY, rID, nextRowID)
//                }

        alert.show()
    }


    private fun saveRecord(waitingList: WaitingList){
        waitViewModel.insertRecord(waitingList)
    }

    private fun updateRecord(id: Long, checked: Boolean){
        var waitingList = waitViewModel.getRecord(id)

        waitingList.complete = checked

        waitViewModel.insertRecord(waitingList)
    }

    private fun deleteRecord(id: Long, name: String): Boolean {

        val dialogBuilder = AlertDialog.Builder(activity)

        var deleteConfirmed = false

        dialogBuilder.setMessage(getString(R.string.msg_delete) + " " + name + " ?")
            .setCancelable(true)
            .setPositiveButton(getString(R.string.msg_confirm)) { _, _ ->

                // delete record
                waitViewModel.deleteRecord(id)
                deleteConfirmed = true
            }
            .setNegativeButton(getString(R.string.msg_cancel)) { dialog, _ ->
                // cancel
                dialog.cancel()
                deleteConfirmed = false
            }

        // set Title Style
        val titleView = layoutInflater.inflate(R.layout.popup_title,null)
        // set Title Text
        titleView.findViewById<TextView>(R.id.tv_popup_title_text).text = getString(R.string.title_prompt)

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.ic_baseline_delete_forever_24)
        alert.setCustomTitle(titleView)
        alert.show()

        return deleteConfirmed
    }
}