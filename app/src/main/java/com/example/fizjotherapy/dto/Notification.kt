package com.example.fizjotherapy.dto

data class Notification(
    val id: Int?,
    val receiverId: Int,
    val appointment: Wizyta,
    val lifeCycleState: LifeCycleState
) {
    constructor(id: Int?,
                receiverId: Int,
                appointment: Wizyta,
                lifeCycleState: String) : this(id, receiverId, appointment, LifeCycleState.getByString(lifeCycleState))
}
