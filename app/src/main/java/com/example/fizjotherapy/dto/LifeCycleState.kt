package com.example.fizjotherapy.dto

enum class LifeCycleState(val state: String) {
    ACTIVE("A"), CANCELED("C");

    companion object {
        fun getByString(state: String): LifeCycleState {
            return entries.first{enumValue -> enumValue.state == state }
        }
    }
}