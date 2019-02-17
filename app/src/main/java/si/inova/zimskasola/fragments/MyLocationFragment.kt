package si.inova.zimskasola.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.my_location_fragment.*
import si.inova.zimskasola.activities.MainActivity
import si.inova.zimskasola.viewmodels.MainViewModel
import androidx.lifecycle.Observer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class MyLocationFragment : Fragment() {

    companion object {
        fun newInstance() = MyLocationFragment()
        private const val TAG = "MyLocationFragment"
    }

    private lateinit var viewModel: MainViewModel
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.zimskasola.R.layout.my_location_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(MyLocationViewModel::class.java)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initUI()

        loadData()


    }

    private fun loadData() {

        viewModel.getLocations().observe(this, Observer { locations ->
            run {
                Log.d(TAG, locations.toString())
            }
        })


        /* val database = FirebaseDatabase.getInstance().reference
         val ref = database.child("locations")
         /*val database = FirebaseDatabase.getInstance()
         val myRef = database.getReference()

         // Read from the database
         ref.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(dataSnapshot: DataSnapshot) {
                 // This method is called once with the initial value and again
                 // whenever data at this location is updated.
                 val value = dataSnapshot.getValue(String::class.java)
                 Log.d(TAG, "Value is: $value")
             }

             override fun onCancelled(error: DatabaseError) {
                 // Failed to read value
                 Log.w(TAG, "Failed to read value.", error.toException())
             }
         })
         */
         */
        val db = FirebaseFirestore.getInstance()
        db.collection("locations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, document.id + " => " + document.data)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


    }

    private fun initUI() {
        (activity as MainActivity).hideLogout()

        val handler = Handler()
        handler.postDelayed({
            showLocationDetails()
        }, 2500)
    }

    private fun showLocationDetails() {
        l_details?.visibility = View.VISIBLE
        l_searching?.visibility = View.INVISIBLE


    }


}
