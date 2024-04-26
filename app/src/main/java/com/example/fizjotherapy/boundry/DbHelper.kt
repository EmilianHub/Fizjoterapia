package com.example.fizjotherapy.boundry

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.example.fizjotherapy.dto.User
import com.example.fizjotherapy.encrypter.AES
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class DbHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = GlobalRepoConfig.DATABASE_NAME
        private val DATABASE_VERSION = GlobalRepoConfig.DATABASE_VERSION
    }

    override fun onCreate(db: SQLiteDatabase?) {
        onCreateUser(db)
        onCreateAppoint(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        onUpgradeUser(db)
        onUpgradeAppoint(db)
    }

    fun onCreateUser(db: SQLiteDatabase?) {
        val QUERY_DEL = "DROP TABLE IF EXISTS ${UsersTableRepository.TABLE_NAME}"
        db?.execSQL(QUERY_DEL)
        val QUERY = "CREATE TABLE ${UsersTableRepository.TABLE_NAME}(" +
                "${UsersTableRepository.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${UsersTableRepository.COLUMN_EMAIL} TEXT NOT NULL," +
                "${UsersTableRepository.COLUMN_NAME} TEXT," +
                "${UsersTableRepository.COLUMN_USERNAME} TEXT NOT NULL," +
                "${UsersTableRepository.COLUMN_PASSWORD} TEXT NOT NULL," +
                "${UsersTableRepository.COLUMN_PHONE_NUMBER} INTEGER," +
                "${UsersTableRepository.COLUMN_ROLE} TEXT NOT NULL," +
                "${UsersTableRepository.COLUMN_BIRTHDAY} DATE" +
                ")"
        db?.execSQL(QUERY);
        initUsers(db)
    }

    private fun initUsers(db: SQLiteDatabase?) {
        val userJsonFile = "[\n" +
                "  {\n" +
                "    \"name\": \"Emilian\",\n" +
                "    \"username\": \"Emilian\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"some@gmail.com\",\n" +
                "    \"phone\": 123456789,\n" +
                "    \"birthday\": \"2000-11-10\",\n" +
                "    \"rola\": \"admin\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Katarzyna Swierszcz\",\n" +
                "    \"username\": \"Katarzyna\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"kasia@gmail.com\",\n" +
                "    \"phone\": 123456789,\n" +
                "    \"birthday\": \"2000-11-10\",\n" +
                "    \"rola\": \"doc\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Grzegorz Karas\",\n" +
                "    \"username\": \"Grzegorz\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"email\": \"grzesiu@gmail.com\",\n" +
                "    \"birthday\": \"2000-11-10\",\n" +
                "    \"phone\": 123456789,\n" +
                "    \"rola\": \"user\"\n" +
                "  }\n" +
                "]"
        val mapper = jacksonObjectMapper()
        val users: List<User> = mapper.readValue(userJsonFile)
        val foundNames = findByName(users.map {user -> String.format("'${user.username}'")}, db)
        users.filter { user -> !foundNames.contains(user.username.lowercase()) }
        create(users, db)
    }

    private fun create(users: List<User>, db: SQLiteDatabase?) {
        ContentValues().apply {
            users.forEach { user ->
                put(UsersTableRepository.COLUMN_EMAIL, user.email)
                put(UsersTableRepository.COLUMN_NAME, user.name)
                put(UsersTableRepository.COLUMN_USERNAME, user.username)
                put(UsersTableRepository.COLUMN_PASSWORD, AES.encrypt(user.password!!))
                put(UsersTableRepository.COLUMN_PHONE_NUMBER, user.phone)
                put(UsersTableRepository.COLUMN_BIRTHDAY, user.birthday.toString())
                put(UsersTableRepository.COLUMN_ROLE, user.rola)
                db?.insert(UsersTableRepository.TABLE_NAME, null, this)
            }
        }
    }

    fun onUpgradeUser(db: SQLiteDatabase?) {
        val QUERY = "DROP TABLE IF EXISTS ${UsersTableRepository.TABLE_NAME}"
        db?.execSQL(QUERY)
        onCreate(db)
        initUsers(db)
    }

    private fun findByName(usernames : List<String>, db: SQLiteDatabase?) : List<String> {
        val foundNames = mutableListOf<String>()
        val usernamesLower = usernames.map { username -> username.lowercase() }
        val usernamesAsString = TextUtils.join(",", usernamesLower)
        val QUERY = "SELECT username FROM ${UsersTableRepository.TABLE_NAME} WHERE lower(${UsersTableRepository.COLUMN_USERNAME}) in ($usernamesAsString)"
        val result = db?.rawQuery(QUERY, null)!!
        while (result.moveToNext()) {
            foundNames.add(result.getString(result.getColumnIndexOrThrow(UsersTableRepository.COLUMN_USERNAME)).lowercase())
        }
        result.close()
        return foundNames
    }

    private fun onCreateAppoint(db: SQLiteDatabase?) {
        val QUERY_DEL = "DROP TABLE IF EXISTS ${AppointmentRepository.TABLE_NAME}"
        db?.execSQL(QUERY_DEL)
        val QUERY = "CREATE TABLE ${AppointmentRepository.TABLE_NAME}(" +
                "${AppointmentRepository.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${AppointmentRepository.COLUMN_PATIENT} INTEGER," +
                "${AppointmentRepository.COLUMN_PATIENT_NAME} TEXT," +
                "${AppointmentRepository.COLUMN_DOCTOR} INTEGER," +
                "${AppointmentRepository.COLUMN_DATE} DATETIME," +
                "${AppointmentRepository.COLUMN_PHONE_NUMBER} INTEGER," +
                "${AppointmentRepository.COLUMN_BIRTHDATE} DATE," +
                "FOREIGN KEY(${AppointmentRepository.COLUMN_PATIENT}) REFERENCES users(id)," +
                "FOREIGN KEY(${AppointmentRepository.COLUMN_DOCTOR}) REFERENCES users(id)" +
                ")"
        db?.execSQL(QUERY);
    }

    private fun onUpgradeAppoint(db: SQLiteDatabase?) {
        val QUERY = "DROP TABLE IF EXISTS ${AppointmentRepository.TABLE_NAME}"
        db?.execSQL(QUERY)
        onCreate(db)
    }
}