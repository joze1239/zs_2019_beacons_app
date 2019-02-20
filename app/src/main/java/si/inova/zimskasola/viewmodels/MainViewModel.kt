package si.inova.zimskasola.viewmodels

import android.net.Uri
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import si.inova.zimskasola.fragments.MyLocationFragment
import si.inova.zimskasola.models.Location
import java.nio.file.Files.exists
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import si.inova.zimskasola.models.Room
import java.io.File


class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"
    private val db = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    private lateinit var location: MutableLiveData<Location>
    private lateinit var currentRoom: MutableLiveData<Room>
    lateinit var selectedRoom: Room


    val mStorageRef: StorageReference = FirebaseStorage.getInstance().reference


    fun getUserEmail(): String? = auth.currentUser!!.email
    fun getUserName(): String? = auth.currentUser!!.displayName
    fun getUserPhotoUrl(): Uri? = auth.currentUser!!.photoUrl

    fun getLocationData(): LiveData<Location> = location
    fun getCurrentRoom(): LiveData<Room> = currentRoom

    fun init() {
        // Get location data from firebase
        location = MutableLiveData()
        val fileRef = mStorageRef.child("25022c4a-3035-11e9-bb6a-a5c92278bce1.json")
        val localFile = File.createTempFile("data", "json")

        fileRef.getFile(localFile).addOnSuccessListener {
            // Local temp file has been created
            var jsonText = localFile.readText()
            location.value = Gson().fromJson(jsonText, Location::class.java)
        }.addOnFailureListener {
            // Handle any errors
            Log.d(TAG, "error: fileRef.getFile")
        }

        // init current room
        currentRoom = MutableLiveData()
    }

    fun updateCurrentRoom(beaconId: String) {
        val floors = location.value?.floors
        if (floors != null) {
            for (floor in floors) {
                var room = floor.rooms.filter { it.beacon_id.equals(beaconId) }
                Log.d(TAG, "updateCurrentRoom ${room.toString()}")
                if (room.isNotEmpty()) {
                    var newRoom = room[0]
                    newRoom.floor = floor.name
                    currentRoom.value = newRoom
                    return
                }

            }
        }

        Log.d(TAG, "updateCurrentRoom(): no room found!")
        currentRoom.value = null
    }

    fun updateCurrentRoomLeaved() {
        currentRoom.value = null
    }

    fun updateSelectedRoom(room: Room) {
        selectedRoom = room
    }



}