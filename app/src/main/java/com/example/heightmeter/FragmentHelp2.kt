package com.example.heightmeter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.heightmeter.databinding.FragmentHelp1Binding
import com.example.heightmeter.databinding.FragmentHelp2Binding
import com.example.heightmeter.databinding.FragmentStartBinding

class FragmentHelp2 : Fragment() {

    lateinit var binding: FragmentHelp2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelp2Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgButtonToStart.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentHelp2_to_fragmentStart)
        }

    }

}