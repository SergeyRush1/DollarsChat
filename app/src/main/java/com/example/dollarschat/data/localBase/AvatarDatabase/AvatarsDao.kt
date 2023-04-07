package com.example.dollarschat.data.localBase.AvatarDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dollarschat.data.Avatars

@Dao
interface AvatarsDao {
    @Query("SELECT image_name, iventPackageName, iventPackageName, storageReference, accessAvatar, color FROM avatarBase WHERE image_name = :imageName ")
    suspend fun findAvatarByName(imageName:String): Avatars?

//    @Update(entity = AvatarEntity::class)
//    suspend fun updateAvatarAccess(updateAccess: AvatarUpdateAccess)
    @Query("SELECT * FROM avatarBase")
    suspend fun getAll(): List<AvatarEntity>

    @Insert
    suspend fun createAvatar(avatarEntity: AvatarEntity)

    @Query("SELECT * FROM avatarBase WHERE image_name = :imageName")
    fun getAvatarFromName(imageName:String): AvatarEntity?
}