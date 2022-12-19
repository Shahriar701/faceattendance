package reddot.crimson.facialrecognitionattendnancesystem.data

data class UploadResponse(
    val error: Boolean,
    val message: String,
    val image: String
)