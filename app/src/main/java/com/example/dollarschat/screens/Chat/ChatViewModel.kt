package com.example.dollarschat.screens.Chat

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import com.example.dollarschat.data.*
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarsRepository
import com.example.dollarschat.handler.EventHandler
import com.example.dollarschat.screens.Chat.models.ChatEvents
import com.example.dollarschat.screens.Chat.models.ChatState
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateEvent
import com.example.dollarschat.screens.authScreens.UserCreate.models.UserCreateState
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(@ApplicationContext application: Context,
                                              private val avatarsRepository: AvatarsRepository
                                              ) : ViewModel(), EventHandler<ChatEvents> {
    val _chatViewState: MutableLiveData<ChatState> = MutableLiveData(ChatState.Loading)
    val chatViewState: LiveData<ChatState> = _chatViewState
    private val TAG:String = "chat"
    val firebaseHelper = FirebaseHelper(application)
    var chatRoom = ChatRoomBase()
    val settingsSaver = Settings(application)
    var userProfile = settingsSaver.getLocalUser()
    private val roomPref:SharedPreferences = application.getSharedPreferences(TABLE_NAME_ROOM,Context.MODE_PRIVATE)
    var userInRoomListener:ValueEventListener? = null
    var messageLisener:ValueEventListener?=null
    var roomTest:ChatRoomBase?= null


    override fun obtainEvent(event: ChatEvents) {
        when(val currentState = _chatViewState.value){
            is ChatState.Loading -> reduce(event,currentState)
            is ChatState.LoadingMessageInRoom-> {}
            is ChatState.InRoom -> reduce(event,currentState)
            is ChatState.RoomList -> reduce(event,currentState)
            is ChatState.RoomDellited -> reduce(event,currentState)
            is ChatState.GoToRoom -> reduce(event,currentState)

            else->{ }
        }
    }
    private fun reduce(event: ChatEvents,currentState: ChatState.GoToRoom){
        when(event){
           is ChatEvents.GoToRoom -> {goToRoom(event.room)}
            else ->{}

        }
    }
    private fun reduce(event: ChatEvents,currentState: ChatState.RoomDellited){
        when(event){
            is ChatEvents.RoomDelited -> loadRooms()
            else ->{}
        }
    }
    private fun reduce(event: ChatEvents,currentState: ChatState.RoomList){
        when (event){
            is ChatEvents.RoomSelected -> {checkRoom(event, currentState)}

            is ChatEvents.PassEnter -> {if (event.pass==chatRoom.password){
                goToRoom(chatRoom)
            }else{
                _chatViewState.postValue(currentState.copy(passState = false))
            }}

            is ChatEvents.canelButtonClick -> _chatViewState.postValue(currentState.copy(alertState = false))
            is ChatEvents.okAlertDialogClick -> _chatViewState.postValue(currentState.copy(fullRoomAlert = false))

            else ->{}
        }
    }
    private fun checkRoom(event: ChatEvents.RoomSelected,currentState: ChatState.RoomList) {
        viewModelScope.launch {
            val room = firebaseHelper.getRoom(event.room.admin?.id.toString())
            if (room == null) {
                _chatViewState.postValue(ChatState.RoomDellited)
            } else {
                if ((event.room.party?.size ?: 0) >= event.room.size!!) {
                    _chatViewState.postValue(currentState.copy(fullRoomAlert = true))
                } else {
                    if (event.room.accesRoom!!) {
                        goToRoom(event, currentState)
                    } else {
                        chatRoom = event.room
                        _chatViewState.postValue(currentState.copy(alertState = true))
                    }

                }
            }
        }
    }

   private fun reduce(event: ChatEvents,currentState: ChatState.Loading){
       when(event){
           is ChatEvents.Loading->loadRooms()
           is ChatEvents.ExitRoom -> loadRooms()
           is ChatEvents.GoToRoom->goToRoom(event.room)
           else->{}//{loadRooms()}
       }
   }
    private fun reduce(event: ChatEvents,currentState: ChatState.InRoom){
        when (event){
            is ChatEvents.SendMessage-> sendMessage(event)
            is ChatEvents.NewMessageChange -> _chatViewState.postValue(currentState.copy(newMessage = event.newValue))
            is ChatEvents.ChangeAlertDialogState -> _chatViewState.postValue(currentState.copy(alertExitDialog = !currentState.alertExitDialog))
            is ChatEvents.ExitRoom-> exitRoom()
            is ChatEvents.RoomDelited ->roomDeleted(event)
            is ChatEvents.RemoveUsers -> removeUsers(event,currentState)
            is ChatEvents.getUsersInRoom ->getUsersInRoom(currentState)
            is ChatEvents.ClosedUserList -> _chatViewState.postValue(currentState.copy(userListAlert = false))
            is ChatEvents.ClickAfterRemove -> _chatViewState.postValue(ChatState.Loading)

          else ->{}
        }
    }
    private fun getUsersInRoom(currentState: ChatState.InRoom){
        viewModelScope.launch(Dispatchers.IO){
           val room =  firebaseHelper.getRoom(currentState.room.admin?.id!!)
            room?.let {
                _chatViewState.postValue(currentState.copy(room = room,
                    userListAlert = true,
                    userAdmin = currentState.room.admin!!.id==userProfile.id))
            }
        }
    }
    private fun removeUsers(event: ChatEvents.RemoveUsers,currentState: ChatState.InRoom){
        viewModelScope.launch(Dispatchers.IO) {
            _chatViewState.postValue(currentState.copy(userListAlert = false))
            if (firebaseHelper.removeUsers(chatRoom,event.removeUser)){
                event.removeUser.forEach{
                    sendSystemMessage(it,chatRoom)

                }
            }


        }
    }
    private fun sendSystemMessage(user:UserProfile,room:ChatRoomBase){
        viewModelScope.launch {
            firebaseHelper.setMessage(room=room, message = Message(message = user.login))
        }

    }
    private fun roomDeleted(event: ChatEvents.RoomDelited){
        chatRoom = ChatRoomBase()
        _chatViewState.postValue(ChatState.Loading)
    }
    private fun loadRooms() {
       viewModelScope.launch(Dispatchers.IO) {

           val roomID = roomPref.getString(ROOM_ID,"DEFAULT")
           if(roomID!=null&&roomID != "DEFAULT"&&roomID!=""){
               val room = firebaseHelper.getRoom(roomID)
              if (room!=null){
                  for(c in room.party?.indices!!){
                    if ( room.party[c].id == userProfile.id){
                        goToRoom(room)
                        Log.d(TAG,"вход в ранее созданную комнату")
                    }else{


                        val rooms  = firebaseHelper.getChatRooms()
                        if (rooms!=null) {
                            _chatViewState.postValue(ChatState.RoomList(rooms))
                        }
                    }
                  }
              }else{
                  val list = ArrayList<UserProfile>()
                  list.add(userProfile)
                  val newRoom = ChatRoomBase(
                      roomName = roomPref.getString(ROOM_NAME,"DEFAULT"),
                      accesRoom =roomPref.getBoolean(ROOM_ACESS,false),
                      password = roomPref.getString(ROOM_PASSWORD,"DEFAULT"),
                      party = list, admin = userProfile,
                      size = roomPref.getInt(ROOM_SIZE,5))
                 if ( firebaseHelper.createUserRoom(newRoom,userProfile)
                 ){
                     Log.d(TAG,"комната создана")
                     settingsSaver.changeLocalUser(
                         USER_PROFILE_ROOM_CREATE_QUANTITY,
                         null,
                         userProfile.roomsCreateQuantity?.let{it+1},
                         null)
                   _chatViewState.postValue(ChatState.GoToRoom(newRoom))
                 }else{
                     Log.d(TAG,"ошибка создания комнаты")
                 }
              }
               }else{
           val rooms  = firebaseHelper.getChatRooms()
           if (rooms!=null){
               _chatViewState.postValue(ChatState.RoomList(rooms))
               Log.d(TAG,"Комнаты загружены")
           }}
       }

   }
    private fun goToRoom(room:ChatRoomBase) {
        viewModelScope.launch(Dispatchers.IO) {
            var status = true
            for (i in room.party?.indices!!){
                if ( room.party[i].id==userProfile.id ){
                    status = false
                }
            }
            if (status){
                firebaseHelper.setMessage(
                    room, message = Message(user = userProfile, message = ""))
                firebaseHelper.setNewUserInRoom(room = room, userProfile = userProfile)
                chatRoom = room
                _chatViewState.postValue(ChatState.LoadingMessageInRoom)
                messageLisener(room)
            }


            if (room.admin?.id == userProfile.id) {
//                firebaseHelper.setMessage(
//                    room, message = Message(user = userProfile, message = ""))
               chatRoom = room
                _chatViewState.postValue(ChatState.LoadingMessageInRoom)
                messageLisener(room)
               userInRoomLisenerCreate()
            }
        }
    }

    private fun goToRoom(event: ChatEvents.RoomSelected,currentState: ChatState.RoomList){
     viewModelScope.launch(Dispatchers.IO) {
         //потом убрать
         if (event.room.admin?.id != userProfile.id) {
             firebaseHelper.setNewUserInRoom(room = event.room, userProfile = userProfile)
             firebaseHelper.setMessage(
                event.room,
                message = Message(user = userProfile, message = ""))
         }
         chatRoom = event.room

             _chatViewState.postValue(ChatState.LoadingMessageInRoom)
              messageLisener(event.room)
         userInRoomLisenerCreate()

//         if (firebaseHelper.setMessage(
//                 event.room,
//                 message = Message(user = userProfile, message = "")
//             )
//         ) {
//             chatRoom = event.room
//             _chatViewState.postValue(ChatState.LoadingMessageInRoom)
//             messageLisener(event.room,false)
//         }
     }

    }
    private fun sendMessage(event: ChatEvents.SendMessage){
        viewModelScope.launch {
            firebaseHelper.setMessage(room = chatRoom,
                message = Message(user =userProfile , message = event.message))
            settingsSaver.changeLocalUser(USER_PROFILE_MESSAGE_QUANTITY,
                null,
                userProfile.messageQuantity?.let { it+1 },
                null)

        }
    }
   private fun userInRoomLisenerCreate(){
       val database = Firebase.database
       val roomRef = database.getReference(CHAT_ROOMS_REF).child(chatRoom.admin?.id!!).child("party")
       userInRoomListener = roomRef.addValueEventListener(object :ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
               val userList = ArrayList<UserProfile>()

               for (s in snapshot.children) {

                   val user = s.getValue(UserProfile::class.java)
                   if (user != null) {
                       userList.add(user)
                   }
               }
               chatRoom = chatRoom.copy(party = userList)
               var check = false
               userList.forEach {
                   if (it.id==userProfile.id){
                       check = true
                   }
               }
               if (!check){
                   exitAfterRemove ()
               }


           }
           override fun onCancelled(error: DatabaseError) {
               roomRef.removeEventListener(this)
           }
       })
   }

    private fun messageLisener(room: ChatRoomBase) {
        val database = Firebase.database
        val roomRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
        if (messageLisener == null){
            messageLisener = roomRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messList = ArrayList<Message>()

                    for (s in snapshot.children) {

                        val message = s.getValue(Message::class.java)
                        if (message != null) {
                            messList.add(message)
                        }
                    }
                        if (messList.size == 0) {
                            roomRef.child(roomRef.push().key?: userProfile.id!!).setValue(Message(user = userProfile, message = ""))
                            Log.d(TAG,"Данные отправлены ")
                        } else {
                                _chatViewState.postValue(ChatState.InRoom(messList, room = chatRoom))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    roomRef.removeEventListener(this)
                }
            })
    }else{
        viewModelScope.launch {
          val messages =  firebaseHelper.getMessages(room)
            if (messages.size!=0)
            {
                _chatViewState.postValue(ChatState.InRoom(messages, room = chatRoom))
            }

        }
    }
    }
    private fun exitAfterRemove (){
        viewModelScope.launch(Dispatchers.IO) {
            val room = firebaseHelper.getRoom(chatRoom.admin?.id!!)
            if (room!=null){
                val editor = roomPref.edit()
                editor.clear()
                editor.apply()
                val database = Firebase.database
                var roomRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
                messageLisener?.let {
                    roomRef.removeEventListener(it)
                }
                messageLisener =null
                roomRef = database.getReference(CHAT_ROOMS_REF).child(chatRoom.admin?.id!!).child("party")
                userInRoomListener?.let {
                    roomRef.removeEventListener(it)
                }
                userInRoomListener = null
                chatRoom = ChatRoomBase()
                _chatViewState.postValue(ChatState.ExpelledUser)

            }
        }
    }


    private fun exitRoom(){
        viewModelScope.launch(Dispatchers.IO) {
            val room = firebaseHelper.getRoom(chatRoom.admin?.id!!)
            if (room!=null){
                val database = Firebase.database
                var roomRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
                messageLisener?.let {
                    roomRef.removeEventListener(it)
                }
                roomRef = database.getReference(CHAT_ROOMS_REF).child(chatRoom.admin?.id!!).child("party")
                userInRoomListener?.let {
                    roomRef.removeEventListener(it)
                }
                firebaseHelper.setMessage(room, message = Message(userProfile,"exit"))
            if( firebaseHelper.exitRoom(room,userProfile)){
                val editor = roomPref.edit()
                editor.clear()
                editor.apply()
                _chatViewState.postValue(ChatState.Loading)
                Log.d(TAG,"вышли из комнаты")
            }}

        }



    }

    }