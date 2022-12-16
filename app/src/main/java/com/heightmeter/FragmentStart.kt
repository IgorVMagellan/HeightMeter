package com.heightmeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.heightmeter.databinding.FragmentStartBinding


class FragmentStart : Fragment() {

    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
        binding = FragmentStartBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setupBackButton()  // показ кнопки Назад слева вверху

        binding.imageButtonHelp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentStart_to_fragmentHelp12)
        }

        binding.imgButtonSettings.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentStart_to_fragmentSettings)
        }

        binding.imageButtonCamera.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragmentStart_to_cameraXFragment)
        }

    }

    // Кнопка Назад в APPBAR
    private fun setupBackButton() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

//    private fun clearStack() {
//
//        val fragmentmanager: FragmentManager = requireActivity().getSupportFragmentManager()
//        //supportFragmentManager
//            //AppCompatActivity().supportFragmentManager
//            //requireActivity().supportFragmentManager
//            //
//        var count: Int = fragmentmanager.backStackEntryCount
//        Toast.makeText(context, "count $count", Toast.LENGTH_SHORT).show()
//
//        while (count > 0) {
//            fragmentmanager.popBackStack()
//            count--
//        }

//        val fm = requireActivity().supportFragmentManager
//        for (i in 0 until fm.backStackEntryCount) {
//            fm.popBackStack()
//        }

//    }

}