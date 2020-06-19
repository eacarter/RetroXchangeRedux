package com.erickson.retroxchange.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.retroxchange.datamodels.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject


class DatabaseManager @Inject constructor(){

    private val data: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storage: FirebaseStorage by lazy{
        FirebaseStorage.getInstance()
    }

    fun initializeUser(firebaseAuth: FirebaseAuth, context: Context){
       val map = mutableMapOf(
           "id" to "",
           "username" to "",
           "firstname" to "",
           "lastname" to "",
           "city" to "",
           "state" to "",
           "about" to "",
           "profile_image" to "",
           "latitude" to 0.0,
           "longitude" to 0.0,
           "friends" to arrayListOf<String>(),
           "portfolio" to arrayListOf<String>()
       );

        data.collection("User")
            .document(firebaseAuth.currentUser!!.uid)
            .set(map, SetOptions.merge())
            .addOnSuccessListener{
               Log.d("Database", "User Created")
            }
            .addOnFailureListener {
               Log.d("Database", "User Creation Failed")
            }
    }

    fun getUser(id : String) : LiveData<UserData>{
        val user = MediatorLiveData<UserData>()
        data.collection("User").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null){
                    user.postValue(documentSnapshot.toObject(UserData::class.java))
                }
            }
        return user
    }

    fun updateUser(user: FirebaseUser): DocumentReference{
        val userId = data.collection("Users").document(user.uid)
        return userId
    }

    fun findUsers(username : Array<String>, id: String): LiveData<ArrayList<UserData>>{
        val user = MediatorLiveData<ArrayList<UserData>>()
        data.collection("User").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val list = ArrayList<UserData>()
                for(document : QueryDocumentSnapshot in task.getResult()!!){
                    for(i in username){
                        if (i.contains(document["id"].toString()) && !i.contains(id)) {
                            list.add(document.toObject(UserData::class.java))
                        }
                    }
                }
                user.postValue(list)
            }
        }
        return user
    }

    fun addFriend(firebaseUser: FirebaseUser, userID: String){
        data.collection("Users")
            .document(firebaseUser.getUid())
            .update("friends", FieldValue.arrayUnion(userID))
            .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Log.d("Friends", "friend added");
                    }
            }
    }

    fun removeFriend(firebaseUser:FirebaseUser, userID: String){
        data.collection("Users")
            .document(firebaseUser.getUid())
            .update("friends", FieldValue.arrayRemove(userID))
            .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Log.d("Friends", "friend removed");
                    }
            }
    }

    fun getFriends(id: String) : LiveData<List<Any>>{
        val friendsList =  MediatorLiveData<List<Any>>();
        data.collection("Users")
            .document(id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot: DocumentSnapshot = task.getResult()!!
                    if (documentSnapshot.exists()) {
                        val list  = listOf(listOf(documentSnapshot.get("friends")))
                        friendsList.postValue(list)
                    }
                }
            }
        return friendsList;
    }


    fun uploadProfileImage(file: Uri, user: FirebaseUser){
        if(file != null){
            val image = storage.getReference().child("profile_images/" + user.getUid())
            image.putFile(file).addOnCompleteListener {
                if (it.isSuccessful){
                    val image = storage.getReference().child("profile_images/" + user.getUid())
                    image.downloadUrl.addOnSuccessListener{
                        updateUser(user).update("profile_image", it.toString())
                    }
                }
            }
        }
    }

    fun uploadDiscussionImage(file: Uri, discussId: String){
        if(file != null){
            val image = storage.getReference().child("discussion_images/" + discussId)
            image.putFile(file).addOnCompleteListener {
                if (it.isSuccessful){
                    val image = storage.getReference().child("discussion_images/" + discussId)
                    image.downloadUrl.addOnSuccessListener{
//                        updateUser(user).update("profile_image", it.toString())
                    }
                }
            }
        }
    }

    fun getItem(){

    }

    fun addItem(){

    }


}