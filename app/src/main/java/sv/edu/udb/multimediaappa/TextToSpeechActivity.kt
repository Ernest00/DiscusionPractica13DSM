package sv.edu.udb.multimediaappa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
class TextToSpeechActivity : AppCompatActivity() {
    private lateinit var tts: TextToSpeech
    private lateinit var Texto: TextView
    private lateinit var BtnPlay: Button
    private lateinit var BtnSave: Button
    private var numarch = 0
    private val STORAGE_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)
        Texto = findViewById(R.id.edtText2Speech)
        BtnPlay = findViewById(R.id.btnText2SpeechPlay)
        BtnSave = findViewById(R.id.btnText2SpeechSave)
        tts = TextToSpeech(this@TextToSpeechActivity, OnInit)
        BtnPlay.setOnClickListener(onClick)
        BtnSave.setOnClickListener(onClick)
    }
    private var OnInit = OnInitListener { status ->
        if (TextToSpeech.SUCCESS == status) {
            tts.language = Locale("spa", "ESP")
        } else {
            Toast.makeText(applicationContext, "TTS no disponible",
                Toast.LENGTH_SHORT).show()
        }
    }
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.btnText2SpeechPlay -> {
                tts.speak(Texto.text.toString(), TextToSpeech.QUEUE_ADD, null)
            }
            R.id.btnText2SpeechSave -> {
                checkPermisoStorage()
            }
        }
    }

    private fun checkPermisoStorage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Informacion", "Solicitud de permiso storage")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Log.d("Informacion", "Debe de dirigirse a ajustes del telefono")
                Toast.makeText(this, "Debe de dirigirse a ajustes del telefono",Toast.LENGTH_SHORT).show()
            } else {
//El usuario nunca ha aceptado ni rechazado,
// así que le pedimos que acepte el permiso.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_REQUEST_CODE
                )
            }
        } else {
            Log.d("Informacion", "Se ha concedido permiso al almacenamiento")
            tts.speak(Texto.text.toString(), TextToSpeech.QUEUE_ADD, null)
            val myHashRender = HashMap<String, String>()
            val Texto_tts = Texto.text.toString()
//Cada vez que guarde hara un nuevo archivo wav
            numarch = numarch + 1
            val destFileName: String =
                Environment.getExternalStorageDirectory()
                    .absolutePath + "/Download/tts" + numarch + ".wav"
            myHashRender[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = Texto_tts
            tts.synthesizeToFile(Texto_tts, myHashRender, destFileName)
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}