package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bignerdranch.android.wellnesspal.R
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ReauthenticateFragment: DialogFragment() {
// This code borrowed heavily from https://developer.android.com/develop/ui/views/components/dialogs.
    val auth = Firebase.auth.currentUser
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_reauthenticate, null)


            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            builder.setView(view)
                // Add action buttons.
                .setPositiveButton(R.string.signin
                ) { dialog, id ->
                    val user = view.findViewById<Button>(R.id.username_reauth_field)
                   // auth?.reauthenticate(EmailAuthProvider.getCredential()
                }
                .setNegativeButton(R.string.cancel
                ) { dialog, id ->
                    getDialog()?.cancel()
                }
            builder.setTitle("This action requires reauthentication. Please sign in again.")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}