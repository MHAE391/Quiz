package com.m391.quiz.ui.question.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.m391.quiz.R
import com.m391.quiz.database.local.repositories.QuizRepository
import com.m391.quiz.databinding.FragmentCreateQuestionBinding
import com.m391.quiz.ui.quiz.QuizViewModel
import com.m391.quiz.ui.quiz.QuizViewModelFactory
import com.m391.quiz.ui.shared.BaseFragment
import com.m391.quiz.utils.Statics.RESPONSE_SUCCESS
import com.m391.quiz.utils.Statics.VALID_DATA
import kotlinx.coroutines.launch

class CreateQuestionFragment : BaseFragment() {

    private val binding: FragmentCreateQuestionBinding by lazy {
        FragmentCreateQuestionBinding.inflate(layoutInflater)
    }
    private val args by navArgs<CreateQuestionFragmentArgs>()
    private lateinit var items: List<String>

    private val quizViewModel: QuizViewModel by activityViewModels {
        QuizViewModelFactory(requireActivity().application, QuizRepository(requireContext()))
    }
    override val viewModel: CreateQuestionViewModel by viewModels {
        CreateQuestionViewModelFactory(
            requireActivity().application,
            args.quizId,
            args.questionNumber,
            quizViewModel.insertQuestion
        )
    }

    private val chooseQuestionBodyImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuestionAnswerImage(uri.toString(), 0)
            }
        }
    private val chooseFirstAnswerImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuestionAnswerImage(uri.toString(), 1)
            }
        }
    private val chooseSecondAnswerImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuestionAnswerImage(uri.toString(), 2)
            }
        }
    private val chooseThirdAnswerImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuestionAnswerImage(uri.toString(), 3)
            }
        }
    private val chooseFourthAnswerImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setQuestionAnswerImage(uri.toString(), 4)
            }
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


        binding.answerType.setText(getString(R.string.type), false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.arrowBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.answerType.setOnDismissListener {
            binding.answerType.clearFocus()
        }
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = 10
        binding.answerType.setOnItemClickListener { _, _, position, _ ->
            if (items[position] == "MCQ") {
                viewModel.setEssayVisibility(View.GONE)
                viewModel.setMCQVisibility(View.VISIBLE)
                layoutParams.addRule(RelativeLayout.BELOW, binding.mcqAnswer.id)

            } else {
                viewModel.setEssayVisibility(View.VISIBLE)
                viewModel.setMCQVisibility(View.GONE)
                layoutParams.addRule(RelativeLayout.BELOW, binding.essayAnswer.id)
            }
            viewModel.setQuestionType(items[position])
            binding.questionScoreLayout.layoutParams = layoutParams
        }

        binding.questionImageBody.setOnClickListener {
            chooseQuestionBodyImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.firstAnswerImageBody.setOnClickListener {
            chooseFirstAnswerImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.secondAnswerImageBody.setOnClickListener {
            chooseSecondAnswerImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.thirdAnswerImageBody.setOnClickListener {
            chooseThirdAnswerImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.fourthAnswerImageBody.setOnClickListener {
            chooseFourthAnswerImage.launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        binding.previewQuestion.setOnClickListener {
            viewModel.positiveShowLoading()
            val response = viewModel.checkDataValidation()
            if (response == VALID_DATA) {
                val question = viewModel.getQuestion()
                findNavController().navigate(
                    CreateQuestionFragmentDirections
                        .actionCreateQuestionFragmentToPreviewQuestionFragment(
                            question,
                            getString(R.string.teacher_create)
                        )
                )
                viewModel.negativeShowLoading()
            } else {
                viewModel.showSnackBar(response, requireView())
                viewModel.negativeShowLoading()
            }
        }
        binding.createQuestion.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveQuestionToDatabase()
            }
        }
        binding.essayTextAnswer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEssayAnswer(isChecked, 0)
        }
        binding.essayImageAnswer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEssayAnswer(isChecked, 1)
        }
    }

    override fun onResume() {
        super.onResume()
        items = resources.getStringArray(R.array.answer_type).toList()
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                items
            )
        binding.answerType.setAdapter(adapter)
        viewModel.result.observe(viewLifecycleOwner) { response ->
            if (!response.isNullOrEmpty()) {
                if (response.equals(RESPONSE_SUCCESS)) findNavController().popBackStack()
                else viewModel.showSnackBar(response, requireView())
                viewModel.resetResult()
                viewModel.negativeShowLoading()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.result.removeObservers(viewLifecycleOwner)
    }

}