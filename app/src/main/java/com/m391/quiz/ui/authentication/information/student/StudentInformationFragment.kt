package com.m391.quiz.ui.authentication.information.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.m391.quiz.R
import com.m391.quiz.databinding.FragmentStudentInformationBinding
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.ui.shared.BaseViewModel
import com.m391.quiz.utils.Binding

class StudentInformationFragment() : BaseFragment() {

    private val binding: FragmentStudentInformationBinding by lazy {
        FragmentStudentInformationBinding.inflate(layoutInflater)
    }
    override val viewModel: StudentInformationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    /*
        private val chooseStudentPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Binding.loadImage(binding.profileImage, uri.toString())
                    viewModel.setStudentImage(uri.toString())
                }
            }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()/*
        binding.studentDateOfBarth.setOnClickListener {

        }
        binding.studentAcademic.setOnClickListener {

        }
        binding.profileImage.setOnClickListener {
            chooseStudentPhoto.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.submit.setOnClickListener {

        }*/
    }


}