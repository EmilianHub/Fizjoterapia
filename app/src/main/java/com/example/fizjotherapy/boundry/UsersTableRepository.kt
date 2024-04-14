package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fizjotherapy.DTO.Users
import com.example.fizjotherapy.control.UsersService

class UsersTableRepository(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "fizjotheraphy.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val QUERY = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_EMAIL TEXT NOT NULL," +
                "$COLUMN_USERNAME TEXT NOT NULL," +
                "$COLUMN_PASSWORD TEXT NOT NULL," +
                "$COLUMN_PASSWORD TEXT NOT NULL" +
                ")"
        db?.execSQL(QUERY);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun create(users: Users) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, users.email)
            put(COLUMN_USERNAME, users.username)
            put(COLUMN_PASSWORD, users.password)
            put(COLUMN_ROLE, users.rola)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun findByUsernameAndPassword(username: String, password: String): Users? {
        var user: Users? = null
        val db = readableDatabase
        val QUERY = "SELECT * FROM $TABLE_NAME " +
                "WHERE $username in ($COLUMN_USERNAME, $COLUMN_EMAIL) and $COLUMN_PASSWORD = $password"
        val result = db.rawQuery(QUERY, null)
        if (result.moveToFirst()) {
            user = Users(
                result.getInt(result.getColumnIndexOrThrow(COLUMN_ID)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_USERNAME)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_EMAIL)),
                result.getString(result.getColumnIndexOrThrow(COLUMN_ROLE)),
            )
        }
        result.close()
        db.close()
        return user;
    }
}