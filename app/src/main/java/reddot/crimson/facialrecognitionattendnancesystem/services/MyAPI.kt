package reddot.crimson.facialrecognitionattendnancesystem.services

import okhttp3.MultipartBody
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import reddot.crimson.facialrecognitionattendnancesystem.data.UploadResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit


interface MyAPI {
    
    @Multipart
    @POST("api/faceimageupload/")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>
    
    companion object {
        operator fun invoke(): MyAPI {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build()
            
            return Retrofit.Builder()
                .baseUrl("https://9462-58-145-187-252.in.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(MyAPI::class.java)
        }
    }
}