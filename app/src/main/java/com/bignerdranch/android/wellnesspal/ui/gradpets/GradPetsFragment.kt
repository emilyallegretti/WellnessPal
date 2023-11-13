package com.bignerdranch.android.wellnesspal.ui.gradpets


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.wellnesspal.ArchiveListAdapter
import com.bignerdranch.android.wellnesspal.databinding.FragmentGradPetsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class GradPetsFragment : Fragment() {

    private var _binding: FragmentGradPetsBinding? = null
    lateinit var gradPetsQuery : Query
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference
    lateinit var gradPetsViewModel: GradPetsViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gradPetsViewModel =
            ViewModelProvider(this).get(GradPetsViewModel::class.java)

        _binding = FragmentGradPetsBinding.inflate(inflater, container, false)
        // set layout manager to 3-column grid
        binding.archiveRecyclerView.layoutManager = GridLayoutManager(context, 3)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: CHANGE THIS QUERY TO PROPER AGE WHEN DEBUGGING DONE
        gradPetsQuery = database.child("users").child(auth.currentUser!!.uid).child("pets").orderByChild("age").equalTo(9.0)
        gradPetsViewModel.addGradPetEventListener(gradPetsQuery)
        gradPetsViewModel.gradPetsData.observe(viewLifecycleOwner){
            binding.archiveRecyclerView.adapter = ArchiveListAdapter(it)
        }

    }

    override fun onStop() {
        super.onStop()
        gradPetsQuery.removeEventListener(gradPetsViewModel.GradPetListener)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}