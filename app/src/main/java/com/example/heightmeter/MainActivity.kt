package com.example.heightmeter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences

    var personHeight: Int = 172  // рост измеряющего человека в сантиметрах
    var isSound: Boolean = true  // звук разрешен

    private val APP_PREFERENCES_PERSONHEIGHT = "personHeight"
    private val APP_PREFERENCES_ISSOUND = "isSound"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        prefs = getPreferences(Context.MODE_PRIVATE)

    }

    override fun onPause() {
        super.onPause()

        // Запоминаем данные
        val editor = prefs.edit()
        editor.putInt(APP_PREFERENCES_PERSONHEIGHT, personHeight).apply()
        editor.putBoolean(APP_PREFERENCES_ISSOUND, isSound).apply()
    }

    override fun onResume() {
        // читаем сохраненные настройки
        if (prefs.contains(APP_PREFERENCES_PERSONHEIGHT)) {
            // Получаем число из настроек
            personHeight = prefs.getInt(APP_PREFERENCES_PERSONHEIGHT, 170)
            // Выводим на экран данные из настроек
            //infoTextView.text = "Я указал $personHeight cm"
            //           Toast.makeText(applicationContext,"Я прочитал ${personHeight.toString()} см.", Toast.LENGTH_SHORT).show()
        }
        if (prefs.contains(APP_PREFERENCES_ISSOUND)) {
            // Получаем из настроек
            isSound = prefs.getBoolean(APP_PREFERENCES_ISSOUND, true)
//            Toast.makeText(
//                applicationContext,
//                "Я прочитал isSound= ${isSound.toString()} .",
//                Toast.LENGTH_SHORT
//            ).show()
        }
        super.onResume()
    }

    companion object {

        @JvmStatic
        fun newInstanceMain() = MainActivity().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
        }
    }

}

// Toast.makeText(context, "Сохранено   " + personHeight.toString(), LENGTH_SHORT).show()
//R.id.editTextNumberDecimal = personHeight.toString()
