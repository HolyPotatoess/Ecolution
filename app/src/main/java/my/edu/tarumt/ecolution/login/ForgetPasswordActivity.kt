package my.edu.tarumt.ecolution.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import my.edu.tarumt.ecolution.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityForgetPasswordBinding

    //firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, begin pass recover through firebase
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        binding.forgotPasswordResetButton.setOnClickListener{
            validateData()
        }

    }

    private var email = ""
    private fun validateData() {
        // 1) input Data
        email = binding.forgotPasswordEmail.text.toString().trim()

        //check data
        if (email.isEmpty()){
            Toast.makeText(this,"Please Enter Your Email", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        }
        else {
            resetPassword()
        }

    }

    private fun resetPassword() {
        progressDialog.setMessage(("Sending Password reset link to $email"))
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Email sent to \n$email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed Send. ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}