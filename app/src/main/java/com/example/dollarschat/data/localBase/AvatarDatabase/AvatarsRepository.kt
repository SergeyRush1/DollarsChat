package com.example.dollarschat.data.localBase.AvatarDatabase

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

class AvatarsRepository @Inject constructor(
    private val avatarDao: AvatarsDao
) {

    suspend fun addNewAvatar(avatarsEntity: AvatarEntity) {
        avatarDao.createAvatar(avatarsEntity)
    }

    suspend fun getAll(): List<AvatarEntity> =
        avatarDao.getAll()

    suspend fun getAvatar(name:String):AvatarEntity? =
        avatarDao.getAvatarFromName(name)
}

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "avatarBase"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideHabbitDao(avatarDatabase: Database): AvatarsDao = avatarDatabase.avatarsDao()

}