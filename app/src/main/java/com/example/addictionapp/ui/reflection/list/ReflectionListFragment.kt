package com.example.addictionapp.ui.reflection.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.addictionapp.R
import com.example.addictionapp.data.models.Reflection
import com.example.addictionapp.ui.reflection.create.CreateReflectionActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_reflection_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReflectionListFragment : Fragment() {
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

        bindUI()

        createReflectionBtn.setOnClickListener {
            val intent = Intent(this.context, CreateReflectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindUI() {
        /**
        lifecycleScope.launch(Dispatchers.IO){
            viewModel.getAllReflections().observe(viewLifecycleOwner, Observer {
                initRecycler(it.toReflectionItem())
            })
        } **/

        viewModel.reflectionList.observe(viewLifecycleOwner, Observer {
            initRecycler(it.toReflectionItem())
        })

        /** Temporary implementation for Groupie recycler view
        val reflections: List<Reflection> = listOf(
        Reflection("15.05.2019","Good","Good session"),
        Reflection("12.06.2020","Bad","Bad session"),
        Reflection("10.02.2022","Worse","Worse session")
        ) **/
    }

    private fun initRecycler(reflectionListItems: List<ReflectionListItem>) {
        val groupieAdapter = GroupAdapter<GroupieViewHolder>().apply{
            spanCount = 2
            addAll(reflectionListItems)
        }

        reflectionList.apply {
            layoutManager = GridLayoutManager(this@ReflectionListFragment.context, groupieAdapter.spanCount).apply{
                spanSizeLookup = groupieAdapter.spanSizeLookup
            }
            adapter = groupieAdapter
        }
    }

    private fun List<Reflection>.toReflectionItem() : List<ReflectionListItem>{
        return this.map { reflection ->
            ReflectionListItem(reflection)
        }
    }
}