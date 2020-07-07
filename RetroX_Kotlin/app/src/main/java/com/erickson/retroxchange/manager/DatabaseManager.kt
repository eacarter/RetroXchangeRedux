package com.erickson.retroxchange.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.retroxchange.datamodels.DiscussionCommentData
import com.erickson.retroxchange.datamodels.DiscussionData
import com.erickson.retroxchange.datamodels.UserInfoData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject


class DatabaseManager @Inject constructor(){

    private val data: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val storage: FirebaseStorage by lazy{
        FirebaseStorage.getInstance()
    }
    private val postId: MediatorLiveData<String> = MediatorLiveData()

    fun initializeUser(firebaseAuth: FirebaseAuth){
       val map = mutableMapOf(
           "id" to "",
           "username" to firebaseAuth.currentUser!!.email!!.substringBefore('@'),
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

    fun getUser(id : String) : LiveData<UserInfoData>{
        val user = MediatorLiveData<UserInfoData>()
        data.collection("User").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot != null){
                    user.postValue(documentSnapshot.toObject(UserInfoData::class.java))
                }
            }
        return user
    }

    fun updateUser(user: FirebaseUser): DocumentReference{
        val userId = data.collection("User").document(user.uid)
        return userId
    }

    fun findUsers(username : Array<String>, id: String): LiveData<ArrayList<UserInfoData>>{
        val user = MediatorLiveData<ArrayList<UserInfoData>>()
        data.collection("User").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val list = ArrayList<UserInfoData>()
                for(document : QueryDocumentSnapshot in task.getResult()!!){
                    for(i in username){
                        if (i.contains(document["id"].toString()) && !i.contains(id)) {
                            list.add(document.toObject(UserInfoData::class.java))
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

    fun uploadDiscussionImage(file: Uri, discussId: String){
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

    fun uploadDiscussionFeed( id: String, item: DiscussionData){
        data.collection("Discussions").document(id).set(item).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("DatabaseManager", "discussion uploaded")
            }
        }
    }

    fun getDiscussionfeed() : LiveData<ArrayList<DiscussionData>>{
        val feed : MutableLiveData<ArrayList<DiscussionData>> = MutableLiveData()

        data.collection("Discussions")
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val list: ArrayList<DiscussionData> = ArrayList<DiscussionData>()
                    val doc: QuerySnapshot = task.getResult()!!
                    for (n in doc){
                        list.add(n.toObject(DiscussionData::class.java))
                    }
                    feed.postValue(list)
                }
            }
        return feed
    }

    fun getDiscussionfeedComments(id: String) : LiveData<ArrayList<DiscussionCommentData>>{
        val feed : MutableLiveData<ArrayList<DiscussionCommentData>> = MutableLiveData()

        data.collection("Discussions").document(id).collection("Comments")
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val list: ArrayList<DiscussionCommentData> = ArrayList<DiscussionCommentData>()
                    val doc: QuerySnapshot = task.getResult()!!
                    for (n in doc){
                        list.add(n.toObject(DiscussionCommentData::class.java))
                    }
                    feed.postValue(list)
                }
            }
        return feed
    }

    fun getUserDiscussionfeed(id: String) : LiveData<ArrayList<DiscussionData>>{
        val feed : MutableLiveData<ArrayList<DiscussionData>> = MutableLiveData()

        data.collection("Discussions").whereEqualTo("userId", id)
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val list: ArrayList<DiscussionData> = ArrayList<DiscussionData>()
                    val doc: QuerySnapshot = task.getResult()!!
                    for (n in doc){
                        list.add(n.toObject(DiscussionData::class.java))
                    }
                    feed.postValue(list)
                }
            }
        return feed
    }

    fun uploadDiscussionComment(id: String, postId: String, item: DiscussionCommentData){
        data.collection("Discussions").document(id).collection("Comments")
            .document(postId).set(item).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("DatabaseManager", "comment uploaded")
                }
            }
    }

    fun getDiscussionSpecific(id: String) : LiveData<DiscussionData>{
        val feed : MutableLiveData<DiscussionData> = MutableLiveData()
        data.collection("Discussions").document(id).get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val doc: DocumentSnapshot = task.getResult()!!
                    feed.postValue(doc.toObject(DiscussionData::class.java))
                }
            }
        return feed
    }
    
    fun updateDiscussionCount(id: String, userID: String){
        data.collection("Discussions").document(id).update("starredUsers", FieldValue.arrayUnion(userID)).
                addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Log.d("Database", "updated count")
                    }
                }
    }

    fun removeDiscussionCount(id: String, userID: String){
        data.collection("Discussions").document(id).update("starredUsers", FieldValue.arrayRemove(userID)).
        addOnCompleteListener {task ->
            if(task.isSuccessful){
                Log.d("Database", "updated count")
            }
        }
    }

//    fun updateDiscussionCommentCount(id: String, userID: String){
//        data.collection("Discussions").document(id).collection("Comments")..update("starredUsers", FieldValue.arrayUnion(userID)).
//        addOnCompleteListener {task ->
//            if(task.isSuccessful){
//                Log.d("Database", "updated count")
//            }
//        }
//    }
//
//    fun removeDiscussionCommentCount(id: String, userID: String){
//        data.collection("Discussions").document(id).update("starredUsers", FieldValue.arrayRemove(userID)).
//        addOnCompleteListener {task ->
//            if(task.isSuccessful){
//                Log.d("Database", "updated count")
//            }
//        }
//    }

    fun getItem(){

    }

    fun addItem(){

    }


}