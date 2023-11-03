package com.bignerdranch.android.wellnesspal.ui.sleeplog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentSleepLogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
private const val TAG = "SleepLogFragment"

class SleepLogFragment : Fragment() {
    private var _binding: FragmentSleepLogBinding? = null
    private lateinit var sleepLogViewModel: SleepLogViewModel
    private val db = Firebase.database.reference
    val auth = FirebaseAuth.getInstance()
    lateinit var goalRef : DatabaseReference
    // get reference to all food logs
    private lateinit var logsQuery : Query
    private lateinit var currPetQuery : Query


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sleepLogViewModel =
            ViewModelProvider(this)[SleepLogViewModel::class.java]

        _binding = FragmentSleepLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSleepLog
        sleepLogViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        sleepLogViewModel.totalLogsCurrDateData.observe(viewLifecycleOwner) {
            Log.d(TAG, "total logs observer fired")
            val sleepGoal = sleepLogViewModel.goalData.value?.toInt()
            if (sleepGoal != null) {
                // display meals left to goal
                var hoursLeft = (sleepGoal - it)
                if (hoursLeft < 0) {
                    hoursLeft = 0
                }
                binding.sleepLeftToGoalText.text  = "Hours sleep left to reach goal: $hoursLeft"
                if (hoursLeft == 0) {
                    // TODO: UNCOMMENT WHEN DEBUGGING DONE
                    //waterLogViewModel.goalMet = true
                    binding.goalsMetNote.text = "Note: You have met your sleep goal for the day! You can still submit meal logs, but they won't count towards your pet's growth."
                }
            }


        }
        sleepLogViewModel.goalData.observe(viewLifecycleOwner) {
            binding.waterGoalText.text = "Sleep Goal: $it"
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = auth.currentUser?.uid
        // get refernce to eat goal
        goalRef = db.child("users/$uid/goal/sleepGoal")
        // get reference to all food logs
        logsQuery = db.child("users/$uid/logs").orderByChild("type").equalTo("sleep")
        currPetQuery = db.child("users/$uid/pets").orderByChild("current").equalTo(true).limitToFirst(1)

        sleepLogViewModel.addGoalEventListener(goalRef)
        sleepLogViewModel.addTotalLogsEventListener(logsQuery)
        sleepLogViewModel.addPetEventListener(currPetQuery)

        binding.apply {
            buttonSubmitSleepLog.setOnClickListener {
                val amt = fieldSleepLog.text.toString()
                // restrict user from entering 0 or empty string
                if (amt == "") {
                    Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG)
                } else {
                    //TODO: UNCOMMENT THIS BLOCK WHEN DEBUGGING IS DONE
//                if (amt.toInt() <= 0) {
                    //               Toast.makeText(context, R.string.non_positive_num_err, Toast.LENGTH_LONG)
//                } else {
                    sleepLogViewModel.writeNewSleepLog(amt)
                    // every time total meal count changes,
                    // check if the total amount of meals logged equals the daily goal
                    // if so, update the age of the pet
                    val goal = sleepLogViewModel.goalData.value?.toInt() ?: 0
                    val totalLogs = sleepLogViewModel.totalLogsCurrDateData.value?.toInt() ?: 0
                    Log.d(TAG, "total logs: $totalLogs, goal: $goal")
                    if (goal <= totalLogs + amt.toInt()) {//&& !foodLogViewModel.goalMet) {      //  TODO: UNCOMMENT WHEN DEBUGGING DONE
                        Log.d(TAG, "daily goal reached, incrementing age")
                        val petName = sleepLogViewModel.currPet.name
                        Toast.makeText(
                            context,
                            "$petName's age just increased by 1!",
                            Toast.LENGTH_SHORT
                        ).show()
                        sleepLogViewModel.incrementPetAge()
                    }
                    findNavController().popBackStack()
                    // }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        super.onDestroyView()
        currPetQuery.removeEventListener(sleepLogViewModel.currPetListener)
        goalRef.removeEventListener(sleepLogViewModel.goalListener)
        logsQuery.removeEventListener(sleepLogViewModel.logsListener)
        _binding = null
    }
}