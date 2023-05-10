package my.edu.tarumt.ecolution.userprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.login.LoginActivity
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity

class UserProfileFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("msg", "loaded")
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val updateBttn = view.findViewById<Button>(R.id.update_bttn)
        val updateProfileFragment = UpdateProfileFragment()
        updateBttn.setOnClickListener{
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fl_wrapper, updateProfileFragment)
                commit()
            }
            /*var fc = fragmentManager?.beginTransaction()
            fc?.replace(R.id.navigation_user, updateProfileFragment)
            fc?.commit()*/
        }
        getDatabaseData(view)

        view.findViewById<Button>(R.id.change_profile_pic_bttn).setOnClickListener {
            pickImageGallery()
        }

        view.findViewById<Button>(R.id.logout_bttn).setOnClickListener{
            startActivity(Intent(this.requireContext(), LoginActivity::class.java))
        }

    }

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    private fun getDatabaseData(view: View){

        val database = FirebaseDatabase.getInstance().getReference("Users")
        val storageRef = Firebase.storage.getReference("Users")
        database.child(firebaseAuth.uid!!).get().addOnSuccessListener {
            val dtUsername = it.child("name").value
            val dtEmail = it.child("email").value
            val dtBioInfo = it.child("bioInfo").value
            val dtNationality = it.child("nationality").value
            val dtPhoneNo = it.child("phoneNumber").value
            val dtGender = it.child("gender").value
            val dtTotalPoint = it.child("totalPoints").value
            val dtRank = it.child("Rank").value
            view.findViewById<TextView>(R.id.profile_username).text = dtUsername.toString()
            view.findViewById<TextView>(R.id.profile_email).text = dtEmail.toString()
            view.findViewById<TextView>(R.id.profile_nationality).text = dtNationality.toString()
            view.findViewById<TextView>(R.id.profile_phoneNo).text = dtPhoneNo.toString()
            view.findViewById<TextView>(R.id.bio_info).text = dtBioInfo.toString()
            view.findViewById<TextView>(R.id.profile_gender).text = dtGender.toString()
            view.findViewById<TextView>(R.id.profile_points).text = dtTotalPoint.toString() + " pts"
            view.findViewById<TextView>(R.id.profile_rank).text = "No. " + dtRank.toString()
        }

        storageRef.child(firebaseAuth.uid!!).downloadUrl.addOnSuccessListener { Uri ->
            val imageUri = Uri.toString()
            val profilePicView = view.findViewById<CircleImageView>(R.id.profile_image)
            Glide.with(Fragment())
                .load(imageUri)
                .into(profilePicView)
        }
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            view?.findViewById<ImageView>(R.id.profile_image)?.setImageURI(data?.data)
        }
        val _picUri = data?.data
        if (_picUri != null) {
            saveImageToDatabase(_picUri)
            Toast.makeText(context, "Successfully upload Image", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Failed to upload Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToDatabase(picUri: Uri){
        val storageRef = FirebaseStorage.getInstance().getReference("Users/" + firebaseAuth.uid!!)
        storageRef.putFile(picUri).addOnSuccessListener{

        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }
}