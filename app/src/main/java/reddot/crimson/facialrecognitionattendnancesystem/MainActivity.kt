package reddot.crimson.facialrecognitionattendnancesystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import reddot.crimson.facialrecognitionattendnancesystem.ui.AwsAuth
import reddot.crimson.facialrecognitionattendnancesystem.ui.OnPremisesAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val buttonAWS: Button = findViewById(R.id.btn_aws)
        val buttonPrem: Button = findViewById(R.id.btn_prem)
        
        buttonAWS.setOnClickListener{
            val awsIntent = Intent(this, AwsAuth::class.java)
            startActivity(awsIntent)
        }
        buttonPrem.setOnClickListener{
            val premIntent = Intent(this, OnPremisesAuth::class.java)
            startActivity(premIntent)
        }
    }
}