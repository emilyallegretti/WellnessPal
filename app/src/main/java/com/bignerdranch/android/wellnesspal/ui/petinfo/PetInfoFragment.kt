package com.bignerdranch.android.wellnesspal.ui.petinfo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.MainActivity
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentPetInfoBinding
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase

private const val TAG = "PetInfoFragment"
class PetInfoFragment : Fragment() {

    private var _binding: FragmentPetInfoBinding? = null
    private lateinit var petInfoViewModel: PetInfoViewModel
    private lateinit var petRef : Query
    private lateinit var logsRef: Query
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference
    private lateinit var petListener: ValueEventListener
    private var userReference = database.child("users").child(auth.currentUser!!.uid)
    private var currPetKey: String =""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        petInfoViewModel =
            ViewModelProvider(this)[PetInfoViewModel::class.java]
        _binding = FragmentPetInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        petInfoViewModel.mostRecentLogDate.value = petInfoViewModel.getCurrentDate()
        Log.d(TAG, "OnCreateView() called")
        return root
    }

    override fun onDestroyView() {
        Log.d(TAG, "OnDestroyView() called")
        super.onDestroyView()

        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "OnViewCreated() called")
        petRef = userReference.child("pets").orderByChild("current").equalTo(true)

        petListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange called on current pet")
                if (snapshot.children.count() == 0) {
                    Log.d(TAG, "no children of pets node ")
                    findNavController().navigate(R.id.to_new_pet)
                } else {        // if the user currently has a pet, display its attributes on the screen
                    Log.d(TAG, "pet has children")
                    // we can use first() since only one pet at all times will have its current attribute set to true
                    val currPet = snapshot.children.first().getValue<Pet>()
                    currPetKey = snapshot.children.first().key.toString()
                    Log.d(TAG, "current pet: $currPet.toString()")
                    currPet?.let {
                        Log.d(TAG, "changing livedata values")
                        petInfoViewModel.petData.value=it
                        val petDataval = petInfoViewModel.petData.value.toString()
                        Log.d(TAG, "livedata pet value: $petDataval)")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Current Pets failed, log a message
                Log.w(TAG, "getCurrPets:onCancelled", databaseError.toException())

            }
        }
        petRef.addValueEventListener(petListener)
        // set pet mood based on amount of days since most recent log
        logsRef = userReference.child("logs").limitToLast(1)
        petInfoViewModel.addMostRecentLogEventListener(logsRef)
        petInfoViewModel.mostRecentLogDate.observe(viewLifecycleOwner) {

            val diff = petInfoViewModel.getDateDiff()
            Log.d(TAG, "observer fired for most recent log, $diff")
            // todo: change once debugging finishes
            if (diff.toInt() < 0) {
                petInfoViewModel.setMood("happy", currPetKey)
            } else if (diff.toInt() < 0)  {
                petInfoViewModel.setMood("irritated", currPetKey)
                Toast.makeText(context, "Your pet is irritated because you haven't submitted a log in 3 days! Make them happy again by submitting a log.", Toast.LENGTH_LONG).show()
            } else {
                petInfoViewModel.setMood("sad", currPetKey)
                Toast.makeText(context, "Your pet is sad because you haven't submitted a log in 7 days! Make them happy again by submitting a log.", Toast.LENGTH_LONG).show()
            }
        }



        // set up observer for current pet
        petInfoViewModel.petData.observe(viewLifecycleOwner) {
            Log.d(TAG, "observer fired for Pet")
            if (it != null) {
                binding.petName.text = it.name
                // build a filepath string for correct picture to set based on color, age, emotion
                lateinit var mood: String
                lateinit var size: String
                var color: String = if (it.color == "Blue") {
                    "blue"
                } else if (it.color == "Red") {
                    "red"
                } else {
                    "purple"
                }
                // TODO: change age to use case amounts when debugging is done
                // if age is not on a milestone, set corresponding picture
                // if age is on a milestone age (3,6,9), display screen and level up pet
                if (it.age!! < 3) {
                    size = "small"
                } else if (it.age >= 3 && it.age < 6) {
                    size = "medium"
                } else if (it.age >= 6 && it.age <= 9) {
                    size = "large"
                }


                // get mood attributes
                if (it.emotion == "happy") {
                    mood = "happy"
                } else if (it.emotion == "irritated") {
                    mood = "irritated"
                } else {
                    mood = "sad"
                }
                /* special cases occur when the pet turns 3, 6, or 9. in these cases, the user must
            be prompted to shake the screen to age up the pet.
            in the case where the pet turns 9, the pet will be taken to the archive. the user will be redirected to see their
            new pet in the archive.
           */
                if (it.age == 3) {
                    // require shake and then display medium sized happy version of pet, given color
                    Log.d(TAG, "birthdayOne is ${petInfoViewModel.petData.value?.birthdayThree}")

                    if (petInfoViewModel.petData.value?.birthdayOne == false){

                        Toast.makeText(context, "Your pet has reached middle age!", Toast.LENGTH_LONG)
                            .show()

                        petInfoViewModel.updateBirthday("One", currPetKey)

                        Log.d(TAG, "navigated to AgeUp")
                        findNavController().navigate(R.id.to_age_up)
                        mood = "happy"
                    }


                } else if (it.age == 6) {
                    // require shake and then display large sized happy version of pet, given color


                    if (petInfoViewModel.petData.value?.birthdayTwo == false){
                        Toast.makeText(context, "Your pet has reached old age!", Toast.LENGTH_LONG)
                            .show()
                        // go to the AgeUpFragment
                        petInfoViewModel.updateBirthday("Two", currPetKey)
                        findNavController().navigate(R.id.to_age_up)

                        mood = "happy"
                    }

                } else if (it.age == 9) {
                    // require shake and then redirect user to archive to see new pet

                    if (petInfoViewModel.petData.value?.birthdayThree == false){
                        Toast.makeText(
                            context,
                            "Your pet is graduating! See it in the Archive!",
                            Toast.LENGTH_LONG
                        ).show()

                        // go to the AgeUpFragment
                        petInfoViewModel.updateBirthday("Two", currPetKey)
                        findNavController().navigate(R.id.to_age_up)
                        mood = "happy"

                        // TODO
                        val bottomNavigationView =
                            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)

                        bottomNavigationView?.selectedItemId = R.id.navigation_gradPets
                        Log.d(TAG, "navigated to gradPets")
                        // set 'current' attribute to false
                        petInfoViewModel.updateCurrentFlag(currPetKey)
                        petInfoViewModel.petData.value = null
                    }
                }

                // finally display picture based on attributes
                setImage(color, size, mood)
            }
        }

        /*
        Set up observers to check if the user has met their goal for food logs, eat logs,
        or water logs. if the user's goal is equal to the amount logged on the same day,
        they will be notified that their pet
         */

        binding.apply {

            waterLogButton.setOnClickListener {
                findNavController().navigate(R.id.to_water_log)

            }

            foodLogButton.setOnClickListener {
                findNavController().navigate(R.id.to_food_log)
            }

            sleepLogButton.setOnClickListener {
                findNavController().navigate(R.id.to_sleep_log)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreate() called")
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy() called")
        //petRef.removeEventListener(ValueEventListener)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "OnAttach() called")

    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "OnDetach() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnStop() called")
        petRef.removeEventListener(petListener)
        logsRef.removeEventListener(petInfoViewModel.logsListener)

    }

    // dynamically set the pet image displayed based on the attributes received from the database.
    fun setImage(color: String, size:String, mood:String) {
        val image = binding.petImage
        //  display the drawable based on found attributes
        // todo: update image resources when new pics are added in
        if (mood == "happy") {
            if (size == "small") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_happy)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_happy)
                } else {
                    image.setImageResource(R.drawable.small_purple_happy)
                }
            } else if (size == "medium") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.med_blue_happy)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.med_red_happy)
                } else {
                    image.setImageResource(R.drawable.med_purple_happy)
                }

            } else if (size == "large"){
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_happy)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_happy)
                } else {
                    image.setImageResource(R.drawable.small_purple_happy)
                }
            }
        } else if (mood == "irritated") {
            if (size == "small") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_mad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_mad)
                } else {
                    image.setImageResource(R.drawable.small_purple_mad)
                }
            } else if (size == "medium") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_mad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_mad)
                } else {
                    image.setImageResource(R.drawable.small_purple_mad)
                }

            } else if (size == "large"){
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_mad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_mad)
                } else {
                    image.setImageResource(R.drawable.small_purple_mad)
                }
            }

        } else if (mood == "sad") {
            if (size == "small") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_sad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_sad)
                } else {
                    image.setImageResource(R.drawable.small_purple_sad)
                }
            } else if (size == "medium") {
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_sad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_sad)
                } else {
                    image.setImageResource(R.drawable.small_purple_sad)
                }

            } else if (size == "large"){
                if (color == "blue") {
                    image.setImageResource(R.drawable.small_blue_sad)
                } else if (color == "red") {
                    image.setImageResource(R.drawable.small_red_sad)
                } else {
                    image.setImageResource(R.drawable.small_purple_sad)
                }
            }
        }
    }

}
