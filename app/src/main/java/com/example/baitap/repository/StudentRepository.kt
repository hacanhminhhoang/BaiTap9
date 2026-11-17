package com.example.baitap.repository

import com.example.baitap.data.StudentDao
import com.example.baitap.model.Student
import com.example.baitap.network.StudentApiService
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
    private val api: StudentApiService
) {
    // Single Source of Truth: UI chỉ quan sát Room
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student) = studentDao.insertStudent(student)

    suspend fun delete(student: Student) = studentDao.deleteStudent(student)

    /** Kéo dữ liệu từ REST API rồi ghi đè vào Room. Ném exception nếu mất mạng/lỗi server. */
    suspend fun refresh() {
        val remote = api.getStudents().map {
            Student(fullName = it.fullName, studentCode = it.studentCode, email = it.email, gpa = it.gpa)
        }
        studentDao.insertAll(remote)
    }
}
