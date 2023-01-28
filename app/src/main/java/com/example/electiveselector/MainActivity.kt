package com.example.electiveselector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
    }
    var time = 0L
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(
                this, "Press once again to exit",
                Toast.LENGTH_SHORT
            ).show()
            time = System.currentTimeMillis()
        }
    }
}