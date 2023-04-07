package com.example.dollarschat.data

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Room
import com.example.dollarschat.data.localBase.AvatarBase
import com.example.dollarschat.data.localBase.AvatarDatabase.AvatarEntity
import com.example.dollarschat.data.localBase.AvatarDatabase.Database
import com.example.dollarschat.screens.Chat.ChatViewModel
import com.example.dollarschat.screens.Chat.models.ChatEvents
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
const val  avatarsRef = "Avatars"
const val PACK_REFERENCE = "PackBase"
const val USER_PROFILE_REFERENCE = "UserProfile"
const val DATABASE_NAME = "avatarBase"
const val CHAT_ROOMS_REF= "chatReferense"
const val MESSAGE_ROOM_REF = "messageRef"


class FirebaseHelper(context: Context) {
   private var currentUser = Firebase.auth.currentUser
    private val TAG = "FirebaseHelper"
    private val context = context


    suspend fun authCheck():Boolean{
        return suspendCoroutine { continuation ->
            if (currentUser==null){
                continuation.resume(false)
            }else{
                continuation.resume(true)
            }
        }
    }
    suspend fun userProfileCheck():UserProfile?{
        return suspendCoroutine { continuation ->
            if (currentUser==null){
                continuation.resume(null)
            }
            currentUser?.let {currentUser->
                val dataBase = Firebase.database
                val userRef = dataBase.getReference(USER_PROFILE_REFERENCE)
                userRef.child(currentUser.uid).get().addOnSuccessListener {
                    val c = it.children
                    val userProfile = it.getValue(UserProfile::class.java)
                    if (userProfile == null) {
                        continuation.resume(null)
                    } else {
                        continuation.resume(userProfile)
                    }
                }.addOnFailureListener {
                    continuation.resume(null)
                }
            }
        }
        }
    suspend fun signInUser(email:String,pass:String):Boolean{
        return suspendCoroutine { continuation ->
            val TAG = "AUTH"
            Firebase.auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                        Log.d(TAG, "Пользователь вошел ")
                    } else {
                        Log.d(TAG, "Пользователь не вошел")
                        continuation.resume(false)
                    }
                }
        }
    }
    suspend  fun registration(email:String,pass:String):Boolean{
       return suspendCoroutine { continuation ->
           Firebase.auth.createUserWithEmailAndPassword(email, pass)
               .addOnCompleteListener() { task ->
                   if (task.isSuccessful) {
                       Log.d(TAG,"Успешная регистрация")
                       continuation.resume(true)
                   } else {
                       Log.d(TAG,"не Успешная регистрация")
                       continuation.resume(false)
                   }
               }.addOnFailureListener {
                   Log.d(TAG,"FailureListener")
                   continuation.resume(false)
               }
        }
    }
    suspend fun createUserProfile(login:String, avatar:Avatars,pref:SharedPreferences,imahePref:SharedPreferences):Boolean{
        return suspendCoroutine { continuation ->
            currentUser?.let {
                val profileUpdates = userProfileChangeRequest {
                displayName = login
                //setDisplayName(event.userLogin)
            }
                currentUser!!.updateProfile(profileUpdates).addOnCompleteListener(){task->
                    if(task.isSuccessful){
                        Log.d(TAG,"Логин изменен")
                    }
                }.addOnFailureListener{
                    continuation.resume(false)
                    Log.d(TAG,"Логин не изменен")
                }

                val userProfile = UserProfile(login = login,
                    avatar = avatar,
                    accessibleAvatars = "StartedPack",
                    id = currentUser!!.uid,
                    messageQuantity = 0,
                    roomsCreateQuantity = 0)
                Log.d(TAG,"user profile  создан")
                Settings(context).setLocalUser(userProfile)
                val database = Firebase.database
                val userRef = database.getReference(USER_PROFILE_REFERENCE)
                userRef.child(currentUser!!.uid).setValue(userProfile)
                Log.d(IMAGE_COLOR,"Данные отправлены ")
                    continuation.resume(true)

            }
        }
    }
    suspend fun getPackAvatar(pack: String,acces:Boolean): ArrayList<Avatars> {
        return suspendCoroutine { continuation ->
            var avatarList = ArrayList<Avatars>()
            val database = Firebase.database
            val avatarRef = database.getReference(avatarsRef).child(pack)
            avatarRef.get().addOnSuccessListener {
                for (c in it.children) {
                    val avatar = c.getValue(AvatarBase::class.java)
                    if (avatar != null) {
                        val avatarAcces:Avatars = Avatars(image_name = avatar.imageName,
                            iventPackageName = avatar.pack, storageReference = avatar.storageRef, accessAvatar = acces, color = avatar.color )
                        avatarList.add(avatarAcces)
                    }
                }
                continuation.resume(avatarList)
            }.addOnFailureListener {
                continuation.resume(error("Error"))
            }
        }
    }
    suspend fun saveAvatarInFile(avatar: Avatars): File? {
        return suspendCoroutine { continuation ->
                val path: File = context.filesDir
                var file = File(path, avatar.image_name)
                val storage = Firebase.storage("gs://dollarschat-a45c3.appspot.com")
                var storageRef = storage.reference
                var imageRef = storageRef.child(avatar.storageReference)
                if (file.exists())
                {continuation.resume(null)
                }else {
                    try {
                        imageRef.getFile(file).addOnSuccessListener {
                            Log.d("loadInFile", "complete")
                            continuation.resume(file)
                        }.addOnFailureListener {
                            continuation.resume(null)
                            Log.d("loadInFile", "errpe")
                        }
                    } catch (e: FileNotFoundException) {
                        continuation.resume(null)
                    } catch (e: IOException) {
                        continuation.resume(null)
                    }
                }
        }
    }
    fun paintBitmap(name:String): ImageBitmap? {
        val bitmap: Bitmap
        val path: File = context.filesDir
        var file = File(path, name)
        if (file.exists()) {
            val byteArray = file.readBytes()
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            return bitmap.asImageBitmap()
        }else return null
    }
    suspend fun getChatRooms():ArrayList<ChatRoomBase>?{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val roomRef = database.getReference(CHAT_ROOMS_REF)
            roomRef.get().addOnSuccessListener{
                val roomsList = ArrayList<ChatRoomBase>()

                for (s in it.children) {
                    val room = s.getValue(ChatRoomBase::class.java)
                    if (room != null) {
                        roomsList.add(room)
                    }
                }
                continuation.resume(roomsList)
            }
            }
        }

        fun getLoacalUser():UserProfile?{
            var imagePref:SharedPreferences = context.getSharedPreferences(TABLE_NAME_AVATARS,Context.MODE_PRIVATE)
            var prefs: SharedPreferences = context.getSharedPreferences(TABLE_NAME,Context.MODE_PRIVATE)
            val user = UserProfile(login = prefs.getString(USER_PROFILE_NAME,""),
                messageQuantity =prefs.getInt(USER_PROFILE_MESSAGE_QUANTITY,0),
                roomsCreateQuantity = prefs.getInt(USER_PROFILE_ROOM_CREATE_QUANTITY,0), id = prefs.getString(
                    USER_PROFILE_ID,""), accessibleAvatars = prefs.getString(USER_PROFILE_ACCESSIBLE_AVATARS,""),
                avatar = Avatars(image_name = imagePref.getString(IMAGE_NAME,"")!!,
                    iventPackageName =imagePref.getString(IMAGE_IVENT_PACKEGE_NAME,"")!!,
                    storageReference = imagePref.getString(IMAGE_STORAGE_REF,"")!!,
                    accessAvatar = imagePref.getBoolean(IMEGE_ACCESSABLE,true),
                    color = imagePref.getString(IMAGE_COLOR,"blue")!! ))
            return if (user.login==""){
                null
            }else{
                user
            }
        }
 suspend fun getRoom(roomID:String):ChatRoomBase?{
     return suspendCoroutine { continuation ->
         val database = Firebase.database
         val avatarRef = database.getReference(CHAT_ROOMS_REF)
         avatarRef.child(roomID).get().addOnSuccessListener{
                 val room = it.getValue(ChatRoomBase::class.java)
                 if (room != null) {
                     continuation.resume(room)
                 }else{
                     continuation.resume(null)
                 }
         }
     }
 }
    suspend fun getLocalRoom():String?{
        return suspendCoroutine { continuation ->
            val prefs: SharedPreferences = context.getSharedPreferences(TABLE_NAME_ROOM,Context.MODE_PRIVATE)
            val room:String? = prefs.getString(ROOM_ID,"")
            continuation.resume(room)
        }
    }

    suspend fun createUserRoom(room:ChatRoomBase,user:UserProfile):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val userRef = database.getReference(CHAT_ROOMS_REF).child(room.admin?.id!!)
           userRef.setValue(room)
            continuation.resume(true)
        }

    }
    suspend fun setMessage(room: ChatRoomBase,message: Message):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val userRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
            userRef.child(userRef.push().key?: message.user?.id!!).setValue(message)
            Log.d(TAG,"Данные отправлены ")
            continuation.resume(true)
        }
    }
    suspend fun setSystemMessage(room: ChatRoomBase,message: Message):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val userRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
            userRef.child(userRef.push().key?: room.admin!!.id.toString()).setValue(message)
            Log.d(TAG,"Данные отправлены ")
            continuation.resume(true)
        }
    }

    suspend fun setNewUserInRoom(room: ChatRoomBase,userProfile: UserProfile):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val userRef = database.getReference(CHAT_ROOMS_REF).child(room.admin?.id!!)
            var list = ArrayList<UserProfile>()
            list= room.party!!
            var state = true
            list.forEach { UserProfile ->
                if (userProfile==UserProfile){
                 state=false
                }
            }
            if (state){
                list.add(userProfile)
            }

            userRef.child("party").setValue(list).addOnSuccessListener {
                continuation.resume(true)
            }.addOnFailureListener {
                continuation.resume(false)
            }

        }
    }

    suspend fun exitRoom(room:ChatRoomBase,userProfile: UserProfile):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val list = ArrayList<UserProfile>()
            val roomRef = database.getReference(CHAT_ROOMS_REF).child(room.admin?.id!!)
            if ((room.party?.size?:0) <= 1|| room.admin!!.id == userProfile.id){
                roomRef.removeValue()
                val meesegeref = database.getReference(MESSAGE_ROOM_REF).child(room.admin!!.id!!)
                meesegeref.removeValue()
                continuation.resume(true)
            }else{
                for (i in room.party?.indices!!){
                if (userProfile.id != room.party[i].id){
                    list.add(room.party[i])
                }
            }
                roomRef.child("party").setValue(list).addOnSuccessListener {
                    continuation.resume(true)
                }.addOnFailureListener {
                    continuation.resume(false)
                }  }
        }
    }

  suspend fun loadAllAvatarInMemory() = coroutineScope {
      val job = launch {
          val userProfile = userProfileCheck()
          Log.d(TAG,"$userProfile")
          val resPacks = avatarPacksLoader()
          Log.d(TAG,"$resPacks")
          var avatarRefList = ArrayList<Avatars>()
          Log.d(TAG,"Загружены данные всех автаров")

          // загрузка списка информации о всех аватарах
          for (i in resPacks.indices) {
              val acces = getAccesAvatar(userProfile)
              var accesPack:Boolean = false
              for (c in acces.indices){
                  if (resPacks[i]==acces[c]){
                      accesPack =true
                  }
              }
              val avatars = getPackAvatar(resPacks[i],accesPack)
              for (c in avatars.indices) {
                  avatarRefList.add(avatars[c])
              }
              Log.d(TAG,"$avatarRefList")
          }
          //перенести
          //сохранение всех аватаров в память
          val db = Room.databaseBuilder(
              context, Database::class.java,
              "avatarBase"
          ).build()
          val avaDao = db.avatarsDao()
          for (i in avatarRefList.indices) {
              //получаем файл
              val file = saveAvatarInFile(avatarRefList[i])
              file?.let {
                    val avatar = AvatarEntity.avatarCreator(avatarRefList[i])
                  try {
                      avaDao.createAvatar(avatar)
                      Log.d(TAG,"avatar ${avatar.image_name} загружен")
                  } catch (e: SQLiteConstraintException) {
                      Log.d(TAG,"avatar ${avatar.image_name} не загружен")
                  }
              }
          }
      }
      job.start()
  }
    suspend fun saveAvatarsInDatabase(){
        val db = Room.databaseBuilder(
            context, Database::class.java,
            "avatarBase"
        ).build()
        val avaDao = db.avatarsDao()
    }
    suspend fun avatarPacksLoader(): ArrayList<String> {
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val packRef = database.getReference(PACK_REFERENCE)
            val packsList = ArrayList<String>()
            packRef.get().addOnSuccessListener {
                for (c in it.children) {
                    val pack = c.getValue(String::class.java)
                    if (pack != null) {
                        packsList.add(pack)
                    }
                }
                continuation.resume(packsList)
            }.addOnFailureListener {
               //обработка ошибки загрузки
            }
        }
    }

    suspend fun getAccesAvatar(userProfile: UserProfile?):List<String>{
        return suspendCoroutine { continuation ->
            userProfile?.let {
                val packList = userProfile.accessibleAvatars!!.split('/')
                continuation.resume(packList)
            }


        }
    }
    suspend fun changeUserProfile(avatar:Avatars,login: String?):Boolean{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val userRef = database.getReference(USER_PROFILE_REFERENCE)
            userRef.child(currentUser!!.uid).child("avatar").setValue(avatar)
            login?.let {
                if (login!=""){
                    userRef.child(currentUser!!.uid).child("login").setValue(login)
                }

            }
            continuation.resume(true)
        }
    }
    suspend fun removeUsers(room: ChatRoomBase,removeUsers:ArrayList<UserProfile>):Boolean{
        return suspendCoroutine { continuation ->
            val newList=ArrayList<UserProfile>()
            val database = Firebase.database
            val userRef = database.getReference(CHAT_ROOMS_REF).child(room.admin?.id!!)
            for (i in removeUsers.indices){
                var state = true
                room.party?.forEach {
                   if (it.id==removeUsers[i].id) {
                       state = false
                   }
                    if (state){
                        newList.add(it)
                    }
                }
            }
           // userRef.child("party").removeValue()
            userRef.child("party").setValue(newList)
            continuation.resume(true)

        }
    }
    suspend fun getMessages(room: ChatRoomBase):ArrayList<Message>{
        return suspendCoroutine { continuation ->
            val database = Firebase.database
            val messageRef = database.getReference(MESSAGE_ROOM_REF).child(room.admin?.id!!)
            val messageList = ArrayList<Message>()
            messageRef.get().addOnSuccessListener {
                for (c in it.children) {
                    val message = c.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                continuation.resume(messageList)
            }


        }
    }


}