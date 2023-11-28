package com.bignerdranch.android.wellnesspal.ui.petinfo

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

class HelpFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_help, null)


            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            builder.setView(view)
                // Add action buttons.
                .setPositiveButton("OK"
                ) { dialog, id ->
                    dialog.dismiss()
                }
            builder.setMessage(
                "Click one of the three icons on the left of your Pet Info screen to log a meal, sleep," +
                        "or water drank.\n" +
                        "If you meet your set goal in any of the three categories, your pet's age will increase by 1, " +
                        "giving you incentive to meet your wellness goals.\n" +
                        "You can change your goals at any time on the Profile page.\n" +
                        "At age 3, your pet will reach middle age, and at age 6, it will reach old age.\n" +
                        "At age 9, your pet will be retired to the Archive, and you will be prompted to create a new pet upon returning to Pet Info.\n" +
                        "Objective: Collect as many Pets in your Archive as possible!"
            )

            builder.setTitle("How to Use Wellness Pal")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}