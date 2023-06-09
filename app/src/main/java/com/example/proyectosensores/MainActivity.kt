package com.example.proyectosensores

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
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    //variable para el hilo
    private lateinit var handler: Handler

    //variables para los sensores
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    //variables para el balon
    private lateinit var ballImage: ImageView
    private var ballWidth = 0
    private var ballHeight = 0

    //variable para obtener el tamaño de la pantalla
    private lateinit var displayMetrics: DisplayMetrics

    //variable para las cordenadas x , y
    private var x = 0f
    private var y = 0f

    // variables para las porterias xd
    private var goalTopX: Float = 0f
    private var goalTopY: Float = 0f
    private var goalBottomX: Float = 0f
    private var goalBottomY: Float = 0f
    private var goalWidth: Float = 0f
    private var goalHeight: Float = 0f

    // variables para los goles anotados
    private var golSuperior = 0
    private var golInferior = 0

    lateinit var scoreSup : TextView
    lateinit var scoreInf : TextView

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

        scoreSup = findViewById<TextView>(R.id.scoreSuperior)
        scoreInf = findViewById<TextView>(R.id.scoreInferior)


        val dpw = displayMetrics.widthPixels - 120
        val dph = displayMetrics.heightPixels - 90


        // Actualizamos la posición inicial del balón para que inicie en la mitad de la pantalla
        val initialX = (dpw- ballWidth) / 2f
        val initialY = (dph - ballWidth) / 2f
        ballImage.translationX = initialX
        ballImage.translationY = initialY

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        handler = Handler()
        handler.post(UpdateBallPositionRunnable())
    }



    private inner class UpdateBallPositionRunnable : Runnable {

        // obtenemos las dimenciones de la pantalla y tambien calibramos manualmente xd
        val dpw = displayMetrics.widthPixels - 120
        val dph = displayMetrics.heightPixels - 90

        override fun run() {
            // codenadas del balon
            val newX = ballImage.translationX - x
            val newY = ballImage.translationY + y

            //logica para evitar que el balon se salga
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

            //logica para caundo anota gol
            if (newX >= goalTopX && newX <= goalTopX + goalWidth && newY <= goalHeight) {
                mensajelog("superior")
            } else if (newX >= goalBottomX && newX <= goalBottomX + goalWidth && newY >= dph - goalHeight - ballHeight) {
                mensajelog("inferior")
            }

            handler.postDelayed(this, 16) // Se vuelve a enviar el mensaje al hilo secundario
        }
        fun reset(){
            val initialX = (dpw - ballWidth) / 2f
            val initialY = (dph - ballWidth) / 2f
            ballImage.translationX = initialX
            ballImage.translationY = initialY
        }
        fun mensajelog(gol:String){
            if (gol=="superior"){
                golSuperior++
                Log.d("gol","¡Gol en la portería superior!")
                Log.d("Gol inferior", "Goles anotados$golSuperior")
                scoreSup.text = "superior $golSuperior"

            }else if(gol=="inferior"){
                golInferior++
                Log.d("gol","¡Gol en la portería inferior!")
                Log.d("Gol inferior", "Goles anotados$golInferior")
                scoreInf.text = "Inferior $golInferior"
            }
            reset()
        }
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
            val acceleracion = 2// aqui modificacmos la velocidad del balon
            x = event.values[0] * acceleracion
            y = event.values[1] * acceleracion
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se utiliza
    }

}
