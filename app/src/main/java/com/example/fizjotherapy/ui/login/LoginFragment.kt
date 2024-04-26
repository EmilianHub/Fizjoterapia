package com.example.fizjotherapy.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fizjotherapy.MainActivity
import com.example.fizjotherapy.R
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.control.UsersService
import com.example.fizjotherapy.databinding.FragmentLoginBinding
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.prompt.PromptService

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var userService: UsersService
    private lateinit var promptService: PromptService

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userService = UsersService(requireContext())
        promptService = PromptService(requireContext())
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = binding.username
        val password = binding.password
        val loginButton = binding.loginButton
        val errorView = binding.loginError
        val registerText = binding.registerText

        password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        tryLogin(username, password, errorView)
                }
                false
            }
        }

        registerText.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }

        loginButton.setOnClickListener {
            tryLogin(username, password, errorView)
        }
    }

    private fun tryLogin(username: EditText?, password: EditText?, errorView: TextView?) {
        if (username != null && password != null) {
            val validateUser = userService.validateUser(username, password, errorView)
            if (validateUser != null) {
                invokeMainActivity(validateUser)
            }
        }
    }


    private fun invokeMainActivity(validateUser: User) {
        GlobalUser.user = validateUser
        promptService.showSuccessToast("Welcome ${validateUser.username}")
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}