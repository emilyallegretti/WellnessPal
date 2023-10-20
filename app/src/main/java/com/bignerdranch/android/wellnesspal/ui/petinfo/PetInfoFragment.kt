package com.bignerdranch.android.wellnesspal.ui.petinfo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentPetInfoBinding

private const val TAG = "PetInfoFragment"
class PetInfoFragment : Fragment() {

    private var _binding: FragmentPetInfoBinding? = null
    private lateinit var petInfoViewModel: PetInfoViewModel
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

        val textView: TextView = binding.textPetInfo
        petInfoViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
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
            foodLogButton.setOnClickListener {
                findNavController().navigate(R.id.to_food_log)
            }

            waterLogButton.setOnClickListener {
                findNavController().navigate(R.id.to_water_log)
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
