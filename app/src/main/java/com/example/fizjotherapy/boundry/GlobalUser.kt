package com.example.fizjotherapy.boundry;

import android.app.Application
import com.example.fizjotherapy.dto.User

class GlobalUser : Application() {
    companion object {
        lateinit var user: User
    }
}
