package com.example.fizjotherapy.mapper

import android.database.Cursor
import com.example.fizjotherapy.dto.User

class UserMapper {
    companion object {
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ROLE = "role"
        private const val COLUMN_PHONE_NUMBER = "phone_number"
        private const val COLUMN_BIRTHDAY = "birthday"

        fun toDTO(result: Cursor) : User {
            return User(
                result.getInt(result.getColumnIndexOrThrow(COLUMN_ID)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_NAME)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_USERNAME)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_EMAIL)),
                result.getInt(result.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_BIRTHDAY)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_ROLE))
            )
        }

        fun toDTO(result: Cursor, prefix: String) : User {
            return User(
                result.getInt(result.getColumnIndexOrThrow("$prefix$COLUMN_ID")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_NAME")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_USERNAME")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_PASSWORD")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_EMAIL")),
                result.getInt(result.getColumnIndexOrThrow("$prefix$COLUMN_PHONE_NUMBER")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_BIRTHDAY")),
                result.getString(result.getColumnIndexOrThrow("$prefix$COLUMN_ROLE"))
            )
        }
    }
}