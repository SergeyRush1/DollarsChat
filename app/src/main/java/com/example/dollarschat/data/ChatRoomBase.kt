package com.example.dollarschat.data

data class ChatRoomBase(val roomName:String? = null,
                        val accesRoom:Boolean? = null,
                        val password:String? = null,
                        val party:ArrayList<UserProfile>? = null,
                        var admin:UserProfile? = null,
                         val size:Int? = null,
                        val messages:Message? = null)
