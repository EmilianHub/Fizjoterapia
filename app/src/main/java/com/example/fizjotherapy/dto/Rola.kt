package com.example.fizjotherapy.dto

enum class Rola(val vName: String) {
    USER("user"), ADMIN("admin"), DOC("doc");
    companion object {
        fun getByString(name: String): Rola {
            return Rola.entries.first{ enumValue -> enumValue.vName == name }
        }
    }
}