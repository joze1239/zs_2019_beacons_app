package si.inova.zimskasola.viewmodels

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


class LocationViewModel : ViewModel() {

    private val TAG = "LocationViewModel"
    private val db = FirebaseFirestore.getInstance()


    private lateinit var locations: MutableLiveData<List<Location>>


    fun init() {

    }

    fun getLocations(): LiveData<List<Location>> {
        locations = MutableLiveData()

        db.collection("locations")
            .get()
            .addOnSuccessListener { result ->

                Log.d(TAG, result.metadata.toString())
                locations.value = result.toObjects(Location::class.java)
                for (document in result) {
                    db.collection("locations").document(document.id).collection("places")
                        .get()
                        .addOnSuccessListener { places ->
                            for (place in places) {
                                Log.d(TAG, place.id + " => " + place.data)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error getting documents: ", exception)
                        }
                }


            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        /*  db.collection("locations")
              .get()
              .addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                      for (document in task.result!!) {
                          Log.d(TAG, document.id + " => " + document.data)

                          db.collection("locations").document(document.id)
                              .get()
                              .addOnCompleteListener { task ->
                                  if (task.isSuccessful) {
                                      for (document in task.result!!) {
                                          Log.d(TAG, document.id + " => " + document.data)
                                      }
                                  } else {
                                      Log.d(TAG, "Error getting documents: ", task.exception)
                                  }
                              }

                      }
                  } else {
                      Log.d(TAG, "Error getting documents: ", task.exception)
                  }
              }
  */
        return locations
    }
}