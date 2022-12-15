package com.heightmeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.heightmeter.databinding.FragmentSettingsBinding
//import android.content.SharedPreferences

class FragmentSettings : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentSettings_to_fragmentStart)

            val etnd = binding.editTextNumberDecimal.text.toString()

//            with (prefs.edit()) {
//                putInt(getString(R.id.editTextNumberDecimal.text), personHeight)
//                apply()
//            }

            Toast.makeText(context, "Сохранено $etnd (нет :)", LENGTH_SHORT).show()
        }


    }
}