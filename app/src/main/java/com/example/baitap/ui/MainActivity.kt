package com.example.baitap.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.baitap.R
import com.example.baitap.data.AppDatabase
import com.example.baitap.network.RetrofitClient
import com.example.baitap.repository.StudentRepository
import com.example.baitap.viewmodel.StudentViewModel
import com.example.baitap.viewmodel.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Tạo ViewModel ngay (không lazy) để Fragment dùng activityViewModels() lấy đúng instance
        val repository = StudentRepository(AppDatabase.getDatabase(this).studentDao(), RetrofitClient.apiService)
        ViewModelProvider(this, StudentViewModelFactory(repository))[StudentViewModel::class.java]

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, StudentListFragment())
                .commit()
        }
    }
}
