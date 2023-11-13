package com.bignerdranch.android.wellnesspal.ui.waterlog

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
import com.bignerdranch.android.wellnesspal.databinding.FragmentWaterLogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
private const val TAG = "WaterLogFragment"

class WaterLogFragment : Fragment() {
    private var _binding: FragmentWaterLogBinding? = null
    private lateinit var waterLogViewModel: WaterLogViewModel
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
        waterLogViewModel =
            ViewModelProvider(this)[WaterLogViewModel::class.java]

        _binding = FragmentWaterLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        waterLogViewModel.totalLogsCurrDateData.observe(viewLifecycleOwner) {
            Log.d(TAG, "total logs observer fired")
            val waterGoal = waterLogViewModel.goalData.value?.toInt()
            if (waterGoal != null) {
                // display meals left to goal
                var waterLeft = (waterGoal - it)
                if (waterLeft < 0) {
                    waterLeft = 0
                }
                binding.waterLeftToGoalText.text  =
                    getString(R.string.oz_water_left_to_reach_goal, waterLeft.toString())
                if (waterLeft == 0) {
                    // TODO: UNCOMMENT WHEN DEBUGGING DONE
                    //waterLogViewModel.goalMet = true
                    binding.goalsMetNote.text = getString(R.string.water_goal_note)
                }
            }


        }
        waterLogViewModel.goalData.observe(viewLifecycleOwner) {
            binding.waterGoalText.text = getString(R.string.waterGoal, it)
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
        goalRef = db.child("users/$uid/goal/waterGoal")
        // get reference to all food logs
        logsQuery = db.child("users/$uid/logs").orderByChild("type").equalTo("water")
        currPetQuery = db.child("users/$uid/pets").orderByChild("current").equalTo(true).limitToFirst(1)

        waterLogViewModel.addGoalEventListener(goalRef)
        waterLogViewModel.addTotalLogsEventListener(logsQuery)
        waterLogViewModel.addPetEventListener(currPetQuery)

        binding.apply {
            buttonSubmitWaterLog.setOnClickListener {
                val amt = fieldWaterLog.text.toString()
                // restrict user from entering 0 or empty string
                if (amt == "") {
                    Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG)
                } else {
                    //TODO: UNCOMMENT THIS BLOCK WHEN DEBUGGING IS DONE
//                if (amt.toInt() <= 0) {
                    // Toast.makeText(context, R.string.non_positive_num_err, Toast.LENGTH_LONG).show()
//                } else {
                    waterLogViewModel.writeNewWaterLog(amt)
                    // every time total meal count changes,
                    // check if the total amount of meals logged equals the daily goal
                    // if so, update the age of the pet
                    val goal = waterLogViewModel.goalData.value?.toInt() ?: 0
                    val totalLogs = waterLogViewModel.totalLogsCurrDateData.value?.toInt() ?: 0
                    Log.d(TAG, "total logs: $totalLogs, goal: $goal")
                    if (goal <= totalLogs + amt.toInt()) {//&& !foodLogViewModel.goalMet) {      //  TODO: UNCOMMENT WHEN DEBUGGING DONE
                        Log.d(TAG, "daily goal reached, incrementing age")
                        val petName = waterLogViewModel.currPet.name
                        Toast.makeText(
                            context,
                            "$petName's age just increased by 1!",
                            Toast.LENGTH_SHORT
                        ).show()
                        waterLogViewModel.incrementPetAge()
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
        currPetQuery.removeEventListener(waterLogViewModel.currPetListener)
        goalRef.removeEventListener(waterLogViewModel.goalListener)
        logsQuery.removeEventListener(waterLogViewModel.logsListener)
        _binding = null
    }
}