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
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import kotlin.math.log

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var ballImage: ImageView
    private lateinit var displayMetrics: DisplayMetrics
    private var ballWidth = 0
    private var ballHeight = 0
    private var x = 0f
    private var y = 0f
    private lateinit var handler: Handler
    private lateinit var goalTop: ImageView
    private lateinit var goalBottom: ImageView

    private var goalTopX: Float = 0f
    private var goalTopY: Float = 0f
    private var goalBottomX: Float = 0f
    private var goalBottomY: Float = 0f
    private var goalWidth: Float = 0f
    private var goalHeight: Float = 0f


    private inner class UpdateBallPositionRunnable : Runnable {
        override fun run() {
            val newX = ballImage.translationX - x
            val newY = ballImage.translationY + y

            val dpw = displayMetrics.widthPixels - 120
            val dph = displayMetrics.heightPixels - 90


            if (newX < 0) {
                ballImage.translationX = 0f
            } else if (newX + ballWidth > dpw) {
                ballImage.translationX = (dpw - ballWidth).toFloat()
            } else {
                ballImage.translationX = newX
            }

            if (newY < 0) {
                ballImage.translationY = 0f
            } else if (newY + ballWidth > dph) {
                ballImage.translationY = (dph - ballWidth).toFloat()
            } else {
                ballImage.translationY = newY
            }

            if (newX >= goalTopX && newX <= goalTopX + goalWidth && newY <= goalHeight) {
                Log.d("gol","¡Gol en la portería superior!")
            } else if (newX >= goalBottomX && newX <= goalBottomX + goalWidth && newY >= dph - goalHeight - ballHeight) {
                Log.d("gol","¡Gol en la portería inferior!")
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
        ballHeight = ballImage.height

        goalWidth = displayMetrics.widthPixels * 0.4f
        goalHeight = displayMetrics.heightPixels * 0.05f
        goalTopX = (displayMetrics.widthPixels - goalWidth) / 2
        goalTopY = 0f
        goalBottomX = goalTopX
        goalBottomY = displayMetrics.heightPixels - goalHeight


        // Actualizamos la posición inicial del balón para que inicie en la mitad de la pantalla
        val initialX = (displayMetrics.widthPixels - ballWidth) / 2f
        val initialY = (displayMetrics.heightPixels - ballWidth) / 2f
        ballImage.translationX = initialX
        ballImage.translationY = initialY

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
