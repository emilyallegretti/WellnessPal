package com.bignerdranch.android.wellnesspal.ui.authenticate

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.R.id.email_reauth_field
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
                .setPositiveButton("Delete Account"
                ) { dialog, id ->
                    val email = view.findViewById<EditText>(email_reauth_field)
                    val pass = view.findViewById<EditText>(R.id.password_reauth_field)
                   auth?.reauthenticate(EmailAuthProvider.getCredential(email.text.toString(), pass.text.toString()))
                    // delete FirebaseUser from Authentication
                    auth?.delete()?.addOnSuccessListener {
                        //Toast.makeText(context, "Successfully deleted user", Toast.LENGTH_SHORT).show()

                    }?.addOnFailureListener {
                       // Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    Thread.sleep(1500)
                    startActivity(Intent(context, AuthActivity::class.java))
                }
                .setNegativeButton(R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.setTitle("This action requires reauthentication. Please sign in again.")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}