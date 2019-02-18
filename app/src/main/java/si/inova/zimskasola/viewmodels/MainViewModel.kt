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
import java.io.File


class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"
    private val db = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    private lateinit var location: MutableLiveData<Location>

    private val mStorageRef: StorageReference = FirebaseStorage.getInstance().reference


    fun getUserEmail(): String? = auth.currentUser!!.email
    fun getUserName(): String? = auth.currentUser!!.displayName
    fun getUserPhotoUrl(): Uri? = auth.currentUser!!.photoUrl

    fun getLocationData(): LiveData<Location> {
        Log.d(TAG, "getLocationData()")
        return location
    }

    fun init() {
        Log.d(TAG, "init()")
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
    }




}