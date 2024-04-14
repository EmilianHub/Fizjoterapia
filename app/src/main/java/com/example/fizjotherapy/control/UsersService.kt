package com.example.fizjotherapy.control;

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.fizjotherapy.DTO.Users
import com.example.fizjotherapy.boundry.UsersTableRepository
import com.example.fizjotherapy.prompt.PromptService

class UsersService private constructor() {

    companion object {
        val INSTANCE: UsersService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { UsersService() }
    }

    private lateinit var userRepository: UsersTableRepository

    fun validateUser(username: EditText, password: EditText, errorView: TextView?): Users? {
        val user = userRepository.findByUsernameAndPassword(username.toString(), password.toString());
        if (user == null) {
            errorView?.visibility = View.VISIBLE
            return null
        }
        return user;
    }
}
