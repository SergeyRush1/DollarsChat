package com.example.dollarschat.data

data class UserProfile(val login:String?=null,
                       val avatar:Avatars?=null,
                       val messageQuantity:Int?=null,
                       val roomsCreateQuantity:Int?=null,
                       var id:String?=null,
                       val accessibleAvatars: String?=null) {
}