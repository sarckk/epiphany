package com.example.addictionapp.ui.reflection.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.ui.reflection.detail.ReflectionDetailFragmentArgs
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_reflection_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReflectionListFragment : Fragment() {
    private val args: ReflectionListFragmentArgs by navArgs()
    private val viewModel by viewModel<ReflectionListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reflection_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(args.confirmationMsg != null){
            Toast.makeText(context, args.confirmationMsg, Toast.LENGTH_SHORT).show()
        }

        /**
        intent.getStringExtra("confirmation_msg")?.let{
            intent.removeExtra("confirmation_msg")
            val toast = Toast.makeText(this, it, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 210)
            toast.show()
        }
        **/

        // launch new Activity when LOG btn clicked
        createReflectionBtn.setOnClickListener {
            val action = ReflectionListFragmentDirections.actionReflectionListFragmentToWellbeingStateFragment()
            findNavController().navigate(action)
        }

        bindUIToViewModel()
    }

    private fun bindUIToViewModel() {
        /**
        lifecycleScope.launch(Dispatchers.IO){
            viewModel.getAllReflections().observe(viewLifecycleOwner, Observer {
                initRecycler(it.toReflectionItem())
            })
        } **/

        viewModel.reflectionList.observe(viewLifecycleOwner, Observer {reflections ->
            if(reflections.isNotEmpty()){
                noDataImg.visibility = View.GONE
                noDataText.visibility = View.GONE
                updateRecycler(reflections)
            } else{
               noDataImg.visibility = View.VISIBLE
               noDataText.visibility = View.VISIBLE
               reflectionRecyclerView.visibility = View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if(!it){
                loadingScreenLoadAll.visibility = View.GONE
            }
        })
    }


    private fun updateRecycler(reflectionList: List<Reflection>) {
        val reflectionListItems = reflectionList.toReflectionItem()
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply{
            spanCount = 2
            setOnItemClickListener { item, view ->
                val position = getAdapterPosition(item)
                val reflection = reflectionList[position]
                showDetail(reflection.dateCreated, view)
            }
            addAll(reflectionListItems)
        }

        reflectionRecyclerView.apply {
            layoutManager = GridLayoutManager(this@ReflectionListFragment.context, groupieAdapter.spanCount).apply{
                spanSizeLookup = groupieAdapter.spanSizeLookup
            }
            adapter = groupieAdapter
        }
    }

    private fun showDetail(dateCreated: String, view: View) {
        val action = ReflectionListFragmentDirections.actionReflectionListFragmentToReflectionDetailFragment(dateCreated)
        view.findNavController().navigate(action)
    }

    private fun List<Reflection>.toReflectionItem() : List<ReflectionListItem>{
        return this.map { reflection ->
            ReflectionListItem(reflection)
        }
    }
}