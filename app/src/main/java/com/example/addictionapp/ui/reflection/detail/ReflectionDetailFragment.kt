package com.example.addictionapp.ui.reflection.detail

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.addictionapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reflection_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReflectionDetailFragment : Fragment(){
    private val args: ReflectionDetailFragmentArgs by navArgs()
    private val viewModel by viewModel<ReflectionDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reflection_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // as per https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result
        super.onViewCreated(view, savedInstanceState)
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.reflectionDetailFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("confirmDelete")) {
                val delete = navBackStackEntry.savedStateHandle.get<Boolean>("confirmDelete");
                if(delete!!){
                    viewModel.deleteReflection(viewModel.reflectionItem.value!!)
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpToolbar()
        bindUIToViewModel()

        viewModel.fetchReflection(args.dateCreated)
    }

    private fun setUpToolbar(){
        detailToolbar.let{
            it.setTitle("Your reflection")
            it.setNavigationIcon(R.drawable.ic_back)
            it.setNavigationOnClickListener() {
                findNavController().navigateUp()
            }

            it.inflateMenu(R.menu.menu_reflection_detail)

            it.setOnMenuItemClickListener {menuItem ->
                when(menuItem.itemId) {
                    R.id.deleteReflection -> {
                        askConfirmDelete()
                        true
                    } else -> {
                    super.onOptionsItemSelected(menuItem)
                }
                }
            }
        }
    }

    private fun bindUIToViewModel(){
        viewModel.navToListView.observe(viewLifecycleOwner, Observer {
            if(it.getContentIfNotHandled() != null){
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

    private fun askConfirmDelete(){
        val action = ReflectionDetailFragmentDirections.actionReflectionDetailFragmentToReflectionConfirmDeleteDialogFragment()
        findNavController().navigate(action)
    }
}