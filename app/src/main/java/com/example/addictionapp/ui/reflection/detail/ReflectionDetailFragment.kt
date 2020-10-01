package com.example.addictionapp.ui.reflection.detail

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.addictionapp.R
import kotlinx.android.synthetic.main.fragment_reflection_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReflectionDetailFragment : Fragment() {
    private val args: ReflectionDetailFragmentArgs by navArgs()
    private val viewModel by viewModel<ReflectionDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reflection_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpToolbar()
        bindUIToViewModel()

        viewModel.fetchReflection(args.dateCreated)
    }

    private fun setUpToolbar(){
        reflectionDetailToolbar.setNavigationIcon(R.drawable.ic_back)
        reflectionDetailToolbar.setNavigationOnClickListener() {
            it.findNavController().navigateUp()
        }

        reflectionDetailToolbar.inflateMenu(R.menu.menu_reflection_detail)

        reflectionDetailToolbar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId) {
                R.id.deleteReflection -> {
                    viewModel.deleteReflection(viewModel.reflectionItem.value!!)
                    true
                } else -> {
                    super.onOptionsItemSelected(menuItem)
                }
            }
        }
    }

    private fun bindUIToViewModel(){
        viewModel.deletedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this@ReflectionDetailFragment.findNavController().navigateUp()
            }
        })

        viewModel.reflectionItem.observe(viewLifecycleOwner, Observer {
            if(it == null){
                // deleted, return
                return@Observer
            }
            dateCreated.text = "Your reflection on ${it.dateCreated}"
            wellBeingRating.text = it.wellBeingRating
            whatElseText.text = it.whatElseText
        })
    }
}