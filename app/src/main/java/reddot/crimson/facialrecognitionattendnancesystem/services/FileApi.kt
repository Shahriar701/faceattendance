//package reddot.crimson.facialrecognitionattendnancesystem.services
//
//import okhttp3.MultipartBody
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Multipart
//import retrofit2.http.POST
//import retrofit2.http.Part
//
//interface FileApi {
//    @Multipart
//    @POST("")
//    suspend fun uploadImage(
//        @Part image: MultipartBody.Part
//    )
//
//    companion object {
//        val instance: FileApi by lazy {
//            Retrofit.Builder()
//                .baseUrl("https://3830-202-134-14-157.in.ngrok.io/api/faceimageupload/")
//                .build()
//                .create(FileApi::class.java)
//        }
//    }
//}