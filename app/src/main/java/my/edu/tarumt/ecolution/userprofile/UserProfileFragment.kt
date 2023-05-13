package my.edu.tarumt.ecolution.userprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.ActivityLoginBinding
import my.edu.tarumt.ecolution.databinding.FragmentUserProfileBinding
import my.edu.tarumt.ecolution.login.LoginActivity

class UserProfileFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("msg", "loaded")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val updateProfileFragment = UpdateProfileFragment()
        val userProfileFragment = UserProfileFragment()
        val userQrCodeFragment = UserQrCodeFragment()
        binding.updateBttn.setOnClickListener {
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_wrapper, updateProfileFragment)
            transaction.commit()

            /*var fc = fragmentManager?.beginTransaction()
            fc?.replace(R.id.navigation_user, updateProfileFragment)
            fc?.commit()*/
        }
        getDatabaseData(binding)

        binding.changeProfilePicBttn.setOnClickListener {
            pickImageGallery()
        }

        binding.logoutBttn.setOnClickListener{
            startActivity(Intent(this.requireContext(), LoginActivity::class.java))
        }

        // Plan B
        binding.refreshBttn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                remove(userProfileFragment)
                replace(R.id.fl_wrapper, userProfileFragment)
                commit()
            }
        }

        binding.imageButtonQrcode.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                remove(userProfileFragment)
                replace(R.id.fl_wrapper, userQrCodeFragment)
                commit()
            }
        }

        return binding.root
    }

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    private fun getDatabaseData(binding: FragmentUserProfileBinding){

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
            binding.profileUsername.text = dtUsername.toString()
            binding.profileEmail.text = dtEmail.toString()
            binding.profileNationality.text = dtNationality.toString()
            binding.profilePhoneNo.text = dtPhoneNo.toString()
            binding.bioInfo.text = dtBioInfo.toString()
            binding.profileGender.text = dtGender.toString()
            binding.profilePoints.text = dtTotalPoint.toString() + " pts"
            binding.profileRank.text = "No. " + dtRank.toString()
        }

        storageRef.child(firebaseAuth.uid!!).downloadUrl.addOnSuccessListener { Uri ->
            val imageUri = Uri.toString()
            val profilePicView = binding.profileImage
            Glide.with(this)
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
            binding.profileImage.setImageURI(data?.data)
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