package com.bignerdranch.android.wellnesspal.ui.newpet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentNewPetBinding
import com.bignerdranch.android.wellnesspal.databinding.FragmentPetInfoBinding
import com.bignerdranch.android.wellnesspal.ui.petinfo.PetInfoViewModel
import kotlinx.coroutines.launch

private const val TAG = "NewPetFragment"
class NewPetFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentNewPetBinding? = null
    private lateinit var newPetViewModel: NewPetViewModel
    private lateinit var colorDropdown : Spinner
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newPetViewModel =
            ViewModelProvider(this)[NewPetViewModel::class.java]
        _binding = FragmentNewPetBinding.inflate(inflater, container, false)
        colorDropdown = binding.collarChoices
        // populate collar color dropdown with choices
        ArrayAdapter.createFromResource(this.requireContext(), R.array.colors_array, android.R.layout.simple_spinner_item, )
            .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // apply adapter to spinner widget
            colorDropdown.adapter = adapter
                // set default selection
            colorDropdown.setSelection(0)

                // set up LiveData observer for current pet


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set event listener for submit button
        binding.submitNewPetButton.setOnClickListener {
            if (checkForInternet(view.context)) {
                val name = binding.petNameField.text.toString()
                if (name.length == 0 || name.length >= 15) {
                    Toast.makeText(
                        context,
                        "Name must be between 1 and 15 characters.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    newPetViewModel.writeNewPet(
                        binding.petNameField.text.toString(),
                        colorDropdown.selectedItem.toString()
                    )
                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(view.context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }

            // whenever a new selection is made, update the cat preview picture to contain the selected color
            colorDropdown.onItemSelectedListener = this
    }
// event handlers for when a new choice gets selected in the spinner.
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val selectedColor = parent?.getItemAtPosition(pos).toString()
        val petImage = binding.petPreviewImage
    when (selectedColor) {
        "Blue" -> {
            petImage.setImageResource(R.drawable.small_blue_happy)
        }
        "Purple" -> {
            petImage.setImageResource(R.drawable.small_purple_happy)
        }
        else -> {
            petImage.setImageResource(R.drawable.small_red_happy)
        }
    }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
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