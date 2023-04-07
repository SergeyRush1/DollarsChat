package com.example.dollarschat.data.localBase.AvatarDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AvatarEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun avatarsDao(): AvatarsDao


}