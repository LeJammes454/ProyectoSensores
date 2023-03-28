package com.example.proyectosensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private lateinit var ballImage: ImageView

    private lateinit var displayMetrics: DisplayMetrics
    private var ballWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        ballImage = findViewById(R.id.ballImage)
        displayMetrics = resources.displayMetrics
        ballWidth = ballImage.width
    }
    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            Log.d("ACCELEROMETER", "x: $x, y: $y")

            val newX = ballImage.translationX - x
            val newY = ballImage.translationY + y

            if (newX < 0) {
                ballImage.translationX = 0f
            } else if (newX + ballWidth > displayMetrics.widthPixels) {
                ballImage.translationX = (displayMetrics.widthPixels - ballWidth).toFloat()
            } else {
                ballImage.translationX = newX
            }

            if (newY < 0) {
                ballImage.translationY = 0f
            } else if (newY + ballWidth > displayMetrics.heightPixels) {
                ballImage.translationY = (displayMetrics.heightPixels - ballWidth).toFloat()
            } else {
                ballImage.translationY = newY
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // No necesitamos implementar esto
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }


}