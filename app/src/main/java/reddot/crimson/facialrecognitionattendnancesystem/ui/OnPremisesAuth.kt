package reddot.crimson.facialrecognitionattendnancesystem.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import reddot.crimson.facialrecognitionattendnancesystem.*
import reddot.crimson.facialrecognitionattendnancesystem.data.UploadResponse
import reddot.crimson.facialrecognitionattendnancesystem.services.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class OnPremisesAuth : AppCompatActivity(), UploadRequestBody.UploadCallback{
    
    val viewModel: FileViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    lateinit var ivCamera: ImageView
    lateinit var btnUpload: Button
    private val CAMERA_REQUEST_CODE = 100
    lateinit var photoFile: File
    lateinit var layoutRoot: View
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_premessis_auth)
        
        val dir: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/Folder/"
        val newdir = File(dir)
        newdir.mkdirs()
        
        btnUpload = findViewById(R.id.btn_upload)
        ivCamera = findViewById(R.id.iv_camera)
        layoutRoot = findViewById<View>(R.id.layout_root)
        progressBar = findViewById(R.id.progress_bar)
        
        btnUpload.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = createImageFileInAppDir()
            
            
            photoFile.also { file ->
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.android.provider",
                    file
                )
                selectedImageUri = photoURI
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }
    
    @Throws(IOException::class)
    private fun createImageFileInAppDir(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(imagePath, "JPEG_${timeStamp}_" + ".jpg")
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                selectedImageUri?.let { uri ->
                    Log.i("selectedImageUri", uri.toString())
                }
                var bitMap: Bitmap? = null
                try {
                    bitMap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                selectedImageUri!!
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    }
                } catch (e: Exception) {
                    Log.e("exception", e.toString())
                }
                
                Log.i("selectedImageUri", selectedImageUri.toString())
                ivCamera.setImageBitmap(bitMap)
    
                uploadImage(bitMap)
            }
        }
    }
    
    private fun uploadImage(bitmap: Bitmap?) {
        if (selectedImageUri == null) {
            layoutRoot.snackbar("Select an Image First")
            return
        }
        
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
        
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
    
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        
        val options = BitmapFactory.Options()
        options.inSampleSize = 2 //4, 8, etc. the more value, the worst quality of image
        
        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layoutRoot.snackbar(t.message!!)
                Log.i("upload", t.message!!)
                progressBar.progress = 0
            }
            
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    Log.i("upload", it.message)
                    layoutRoot.snackbar(it.message)
                    progressBar.progress = 100
                }
            }
        })
        
    }
    
    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }
}