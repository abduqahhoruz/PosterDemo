package uz.instat.posterdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joinposter.transport.PosterTransport
import com.joinposter.transport.server.PosterDevice
import uz.instat.posterdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG ="PosterTransport"
    private val NOTIFICATION_APP_NAME = "Mobilkassa"
    private val APP_ID = "2962"
    private val onMessage: (device: PosterDevice, message: String) -> Unit = { device, message ->
        runOnUiThread {
            Toast.makeText(this, "Message: $message\nFrom: ${device.ip}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(PosterTransport) {
            onMessage = this@MainActivity.onMessage
            onClose = { code, info, initByRemote ->
                Log.d(TAG, "Closed connection: $code $info $initByRemote")
            }
            onError = { error ->
                Log.d(TAG, "Error: ${error.message ?: error.toString()}")
            }
            onFinishedInit = {
                Log.d(TAG, "Library is ready")
            }
            onOpen = {
                hashMapOf("simple" to "test")
            }

            init(applicationContext, APP_ID, NOTIFICATION_APP_NAME)
        }

        binding.btnStart.setOnClickListener{
            val connected = PosterTransport.connectedDevices
            connected.forEach {
                it.sendMessage("{\"text\":\"Hello!\"}")
            }
            PosterTransport.start()
        }
    }


}