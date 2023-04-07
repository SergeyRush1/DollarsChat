package com.example.dollarschat.data

import android.content.Context
import android.content.SharedPreferences
import com.example.dollarschat.ui.theme.DollarsSize

class Settings(context:Context) {
   private var settingsPref: SharedPreferences = context.getSharedPreferences(TABLE_NAME_SETTINGS,Context.MODE_PRIVATE)
    private val userPref:SharedPreferences = context.getSharedPreferences(TABLE_NAME,Context.MODE_PRIVATE)
    private val imahePref:SharedPreferences = context.getSharedPreferences(TABLE_NAME_AVATARS,Context.MODE_PRIVATE)

    fun getDarkMode():Boolean{
        return settingsPref.getBoolean(DARK_MODE,false)
    }
    fun setDarkMode(uiMode:Boolean){
        val editor=settingsPref.edit()
        editor.putBoolean(DARK_MODE,uiMode)
        editor.apply()
    }

    fun setSize(type: String,size:DollarsSize){
        val editor=settingsPref.edit()
        when(size){
            DollarsSize.Big ->{editor.putString(type, BIG).apply()}
            DollarsSize.Medium ->{editor.putString(type, MEDIUM).apply()}
            DollarsSize.Small ->{editor.putString(type, SMALL).apply()}
        }
    }

    fun getSize(type:String):DollarsSize{
        when(settingsPref.getString(type, MEDIUM)){
            BIG -> return DollarsSize.Big
            MEDIUM -> return DollarsSize.Medium
            SMALL -> return DollarsSize.Small
            else -> return DollarsSize.Medium
        }

    }
    fun setLocalUser(userProfile: UserProfile){
        val editor = userPref.edit()
        editor.putString(USER_PROFILE_NAME, userProfile.login)
        editor.putString(USER_PROFILE_AVATAR, userProfile.avatar?.image_name)
        editor.putString(USER_PROFILE_ID, userProfile.id)
        editor.putString(USER_PROFILE_ACCESSIBLE_AVATARS, userProfile.accessibleAvatars)
        editor.putInt(USER_PROFILE_MESSAGE_QUANTITY, userProfile.messageQuantity!!)
        editor.putInt(USER_PROFILE_ROOM_CREATE_QUANTITY,userProfile.roomsCreateQuantity!!)
        editor.apply()
        val imageEditor = imahePref.edit()
        imageEditor.putString(IMAGE_NAME,userProfile.avatar?.image_name)
        imageEditor.putString(IMAGE_COLOR,userProfile.avatar?.color)
        imageEditor.putString(IMAGE_STORAGE_REF,userProfile.avatar?.storageReference)
        imageEditor.putString(IMAGE_IVENT_PACKEGE_NAME,userProfile.avatar?.iventPackageName)
        imageEditor.putBoolean(IMEGE_ACCESSABLE,userProfile.avatar?.accessAvatar!!)
        imageEditor.apply()
    }
    fun getLocalUser(): UserProfile {
        return UserProfile(
            login = userPref.getString(USER_PROFILE_NAME, ""),
            messageQuantity = userPref.getInt(USER_PROFILE_MESSAGE_QUANTITY, 0),
            roomsCreateQuantity = userPref.getInt(USER_PROFILE_ROOM_CREATE_QUANTITY, 0),
            id = userPref.getString(USER_PROFILE_ID, ""),
            accessibleAvatars = userPref.getString(USER_PROFILE_ACCESSIBLE_AVATARS, ""),
            avatar = Avatars(
                image_name = imahePref.getString(IMAGE_NAME, "")!!,
                iventPackageName = imahePref.getString(IMAGE_IVENT_PACKEGE_NAME, "")!!,
                storageReference = imahePref.getString(IMAGE_STORAGE_REF, "")!!,
                accessAvatar = imahePref.getBoolean(IMEGE_ACCESSABLE, true),
                color = imahePref.getString(IMAGE_COLOR, "blue")!!
            )
        )
    }

    fun changeLocalUser(type: String,valueString:String?=null,valueInt:Int?=null,avtar:Avatars?=null){
        when(type){
            USER_PROFILE_NAME ->{
                userPref.edit().putString(USER_PROFILE_NAME,valueString)
                    .apply()
            }
            USER_PROFILE_MESSAGE_QUANTITY->{
                userPref.edit().putInt(USER_PROFILE_MESSAGE_QUANTITY,
                valueInt?:getLocalUser()?.messageQuantity!!).apply()
            }
            USER_PROFILE_ACCESSIBLE_AVATARS->{
                userPref.edit().putString(USER_PROFILE_ACCESSIBLE_AVATARS,valueString)
                    .apply()
            }
            USER_PROFILE_ROOM_CREATE_QUANTITY->{
                userPref.edit().putInt(
                USER_PROFILE_ROOM_CREATE_QUANTITY,valueInt?:getLocalUser()?.roomsCreateQuantity!!)
                    .apply()
            }
            else ->{changeUserAvatar(avtar!!)}
        }
    }
    private fun changeUserAvatar(avatar: Avatars){
        imahePref.edit().putString(IMAGE_NAME,avatar.image_name)
            .putString(IMAGE_COLOR,avatar.color)
            .putString(IMAGE_STORAGE_REF,avatar.storageReference)
            .putString(IMAGE_IVENT_PACKEGE_NAME,avatar.iventPackageName)
            .putBoolean(IMEGE_ACCESSABLE,avatar.accessAvatar)
            .apply()
    }

//    fun setNewUserValue(userProfile: UserProfile,
//                        type: String,
//                        valueAvatars: Avatars?=null, newValueInt:String?=null,newValueString: Int?=null){
//        when(type){
//            USER_PROFILE_NAME ->{}
//            USER_PROFILE_MESSAGE_QUANTITY->{}
//            USER_PROFILE_ACCESSIBLE_AVATARS->{}
//            USER_PROFILE_ROOM_CREATE_QUANTITY->{}
//            else ->{}
//        }
//
//
//    }
//    private fun setTypeStrint(typeString: String){
//
//    }
}