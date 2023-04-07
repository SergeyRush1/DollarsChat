package com.example.dollarschat.data.localBase.AvatarDatabase

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.dollarschat.data.Avatars

@Entity(tableName = "avatarBase",
    indices = [Index("image_name", unique = true)])
data class AvatarEntity( //val path:String,val url:String,
    @PrimaryKey(autoGenerate = true)val id:Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE)val image_name:String,
    val iventPackageName:String,
    val storageReference: String,
    val accessAvatar:Boolean,
    val color:String) {

    fun toAvatars(): Avatars = Avatars(image_name = image_name,
        iventPackageName = iventPackageName,
        storageReference = storageReference,
        accessAvatar = accessAvatar,
        color = color)

    fun toEntity(avatar:Avatars):AvatarEntity = AvatarEntity(id = 0,
        image_name = avatar.image_name,
        iventPackageName = avatar.iventPackageName,
        storageReference = avatar.storageReference,
        accessAvatar = avatar.accessAvatar, color = avatar.color)

    companion object {
        fun avatarCreator(avatars: Avatars): AvatarEntity = AvatarEntity(id = 0,
            image_name = avatars.image_name,
            iventPackageName = avatars.iventPackageName,
            storageReference = avatars.storageReference,
            accessAvatar = avatars.accessAvatar,
            color = avatars.color)
    }



}