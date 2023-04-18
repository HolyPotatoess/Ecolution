package my.edu.tarumt.ecolution

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import my.edu.tarumt.ecolution.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        Handler().postDelayed(Runnable {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        },3000) //screens delay 5 seconds
    }
}