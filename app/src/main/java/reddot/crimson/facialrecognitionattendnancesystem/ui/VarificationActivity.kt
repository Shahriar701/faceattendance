package reddot.crimson.facialrecognitionattendnancesystem.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import reddot.crimson.facialrecognitionattendnancesystem.R

class VarificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_varification)

        val tvResult = findViewById<TextView>(R.id.tv_result)

        val intent = intent
        val msg = intent.getStringExtra("msg")
        Log.i("upload5", msg.toString())
        tvResult.text = msg
    }
}