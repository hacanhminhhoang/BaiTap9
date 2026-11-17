package com.example.baitap.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.baitap.model.Student
import com.example.baitap.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    val allStudents: LiveData<List<Student>> = repository.allStudents.asLiveData(Dispatchers.IO)

    private val _syncMessage = MutableLiveData<String?>()
    val syncMessage: LiveData<String?> = _syncMessage

    init {
        refresh()
    }

    fun insert(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(student)
    }

    fun delete(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(student)
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.refresh()
            _syncMessage.postValue("Đã đồng bộ từ server")
        } catch (e: Exception) { // IOException (mất mạng), HttpException, JsonParseException...
            _syncMessage.postValue("Không đồng bộ được, đang dùng dữ liệu cục bộ")
        }
    }

    fun clearSyncMessage() = _syncMessage.setValue(null)
}

class StudentViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = StudentViewModel(repository) as T
}
