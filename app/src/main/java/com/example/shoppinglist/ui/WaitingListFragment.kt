package com.example.shoppinglist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.adapter.WaitingListAdapter
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}