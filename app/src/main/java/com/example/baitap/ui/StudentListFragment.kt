package com.example.baitap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baitap.databinding.FragmentStudentListBinding
import com.example.baitap.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    // ViewModel (kèm factory) đã được MainActivity tạo sẵn trong ViewModelStore của Activity
    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StudentAdapter { student -> viewModel.delete(student) }
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = adapter

        viewModel.allStudents.observe(viewLifecycleOwner) { students ->
            adapter.submitList(students)
            binding.emptyLayout.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
            binding.tvCount.text = "Tổng: ${students.size} sinh viên"
        }

        viewModel.syncMessage.observe(viewLifecycleOwner) { msg ->
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.clearSyncMessage()
            }
        }

        binding.btnSync.setOnClickListener { viewModel.refresh() }
        binding.fabAdd.setOnClickListener {
            AddStudentDialogFragment().show(childFragmentManager, "ADD_STUDENT")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
