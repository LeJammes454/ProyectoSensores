package com.example.proyectosensores
import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.ImageView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var ballImage: ImageView
    private lateinit var displayMetrics: DisplayMetrics
    private var ballWidth = 0
    private var x = 0f
    private var y = 0f
    private lateinit var handler: Handler

    private inner class UpdateBallPositionRunnable : Runnable {
        override fun run() {
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

            handler.postDelayed(this, 16) // Se vuelve a enviar el mensaje al hilo secundario
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ballImage = findViewById(R.id.ballImage)
        displayMetrics = resources.displayMetrics
        ballWidth = ballImage.width

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        handler = Handler()
        handler.post(UpdateBallPositionRunnable())
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val acceleracion = 5
            x = event.values[0] * acceleracion
            y = event.values[1] * acceleracion
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se utiliza
    }
}
