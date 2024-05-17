package com.example.fizjotherapy.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fizjotherapy.R
import com.example.fizjotherapy.boundry.GlobalUser
import com.example.fizjotherapy.control.UsersService
import com.example.fizjotherapy.databinding.FragmentRegisterBinding
import com.example.fizjotherapy.dto.Rola
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.prompt.PromptService

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var userService: UsersService
    private lateinit var promptViewService: PromptService

    private var userFlag = false
    private var passwordFlag = false
    private var retypePasswordFlag = false
    private var emailFlag = false
    private var nameFlag = false
    private var selectedRole: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        userService = UsersService(requireActivity())
        promptViewService = PromptService(requireActivity())

        val username: EditText = binding.usernameReg
        val name: EditText = binding.nameReg
        val password: EditText = binding.passwordReg
        val rePassword: EditText = binding.retypePassword
        val email: EditText = binding.emailReg
        val regButton: Button = binding.registerButton
        val retypePasswordError: TextView = binding.rePasswordError
        val passwordError: TextView = binding.passwordError
        val usernameError: TextView = binding.usernameError
        val emailError: TextView = binding.emailError
        val phone: EditText = binding.phoneReg
        val birthdate: EditText = binding.birthdateReg
        val roleSpinner: Spinner = binding.roleSpinner

        initSpinner(roleSpinner)
        verifyUsernamePresence(username, usernameError)
        verifyUserNotExists(username, usernameError)
        verifyPasswordLength(password, passwordError)
        confirmPasswords(password, rePassword, retypePasswordError)
        verifyEmailSyntax(email, emailError)
        nameListener(name)

        regButton.setOnClickListener {
            registerUser(mapToUser(
                username,
                password,
                email,
                name,
                birthdate,
                phone
            ))
        }

        return binding.root
    }

    private fun mapToUser(username: EditText, password: EditText, email: EditText, name: EditText, birthdate: EditText, phone: EditText): User {
        return User(
            username.text.toString(),
            password.text.toString(),
            email.text.toString(),
            name.text.toString(),
            phone.text.toString().toInt(),
            birthdate.text.toString(),
            selectedRole ?: Rola.USER.vName)
    }

    private fun initSpinner(roleSpinner: Spinner) {
        GlobalUser.user.also {
            if (it.rola == Rola.ADMIN) {
                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(
                    Rola.entries.map { rola -> rola.vName })
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                roleSpinner.adapter = arrayAdapter

                roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedRole = p0!!.getItemAtPosition(p2).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
                }
            }
        }
    }

    private fun nameListener(name: EditText) {
        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nameFlag = s.isNotBlank()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun verifyUserNotExists(username: EditText, usernameError: TextView) {
        username.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus && userFlag) {
                userFlag = userService.verifyUsernameAbsence(
                    usernameError,
                    username.text.toString(),
                    requireActivity()
                )
            }
        }
    }

    private fun registerUser(user: User) {
        var message = "Registration failed.\nVerify everything is filled correctly"
        if (userFlag && passwordFlag && retypePasswordFlag && emailFlag && nameFlag) {
            val isUserCreated: Boolean = userService.create(user)
            if (isUserCreated) {
                message = "Registered successfully"
                promptViewService.showSuccessToast(message)
                println("New user registered")
                findNavController().navigate(R.id.nav_login)
            } else {
                promptViewService.showFailToast(message)
                println("User not registered")
            }
        } else {
            promptViewService.showFailToast(message)
        }
    }

    private fun verifyUsernamePresence(username: EditText, usernameError: TextView) {
        username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                userFlag = userService.verifyUsernameLength(
                    usernameError,
                    s.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun verifyPasswordLength(password: EditText, passwordError: TextView) {
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                passwordFlag = userService.verifyPasswordSyntax(
                    passwordError,
                    s.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    private fun confirmPasswords(
        password: EditText,
        retypePassword: EditText,
        passwordError: TextView
    ) {
        retypePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                retypePasswordFlag = userService.verifyPasswordRetypeMatch(
                    passwordError,
                    password.text.toString(),
                    s.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun verifyEmailSyntax(email: EditText, emailError: TextView) {
        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailFlag =
                    userService.verifyEmailSyntax(emailError, s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}