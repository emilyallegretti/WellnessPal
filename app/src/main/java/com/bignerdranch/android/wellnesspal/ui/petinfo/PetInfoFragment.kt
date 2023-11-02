package com.bignerdranch.android.wellnesspal.ui.petinfo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentPetInfoBinding
import com.bignerdranch.android.wellnesspal.models.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

private const val TAG = "PetInfoFragment"
class PetInfoFragment : Fragment() {

    private var _binding: FragmentPetInfoBinding? = null
    private lateinit var petInfoViewModel: PetInfoViewModel
    private lateinit var petRef : DatabaseReference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference
    private var userReference = database.child("users").child(auth.currentUser!!.uid)

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
        // every time this fragment is created, check if the current user has any pets with 'current' attribute set to true.
        // if none are found, this means either they have just created an account or their last pet just aged into retirement, and a new pet needs to be created
        // if this is the case, navigate to a new pet creation screen
        petRef = userReference.child("pets")
        // TODO: add listener logic to viewmodel somehow
        petRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot == null) {
                    // if no pet data exists, navigate to pet creation fragment
                    findNavController().navigate(R.id.to_new_pet)
                } else {
                    val currPetQuery = userReference.child("pets").orderByChild("current").equalTo("true")
                    currPetQuery.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.children.count() == 0) {
                                findNavController().navigate(R.id.to_new_pet)
                            } else {        // if the user currently has a pet, display its attributes on the screen
                                // we can use first() since only one pet at all times will have its current attribute set to true
                                val currPet = snapshot.children.first().getValue<Pet>()
                                currPet?.let {
                                    petInfoViewModel.petData.value=it
                                }
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Current Pets failed, log a message
                            Log.w(TAG, "getCurrPets:onCancelled", databaseError.toException())

                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Pets failed, log a message
                Log.w(TAG, "getPet:onCancelled", databaseError.toException())

            }
        })

        // set up observer for current pet
        petInfoViewModel.petData.observe(viewLifecycleOwner) {
            // set picture corresponding to chosen collar color, age, emotion
            val image = binding.petImage
            if (it.color.toString() == "Blue") {
                image.setImageResource(R.drawable.)
            }
        }

        //val textView: TextView = binding.textPetInfo
//        petInfoViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
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
    }

}
