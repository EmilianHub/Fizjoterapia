package com.example.fizjotherapy.control;

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.fizjotherapy.R
import com.example.fizjotherapy.boundry.UsersTableRepository
import com.example.fizjotherapy.dto.Rola
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.encrypter.AES
import com.example.fizjotherapy.prompt.PromptService
import java.util.regex.Pattern

class UsersService(private val activity: Activity) {

    private var userRepository: UsersTableRepository = UsersTableRepository(activity)
    private var promptViewService: PromptService = PromptService(activity)

    private val passwordSyntax =
        Pattern.compile("^(?=.*[0-9!@#$%^&+=])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    private val emailSyntax =
        Pattern.compile("^[a-zA-Z0-9.]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")


    fun validateUser(username: EditText, password: EditText, errorView: TextView?): User? {
        val decryptPassword = AES.encrypt(password.text.toString())
        val user = userRepository.findByUsernameAndPassword(username.text.toString(), decryptPassword!!);
        if (user == null) {
            errorView?.visibility = View.VISIBLE
            return null
        }
        return user;
    }

    fun verifyUsernameAbsence(
        usernameError: TextView,
        username: String?,
        context: Context
    ): Boolean {
        val user: User? = findWithLogin(username)
        if (user != null) {
            val messageText = context.getString(R.string.username_already_exists)
            promptViewService.setErrorTextView(usernameError, messageText, "red", View.VISIBLE)
            return false
        }
        usernameError.visibility = View.INVISIBLE
        return true
    }

    fun findWithLogin(login: String?): User? {
        try {
            return userRepository.findWithUsernameOrEmail(login.orEmpty())
        } catch (e: Exception) {
            Log.d(
                "UserService",
                "Error occurred while searching for users" + e.message
            )
        }
        return null
    }

    fun create(username: String?, password: String, email: String?, name: String, birthdate: String, phone: String): Boolean {
        try {
            val encryptedPassword: String? = AES.encrypt(password)
            val user = User(
                null,
                name,
                username!!,
                encryptedPassword!!,
                email!!,
                phone.toInt(),
                birthdate,
                Rola.USER
            )
            return userRepository.create(user)
        } catch (e: java.lang.Exception) {
            Log.d(
                "UserService",
                "Error occurred while registering user: " + e.message
            )
        }
        return false
    }

    fun create(user: User): Boolean {
        try {
            val encryptedPassword: String? = AES.encrypt(user.password)
            user.password = encryptedPassword!!
            return userRepository.create(user)
        } catch (e: java.lang.Exception) {
            Log.d(
                "UserService",
                "Error occurred while registering user: " + e.message
            )
        }
        return false
    }

    fun verifyPasswordSyntax(
        passwordError: TextView?,
        password: String
    ): Boolean {
        var messageText = ""
        if (password.isBlank()) {
            messageText = activity.getString(R.string.empty_password_text)
            promptViewService.setErrorTextView(passwordError!!, messageText, "red", View.VISIBLE)
            return false
        } else if (password.length < 8) {
            messageText = activity.getString(R.string.short_password_text)
            promptViewService.setErrorTextView(passwordError!!, messageText, "red", View.VISIBLE)
            return false
        } else if (!passwordSyntax.matcher(password).matches()) {
            messageText = activity.getString(R.string.strong_password_text)
            promptViewService.setErrorTextView(passwordError!!, messageText, "red", View.VISIBLE)
            return false
        }
        messageText = "Password is strong"
        promptViewService.setErrorTextView(passwordError!!, messageText, "green", View.VISIBLE)
        return true
    }

    fun verifyEmailSyntax(emailError: TextView, email: String): Boolean {
        var messageText = ""
        if (email.isBlank()) {
            messageText = activity.getString(R.string.email_error_text)
            promptViewService.setErrorTextView(emailError, messageText, "red", View.VISIBLE)
            return false
        } else if (!emailSyntax.matcher(email).matches()) {
            messageText = activity.getString(R.string.incorrect_email_error_text)
            promptViewService.setErrorTextView(emailError, messageText, "red", View.VISIBLE)
            return false
        }
        emailError.visibility = View.INVISIBLE
        return true
    }

    fun verifyPasswordRetypeMatch(
        retypePasswordError: TextView,
        password: String,
        retypePassword: String
    ): Boolean {
        var messageText = ""
        if (retypePassword.isBlank()) {
            messageText = activity.getString(R.string.empty_filed_text)
            promptViewService.setErrorTextView(
                retypePasswordError,
                messageText,
                "red",
                View.VISIBLE
            )
            return false
        } else if (retypePassword != password) {
            messageText = activity.getString(R.string.not_matching_password)
            promptViewService.setErrorTextView(
                retypePasswordError,
                messageText,
                "red",
                View.VISIBLE
            )
            return false
        }
        retypePasswordError.visibility = View.INVISIBLE
        return true
    }

    fun verifyUsernameLength(usernameError: TextView, username: String): Boolean {
        var messageText = ""
        if (username.isBlank()) {
            messageText = activity.getString(R.string.empty_filed_text)
            promptViewService.setErrorTextView(usernameError, messageText, "red", View.VISIBLE)
            return false
        } else if (username.length < 3) {
            messageText = activity.getString(R.string.username_requirements)
            promptViewService.setErrorTextView(usernameError, messageText, "red", View.VISIBLE)
            return false
        }
        usernameError.visibility = View.INVISIBLE
        return true
    }

    fun findAllDoctorNames() : List<User> {
        return userRepository.findAllDocs()
    }
}
