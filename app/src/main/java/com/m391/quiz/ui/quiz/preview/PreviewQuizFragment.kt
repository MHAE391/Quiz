package com.m391.quiz.ui.quiz.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.FragmentPreviewQuizBinding
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.setupLinearRecycler
import kotlinx.coroutines.launch

class PreviewQuizFragment : BaseFragment() {
    private val binding: FragmentPreviewQuizBinding by lazy {
        FragmentPreviewQuizBinding.inflate(layoutInflater)
    }
    private val quizViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }

    private val quizArgs: PreviewQuizFragmentArgs by navArgs()
    override val viewModel: PreviewQuizViewModel by viewModels {
        PreviewQuizViewModelFactory(
            requireActivity().application,
            quizArgs.quizId,
            quizViewModel.getQuizById,
            quizViewModel.getAllQuizQuestion,
            remoteDatabase.quizzes
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        binding.uploadQuiz.setOnClickListener {
            lifecycleScope.launch {
                viewModel.uploadQuizToFirestore()
            }
        }
        binding.createNewQuestion.setOnClickListener {

            findNavController().navigate(
                PreviewQuizFragmentDirections.actionPreviewQuizFragmentToCreateQuestionFragment(
                    quizArgs.quizId,
                    if (!viewModel.quizQuestions.value.isNullOrEmpty())
                        viewModel.quizQuestions.value!!.size.plus(1)
                    else 1
                )
            )
        }
        binding.arrowBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshQuestions()
        }
        viewModel.result.observe(viewLifecycleOwner) { response ->
            if (!response.equals(String())) {
                viewModel.negativeShowLoading()
                if (response == RESPONSE_SUCCESS) {
                    viewModel.showToast(response)
                    findNavController().popBackStack()
                } else viewModel.showSnackBar(response, requireView())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.result.removeObservers(viewLifecycleOwner)
        viewModel.resetResult()
    }

    private fun setUpRecyclerView() {
        val adapter = QuestionAdapter { question ->
            findNavController().navigate(
                PreviewQuizFragmentDirections.actionPreviewQuizFragmentToPreviewQuestionFragment(
                    getString(R.string.teacher_database),
                    question
                )
            )
        }
        binding.quizQuestionsRecycler.setupLinearRecycler(adapter, true)
    }
}