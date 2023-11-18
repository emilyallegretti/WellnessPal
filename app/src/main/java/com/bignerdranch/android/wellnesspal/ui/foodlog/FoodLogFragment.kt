package com.bignerdranch.android.wellnesspal.ui.foodlog

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.bignerdranch.android.wellnesspal.databinding.FragmentFoodLogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
private const val TAG = "FoodLogFragment"

class FoodLogFragment : Fragment() {
    private var _binding: FragmentFoodLogBinding? = null
    private lateinit var foodLogViewModel: FoodLogViewModel
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
        foodLogViewModel =
            ViewModelProvider(this)[FoodLogViewModel::class.java]

        _binding = FragmentFoodLogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFoodLog
        foodLogViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        foodLogViewModel.totalLogsCurrDateData.observe(viewLifecycleOwner) {
            Log.d(TAG, "total logs observer fired")
            val eatGoal = foodLogViewModel.goalData.value?.toInt()
            if (eatGoal != null) {
                // display meals left to goal
                var mealsLeft = (eatGoal - it)
                if (mealsLeft < 0) {
                    mealsLeft = 0
                }
                binding.mealsLeftToGoalText.text  =
                    getString(R.string.meals_left_to_reach_goal, mealsLeft.toString())
                if (mealsLeft == 0) {
                    // TODO: UNCOMMENT WHEN DEBUGGING DONE
                    //foodLogViewModel.goalMet = true
                    binding.goalsMetNote.text =
                        getString(R.string.goalsMetNote)
                }
            }


        }
        foodLogViewModel.goalData.observe(viewLifecycleOwner) {
            binding.eatGoalText.text = getString(R.string.meal_goal, it)
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
        goalRef = db.child("users/$uid/goal/eatGoal")
        // get reference to all food logs
        logsQuery = db.child("users/$uid/logs").orderByChild("type").equalTo("eat")
        currPetQuery = db.child("users/$uid/pets").orderByChild("current").equalTo(true).limitToFirst(1)

        foodLogViewModel.addGoalEventListener(goalRef)
        foodLogViewModel.addTotalLogsEventListener(logsQuery)
        foodLogViewModel.addPetEventListener(currPetQuery)

        binding.apply {
            buttonSubmitFoodLog.setOnClickListener {
                if (checkForInternet(view.context)) {
                    val amt = fieldFoodLog.text.toString()
                    if (amt == "") {
                        Toast.makeText(context, R.string.empty_field_err, Toast.LENGTH_LONG).show()
                    } else {
                        // restrict user from entering 0
                        //TODO: UNCOMMENT THIS BLOCK WHEN DEBUGGING IS DONE
//                if (amt.toInt() <= 0) {
//                    Toast.makeText(context, "Please enter a positive number.", Toast.LENGTH_LONG)
//                } else {
                        foodLogViewModel.writeNewFoodLog(amt)
                        // every time total meal count changes,
                        // check if the total amount of meals logged equals the daily goal
                        // if so, update the age of the pet
                        val goal = foodLogViewModel.goalData.value?.toInt() ?: 0
                        val totalLogs = foodLogViewModel.totalLogsCurrDateData.value?.toInt() ?: 0
                        Log.d(TAG, "total logs: $totalLogs, goal: $goal")
                        if (goal <= totalLogs + amt.toInt()) {//&& !foodLogViewModel.goalMet) {      //  TODO: UNCOMMENT WHEN DEBUGGING DONE
                            Log.d(TAG, "daily goal reached, incrementing age")
                            val petName = foodLogViewModel.currPet.name
                            Toast.makeText(
                                context,
                                "$petName's age just increased by 1!",
                                Toast.LENGTH_SHORT
                            ).show()
                            foodLogViewModel.incrementPetAge()
                        }
                        findNavController().popBackStack()
                        // }
                    }
                }
                else{
                    Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        currPetQuery.removeEventListener(foodLogViewModel.currPetListener)
        goalRef.removeEventListener(foodLogViewModel.goalListener)
        logsQuery.removeEventListener(foodLogViewModel.logsListener)
        _binding = null
    }
    //Code from geeks for geeks geeksforgeeks.com
    private fun checkForInternet(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork?:return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network)?:return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}