package sv.edu.udb.multimediaappa
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class VideoURLActivity : AppCompatActivity() {
    private lateinit var mediacontrol: MediaController
    private lateinit var textVideo: TextView
    private lateinit var txtVideo: EditText
    private lateinit var video: VideoView
    private var TAG: String = "INFORMACION"
    private lateinit var btnVideo: Button
    private lateinit var btnPermiso: Button
    private val INTERNET_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_urlactivity)
        btnVideo = findViewById(R.id.idCargarVideo)
        textVideo = findViewById(R.id.textVideo)
        txtVideo = findViewById(R.id.txtUrl)

        btnVideo.setOnClickListener {
            this.checkPermisoInternet()

            textVideo.text = getString(R.string.mensajevideo)
            video = findViewById(R.id.video)
//Accediendo al archivo almacenado en nuestro dispositivo
            var f = File(
                Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Download/",
                "video.3gp"
            )

            if (txtVideo.text.isNullOrBlank()) {
                //IS empty
            }else{
                val videoUrl = txtVideo.text.toString() // Reemplaza con tu URL de video

                val uri: Uri = Uri.parse(videoUrl)
                video.setVideoURI(uri)
                mediacontrol = MediaController(this)
                video.setMediaController(mediacontrol)
                mediacontrol.show()
            }


        }
    }
    private fun checkPermisoInternet() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Informacion", "Solicitud de permiso de internet")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.INTERNET
                )
            ) {
                Log.d("Informacion", "Debe de dirigirse a ajustes del telefono")
                textVideo.text = "Debe de dirigirse a ajustes del telefono"
            } else {
//El usuario nunca ha aceptado ni rechazado,
// así que le pedimos que acepte el permiso.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.INTERNET),
                    INTERNET_REQUEST_CODE
                )
            }
        } else {
            Log.d("Informacion", "Se ha concedido permiso al internet")
            textVideo.text = "Se ha concedido permiso al almacenamiento"
            btnVideo!!.isEnabled = true
        }
    }
}