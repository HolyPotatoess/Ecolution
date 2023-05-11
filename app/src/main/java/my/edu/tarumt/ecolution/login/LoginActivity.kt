package my.edu.tarumt.ecolution.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import my.edu.tarumt.ecolution.MainMenuActivity
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.admin.AdminCheckActivity
import my.edu.tarumt.ecolution.databinding.ActivityLoginBinding
import my.edu.tarumt.ecolution.register.RegisterActivity


class LoginActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityLoginBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //no action bar
        supportActionBar?.hide()
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click no account , got register screen
        binding.createAccount.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.loginForgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        binding.adminCheck.setOnClickListener{
            startActivity(Intent(this, AdminCheckActivity::class.java))
        }
        //Hide & show Password
        binding.loginHidePassword.tag = 1
        binding.loginHidePassword.setOnClickListener {
            if(binding.loginHidePassword.tag == 1){
                binding.loginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.loginHidePassword.setImageResource(R.drawable.ic_visibility_on)
                binding.loginHidePassword.tag = 2
            } else{
                binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.loginHidePassword.setImageResource(R.drawable.ic_visibility_off)
                binding.loginHidePassword.tag = 1
            }
        }



        //binding click, begin login
        binding.loginButton.setOnClickListener{
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

    private var email = ""
    private var password= ""

    private fun validateData(){
        //1) Input Data
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        //2) Validate Data
        if (email.isEmpty()){
            Toast.makeText(this,"Please Enter Your Email", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Patterns", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()) {
            Toast.makeText(this,"Please Enter a password", Toast.LENGTH_SHORT).show()
        }
        else {
            loginUser()
        }
    }
    private fun loginUser(){
        //3) login - Firebase Auth

        //show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainMenuActivity::class.java))

            }
            .addOnFailureListener{ e->
                //failed login
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed. ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}