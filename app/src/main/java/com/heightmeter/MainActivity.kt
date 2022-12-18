package com.heightmeter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


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

    // Обеспечивает переход назад по кнопке на APPBAR
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
//                Toast.makeText(this, "Back pressed", Toast.LENGTH_SHORT).show()
                Log.i(FragmentCameraX.TAG, "Magellan: Back pressed")
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                Log.i(FragmentCameraX.TAG, "Magellan: Main Activity KEYCODE_VOLUME_DOWN")

                !FragmentCameraX.isOffline  // возвратить true, если не нужно отдавать обработчику по-умолчанию
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}

// Toast.makeText(context, "Сохранено   " + personHeight.toString(), LENGTH_SHORT).show()
//R.id.editTextNumberDecimal = personHeight.toString()
