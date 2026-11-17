package com.example.baitap.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class StudentDto(
    @SerializedName("id") val remoteId: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("student_code") val studentCode: String,
    @SerializedName("email") val email: String,
    @SerializedName("gpa") val gpa: Double
)

interface StudentApiService {
    @GET("api/v1/students")
    suspend fun getStudents(): List<StudentDto>

    @POST("api/v1/students")
    suspend fun createStudent(@Body studentDto: StudentDto): StudentDto
}

object RetrofitClient {
    // ponytail: URL mẫu của bài giảng, chưa có server thật -> refresh() sẽ lỗi và app chạy offline-first.
    private const val BASE_URL = "https://api.example.com/"

    val apiService: StudentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentApiService::class.java)
    }
}
