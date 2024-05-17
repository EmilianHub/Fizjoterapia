package com.example.fizjotherapy.mapper

import android.database.Cursor
import com.example.fizjotherapy.dto.Notification

class NotificationMapper {
    companion object {
        private const val COLUMN_ID = "ntid"
        private const val COLUMN_RECEIVER_ID = "ntuser_receiver_id"
        private const val COLUMN_LIFE_CYCLE_STATE = "ntlifecyclestate"

        fun toDTO(result: Cursor): Notification {
            return Notification(
                result.getInt(result.getColumnIndexOrThrow(COLUMN_ID)),
                result.getInt(result.getColumnIndexOrThrow(COLUMN_RECEIVER_ID)),
                WizytaMapper.toSimpleDTO(result),
                result.getString(result.getColumnIndexOrThrow(COLUMN_LIFE_CYCLE_STATE))
            )
        }
    }
}