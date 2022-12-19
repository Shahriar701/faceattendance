package reddot.crimson.facialrecognitionattendnancesystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import reddot.crimson.facialrecognitionattendnancesystem.repository.FileRepository
import java.io.File

class FileViewModel(private val repository: FileRepository = FileRepository()): ViewModel() {
    fun uploadImage(file: File){
        viewModelScope.launch {
            repository.uploadImage(file)
        }
    }
}