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

        fun toPatientDTO(result: Cursor) : User {
            return User(
                result.getInt(result.getColumnIndexOrThrow("pa$COLUMN_ID")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_NAME")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_USERNAME")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_PASSWORD")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_EMAIL")),
                result.getInt(result.getColumnIndexOrThrow("pa$COLUMN_PHONE_NUMBER")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_BIRTHDAY")),
                result.getString(result.getColumnIndexOrThrow("pa$COLUMN_ROLE"))
            )
        }

        fun toDocDTO(result: Cursor): User {
            return User(
                result.getInt(result.getColumnIndexOrThrow("doc$COLUMN_ID")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_NAME")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_USERNAME")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_PASSWORD")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_EMAIL")),
                result.getInt(result.getColumnIndexOrThrow("doc$COLUMN_PHONE_NUMBER")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_BIRTHDAY")),
                result.getString(result.getColumnIndexOrThrow("doc$COLUMN_ROLE"))
            )
        }
    }
}