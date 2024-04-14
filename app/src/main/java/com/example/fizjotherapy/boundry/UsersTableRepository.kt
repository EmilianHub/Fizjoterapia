package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.example.fizjotherapy.R
import com.example.fizjotherapy.dto.Users
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.util.Locale
import java.util.stream.Collector
import java.util.stream.Collectors

class UsersTableRepository(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "fizjotheraphy.db"
        private const val DATABASE_VERSION = 4
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
                "$COLUMN_ROLE TEXT NOT NULL" +
                ")"
        db?.execSQL(QUERY);
    }

    private fun initUsers(db: SQLiteDatabase?) {
        val userJsonFile = "[\n" +
                "  {\n" +
                "    \"username\": \"Emilian\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"some@gmail.com\",\n" +
                "    \"rola\": \"admin\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"Katarzyna\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"kasia@gmail.com\",\n" +
                "    \"rola\": \"user\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"Grzegorz\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"grzesiu@gmail.com\",\n" +
                "    \"rola\": \"user\"\n" +
                "  }\n" +
                "]"
        val mapper = jacksonObjectMapper()
        val users: List<Users> = mapper.readValue(userJsonFile)
        val foundNames = findByName(users.map {user -> String.format("'${user.username}'")}, db)
        users.filter { user -> !foundNames.contains(user.username.lowercase()) }
        create(users, db)
    }

    private fun create(users: List<Users>, db: SQLiteDatabase?) {
        val values = ContentValues()
            users.forEach {user ->
                values.put(COLUMN_EMAIL, user.email)
                values.put(COLUMN_USERNAME, user.username)
                values.put(COLUMN_PASSWORD, user.password)
                values.put(COLUMN_ROLE, user.rola)
                db?.insert(TABLE_NAME, null, values)
            }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        initUsers(db)
    }

    private fun findByName(usernames : List<String>, db: SQLiteDatabase?) : List<String> {
        val foundNames = mutableListOf<String>()
        val usernamesLower = usernames.map { username -> username.lowercase() }
        val usernamesAsString = TextUtils.join(",", usernamesLower)
        val QUERY = "SELECT username FROM $TABLE_NAME WHERE lower($COLUMN_USERNAME) in ($usernamesAsString)"
        val result = db?.rawQuery(QUERY, null)!!
        while (result.moveToNext()) {
            foundNames.add(result.getString(result.getColumnIndexOrThrow(COLUMN_USERNAME)).lowercase())
        }
        result.close()
        return foundNames
    }

    fun create(user: Users) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_ROLE, user.rola)
        }
        db.beginTransaction()
        db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun create(users: List<Users>) {
        val db = writableDatabase
        val values = ContentValues().apply {
            users.forEach {user ->
                put(COLUMN_EMAIL, user.email)
                put(COLUMN_USERNAME, user.username)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_ROLE, user.rola)
            }
        }
        db.beginTransaction()
        db.insert(TABLE_NAME, null, values)
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun findByUsernameAndPassword(username: String, password: String): Users? {
        var user: Users? = null
        val db = readableDatabase
        val QUERY = "SELECT * FROM $TABLE_NAME " +
                "WHERE lower('$username') in (lower($COLUMN_USERNAME), lower($COLUMN_EMAIL)) and $COLUMN_PASSWORD = '$password'"
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
        return user;
    }
}