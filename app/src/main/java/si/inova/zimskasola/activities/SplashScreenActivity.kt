package si.inova.zimskasola.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.example.zimskasola.R
import com.google.firebase.auth.FirebaseAuth
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.IllegalStateException


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(true)
            .build()
        Fabric.with(fabric)

        setContentView(R.layout.activity_splash_screen)

        initView()
        scheduleSplashScreen()
    }





    private fun initView() {
        Glide.with(baseContext)
            .load(R.drawable.text_animation)
            .into(iv_app_logo);
    }


    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
            {
                // Check if user is signed in (non-null) and update UI accordingly.
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    starMainAc()
                } else {
                    startLoginAc()
                }
            },
            splashScreenDuration
        )
    }

    private fun getSplashScreenDuration() = 2500L


    private fun startLoginAc() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        finish()
    }

    private fun starMainAc() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        finish()
    }

}
