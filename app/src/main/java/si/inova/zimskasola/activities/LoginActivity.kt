package si.inova.zimskasola.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zimskasola.R
import si.inova.zimskasola.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

}