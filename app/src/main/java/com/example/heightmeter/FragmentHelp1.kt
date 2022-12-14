package com.example.heightmeter

import android.app.AlertDialog
import android.media.MediaActionSound
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.heightmeter.databinding.FragmentHelp1Binding


class FragmentHelp1 : Fragment() {
    lateinit var binding: FragmentHelp1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelp1Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgButtHelpNext.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentHelp1_to_fragmentHelp2)
        }

        binding.imgButtBack.setOnClickListener {
            //dialogAbout()
            Navigation.findNavController(it).navigate(R.id.action_fragmentHelp1_to_fragmentStart3)
        }

    }

        fun dialogAbout() {
            val sound = MediaActionSound()
            //sound.play(MediaActionSound.SHUTTER_CLICK)
            sound.play(MediaActionSound.FOCUS_COMPLETE) // не работает?

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Dialog")
            builder.setMessage("Покормить кота?")
            builder.setPositiveButton("OK", null)
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
}