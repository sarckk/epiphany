package com.example.addictionapp.ui.reflection.detail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.addictionapp.R
import java.lang.ClassCastException

class ReflectionConfirmDeleteDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Delete this reflection?")
                .setPositiveButton("Delete",
                    DialogInterface.OnClickListener { dialog, id ->
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("confirmDelete", true)
                        dismiss()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("confirmDelete", false)
                        dismiss()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("confirmDelete", false)
        super.onCancel(dialog)
    }
}