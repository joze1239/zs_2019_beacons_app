package si.inova.zimskasola

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity(), BeaconScanner.Listener {

    private var scanner = BeaconScanner(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun onStart() {
        super.onStart()
        scanner.start()
    }

    override fun onStop() {
        super.onStop()
        scanner.stop()
    }

    override fun onBeaconFound(data: String) {
        tvBeaconAttachment.text = data
    }

    override fun onBeaconLost(data: String) {
        tvBeaconAttachment.setText(R.string.unknown_location)
    }

}


