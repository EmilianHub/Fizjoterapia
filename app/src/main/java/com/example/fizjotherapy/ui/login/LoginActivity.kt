package com.example.fizjotherapy.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.fizjotherapy.DTO.Users
import com.example.fizjotherapy.MainActivity
import com.example.fizjotherapy.R
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.control.UsersService
import com.example.fizjotherapy.databinding.ActivityLoginBinding
import com.example.fizjotherapy.prompt.PromptService

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userService: UsersService
    private lateinit var promptService: PromptService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val loginButton = binding.loginButton
        val errorView = binding.loginError

        password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        tryLogin(username, password, errorView)
                }
                false
            }
        }

        loginButton?.setOnClickListener {
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

    private fun invokeMainActivity(validateUser: Users) {
        GlobalUser.user = validateUser
        promptService.showSuccessToast(applicationContext, "Welcome ${validateUser.username}")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}