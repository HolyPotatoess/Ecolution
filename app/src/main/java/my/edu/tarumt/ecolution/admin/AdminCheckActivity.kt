package my.edu.tarumt.ecolution.admin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.ActivityAdminCheckBinding


class AdminCheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCheckBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
            Toast.makeText(this,"Please Enter Your Email", Toast.LENGTH_SHORT).show()
        }

        else if (companyKey.isEmpty()) {
            Toast.makeText(this,"Please Enter a password", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "YOU ARE CORRECT NICE!", Toast.LENGTH_SHORT).show()
        }
    }
}