package com.example.fizjotherapy.control;

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.fizjotherapy.dto.Users
import com.example.fizjotherapy.boundry.UsersTableRepository

class UsersService(context: Context) {

    private var userRepository: UsersTableRepository = UsersTableRepository(context)

    fun validateUser(username: EditText, password: EditText, errorView: TextView?): Users? {
        val user = userRepository.findByUsernameAndPassword(username.text.toString(), password.text.toString());
        if (user == null) {
            errorView?.visibility = View.VISIBLE
            return null
        }
        return user;
    }
}
