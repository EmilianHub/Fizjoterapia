package com.example.fizjotherapy.boundry;

import android.app.Application
import com.example.fizjotherapy.DTO.Users

class GlobalUser : Application() {
    companion object {
        lateinit var user: Users
    }
}
