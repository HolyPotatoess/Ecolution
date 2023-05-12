package my.edu.tarumt.ecolution.admin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarumt.ecolution.MainMenuActivity
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.ActivityAdminCheckBinding
import my.edu.tarumt.ecolution.databinding.ActivityLoginBinding


class AdminCheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCheckBinding

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        //Hide & show Password
        binding.loginHidePasswordId.tag = 1
        binding.loginHidePasswordId.setOnClickListener {
            if(binding.loginHidePasswordId.tag == 1){
                binding.companyEcolutionId.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.loginHidePasswordId.setImageResource(R.drawable.ic_visibility_on)
                binding.loginHidePasswordId.tag = 2
            } else{
                binding.companyEcolutionId.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.loginHidePasswordId.setImageResource(R.drawable.ic_visibility_off)
                binding.loginHidePasswordId.tag = 1
            }
        }

        //Hide & show Password
        binding.loginHidePasswordKey.tag = 1
        binding.loginHidePasswordKey.setOnClickListener {
            if(binding.loginHidePasswordKey.tag == 1){
                binding.companyEcolutionKey.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.loginHidePasswordKey.setImageResource(R.drawable.ic_visibility_on)
                binding.loginHidePasswordKey.tag = 2
            } else{
                binding.companyEcolutionKey.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.loginHidePasswordKey.setImageResource(R.drawable.ic_visibility_off)
                binding.loginHidePasswordKey.tag = 1
            }
        }

        //binding click, begin login
        binding.proceedButton.setOnClickListener{
            /*Steps
            * 1) Input Data
            * 2) Validate Data
            * 3) Login - Firebase Auth
            * 4) Check user type - Firebase Auth
            * If - Move to user dashboard
            * Id Admin - Move to admin dashboard
            * */
            validateData()
        }
    }

    private var companyId = ""
    private var companyKey= ""


    private fun validateData(){
        //1) Input Data
        companyId = binding.companyEcolutionId.text.toString().trim()
        companyKey = binding.companyEcolutionKey.text.toString().trim()

        val ref = FirebaseDatabase.getInstance().getReference("AdminTool")

        //2) Validate Data
        if (companyId.isEmpty()){
            Toast.makeText(this,"Please Enter Your Company Id", Toast.LENGTH_SHORT).show()
        }

        else if (companyKey.isEmpty()) {
            Toast.makeText(this,"Please Enter Your Company Key", Toast.LENGTH_SHORT).show()
        }
        else {
            val ref = FirebaseDatabase.getInstance().getReference("AdminTool")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(companyId)) {
                        // Company ID exists
                        val companySnapshot = snapshot.child(companyId)
                        val getCompanyKey = companySnapshot.child("companyKey").getValue(String::class.java)

                        if (getCompanyKey == companyKey) {
                            // companyKey matches, login admin
                            val intent = Intent(this@AdminCheckActivity, AdminToolActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@AdminCheckActivity, "Admin Login Successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AdminCheckActivity, "Company Key Not Match", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Company ID does not exist
                        Toast.makeText(this@AdminCheckActivity, "Company ID not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        }
    }



}