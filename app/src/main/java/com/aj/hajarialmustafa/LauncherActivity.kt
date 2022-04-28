package com.aj.hajarialmustafa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class LauncherActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher)
        Handler().postDelayed({
            // Start your app main activity
            startActivity(Intent(this,ItemDetailHostActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}