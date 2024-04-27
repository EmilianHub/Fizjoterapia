package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.example.fizjotherapy.dto.Rola
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.encrypter.AES
import com.example.fizjotherapy.mapper.UserMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class UsersTableRepository(val context: Context) {
    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_NAME = "name"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_BIRTHDAY = "birthday"
        const val COLUMN_ROLE = "role"
    }

    fun create(users: List<User>): Boolean {
        val db = DbHelper(context).writableDatabase
        db.beginTransaction()
        ContentValues().apply {
            users.forEach {user ->
                put(COLUMN_EMAIL, user.email)
                put(COLUMN_NAME, user.name)
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_PHONE_NUMBER, user.phone)
                put(COLUMN_BIRTHDAY, user.birthday)
                put(COLUMN_ROLE, user.rola)
                db.insert(TABLE_NAME, null, this)
            }
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        return true
    }

    fun create(user: User): Boolean {
        val db = DbHelper(context).writableDatabase
        val values = ContentValues().apply {
                put(COLUMN_EMAIL, user.email)
                put(COLUMN_NAME, user.name)
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_PHONE_NUMBER, user.phone)
                put(COLUMN_BIRTHDAY, user.birthday)
                put(COLUMN_ROLE, user.rola)

        }
        db.beginTransaction()
        db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
        return true
    }

    fun findByUsernameAndPassword(username: String, password: String): User? {
        var user: User? = null
        val db = DbHelper(context).readableDatabase
        val QUERY = "SELECT * FROM $TABLE_NAME " +
                "WHERE '${username.lowercase()}' in (lower($COLUMN_USERNAME), lower($COLUMN_EMAIL)) and $COLUMN_PASSWORD = '$password'"
        val result = db.rawQuery(QUERY, null)
        if (result.moveToFirst()) {
            user = UserMapper.toDTO(result)
        }
        result.close()
        return user;
    }

    fun findWithUsernameOrEmail(login: String): User? {
        val db = DbHelper(context).readableDatabase
        val QUERY = "SELECT * FROM $TABLE_NAME WHERE lower($COLUMN_USERNAME) = '${login.lowercase()}' OR lower($COLUMN_EMAIL) = '${login.lowercase()}'"
        val result = db.rawQuery(QUERY, null)

        return if (result.moveToFirst()) {
            return UserMapper.toDTO(result)
        } else null
    }

    fun findAllDocs() : List<User>{
        val db = DbHelper(context).readableDatabase
        val QUERY = "SELECT * FROM $TABLE_NAME WHERE lower($COLUMN_ROLE) = lower('${Rola.DOC.rola}')"
        val result = db.rawQuery(QUERY, null)

        val doctors = mutableListOf<User>()

        while (result.moveToNext()) {
            doctors.add(UserMapper.toDTO(result))
        }
        return doctors
    }
}