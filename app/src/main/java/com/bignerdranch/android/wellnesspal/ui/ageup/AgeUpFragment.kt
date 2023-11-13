package com.bignerdranch.android.wellnesspal.ui.ageup

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.wellnesspal.R
import com.bignerdranch.android.wellnesspal.databinding.FragmentAgeUpBinding
import com.bignerdranch.android.wellnesspal.databinding.FragmentPetInfoBinding
import kotlin.math.sqrt


private var TAG = "AgeUpFragment"
private const val SHAKE_THRESHOLD = 20.0
class AgeUpFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentAgeUpBinding? = null
    private lateinit var ageUpViewModel: AgeUpViewModel

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ageUpViewModel =
            ViewModelProvider(this)[AgeUpViewModel::class.java]
        _binding = FragmentAgeUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d(TAG, "OnCreateView() called")
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "OnViewCreated() called")

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val image = binding.ageUpPetImage
        image.setImageResource(R.drawable.baseline_cake_24)

        binding.shakeInstruction.text = "Shake your phone to age up!"

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "Shake detected")
            handleShake(event.values[0], event.values[1], event.values[2])
        }
    }

    private fun handleShake(x: Float, y: Float, z: Float) {
        val acceleration = sqrt(x * x + y * y + z * z)
        if (acceleration > SHAKE_THRESHOLD) {
            Toast.makeText(context, "Pet has aged up!!", Toast.LENGTH_LONG)
                .show()
            findNavController().popBackStack()
            Log.d(TAG, "navigated back to PetInfo")
        }else{
            Toast.makeText(context, "Keep shaking your phone!", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "accuracy changed")
    }




}