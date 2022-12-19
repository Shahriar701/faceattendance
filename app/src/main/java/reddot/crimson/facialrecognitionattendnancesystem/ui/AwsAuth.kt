package reddot.crimson.facialrecognitionattendnancesystem.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StorageUploadFileOptions
import reddot.crimson.facialrecognitionattendnancesystem.R
import java.io.File

class AwsAuth : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aws_auth)
//
//        try {
//            Amplify.configure(
//                AmplifyConfiguration.fromConfigFile(applicationContext, R.raw.awsconfiguration),
//                applicationContext
//            )
//            Log.i("MyAmplifyApp", "Initialized Amplify")
//        } catch (error: AmplifyException) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
//        }
        
        AWSMobileClient.getInstance()
            .initialize(applicationContext, object : Callback<UserStateDetails?> {
                override fun onResult(userStateDetails: UserStateDetails?) {
                    when (userStateDetails!!.userState) {
                        UserState.SIGNED_IN -> runOnUiThread {
                            Log.i("INIT", "Logged IN")
                        }
                        UserState.SIGNED_OUT -> runOnUiThread {
                            Log.i("INIT", "Logged OUT")
                        }
                        else -> AWSMobileClient.getInstance().signOut()
                    }
                }
                
                override fun onError(e: Exception?) {
                    Log.e("INIT", e.toString())
                }
            }
            )
    }
    
    private fun uploadFile() {
        val exampleFile = File(applicationContext.filesDir, "ExampleKey")
        exampleFile.writeText("Example file contents")
        
        val options = StorageUploadFileOptions.defaultInstance()
        Amplify.Storage.uploadFile("ExampleKey", exampleFile, options,
            { Log.i("MyAmplifyApp", "Fraction completed: ${it.fractionCompleted}") },
            { Log.i("MyAmplifyApp", "Successfully uploaded: ${it.key}") },
            { Log.e("MyAmplifyApp", "Upload failed", it) }
        )
    }
}