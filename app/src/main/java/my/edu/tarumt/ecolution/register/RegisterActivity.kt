package my.edu.tarumt.ecolution.register


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.LTGRAY
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Trace.isEnabled
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns.*
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.ActivityRegisterBinding
import my.edu.tarumt.ecolution.login.LoginActivity
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap



class RegisterActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityRegisterBinding

    //Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseUser: FirebaseUser

    //nationality
    private val nationality = arrayOf("Nationality","Malaysia", "Singapore", "Brunei", "Indonesia", "Thailand")

    //get Date Value

    private var name = ""
    private var email = ""
    private var password = ""
    private var phoneNumber = ""
    private var country = ""
    private var datePick = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        //hide action bar
        supportActionBar?.hide()

        //init progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        //Date picker to choose Date of Birth
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        binding.dateOfBirth.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view,year,month, dayOfMonth ->
                binding.dateOfBirth.text = "DOB: " + dayOfMonth + "/" + (month + 1) + "/" + year
                datePick = "" + dayOfMonth + "/" + (month + 1) + "/" + year
                binding.dateOfBirth.setTextColor(Color.parseColor("#000000"));
            },year,month,day)
            //set max date after current date
            datePickerDialog.datePicker.maxDate = cal.timeInMillis;
            datePickerDialog.show()
        }


        //array adapter for nationality
        val arrayAdapter = ArrayAdapter (this@RegisterActivity, android.R.layout.simple_spinner_dropdown_item, nationality)
        binding.signupNationality.adapter = arrayAdapter

        binding.signupNationality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        //Hide & show Password
        binding.signupHidePassword.tag = 1
        binding.signupHidePassword.setOnClickListener {
            if(binding.signupHidePassword.tag == 1){
                binding.signupPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.signupHidePassword.setImageResource(R.drawable.ic_visibility_on)
                binding.signupHidePassword.tag = 2
            } else{
                binding.signupPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.signupHidePassword.setImageResource(R.drawable.ic_visibility_off)
                binding.signupHidePassword.tag = 1
            }
        }

        binding.signupHidePasswordConfirm.tag = 1
        binding.signupHidePasswordConfirm.setOnClickListener {
            if(binding.signupHidePasswordConfirm.tag == 1){
                binding.signupPasswordConfirm.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.signupHidePasswordConfirm.setImageResource(R.drawable.ic_visibility_on)
                binding.signupHidePasswordConfirm.tag = 2
            } else{
                binding.signupPasswordConfirm.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.signupHidePasswordConfirm.setImageResource(R.drawable.ic_visibility_off)
                binding.signupHidePasswordConfirm.tag = 1
            }
        }


        //back button tools
        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        binding.termCondition.setOnClickListener{
            startActivity(Intent(this, TermsAndConditionsActivity::class.java))
        }

        binding.signupConfirm.setOnClickListener{
            /*Step
        1. Input Data
        2. Validate Data
        3. Create Account - Firebase Auth
        4. Save User Info - Firebase Realtime DataBase
        */
            validateData()
        }
    }


    private fun validateData(){
        //1) Input Data

        name = binding.signupUsername.text.toString().trim()
        email = binding.signupEmail.text.toString().trim()
        password = binding.signupPassword.text.toString().trim()
        phoneNumber = binding.signupPhoneNumber.text.toString().trim()
        country = binding.signupNationality.selectedItem.toString().trim()

        val cPassword = binding.signupPasswordConfirm.text.toString().trim()

        //2) validate Data
        if(name.isEmpty()){
            //if the name is empty
            Toast.makeText(this,"Enter Your name...",Toast.LENGTH_SHORT).show()
        }
        else if(!EMAIL_ADDRESS.matcher(email).matches()){
            //Invalid email patterns
            Toast.makeText(this,"Invalid Email Patterns...",Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            //if the password is empty
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show()
        }
        else if(password.length <= 6){
            //if the password less than 6 character
            Toast.makeText(this,"Please Enter Password more than 6 character",Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            //if the password is empty
            Toast.makeText(this,"Please Fill Your Password",Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
            Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show()
        }
        else if (phoneNumber.isEmpty()){
            Toast.makeText(this,"Please Fill Your Phone Number",Toast.LENGTH_SHORT).show()
        }
        else if(datePick.isEmpty()) {
            Toast.makeText(this, "Please Fill Your Date Of Birth", Toast.LENGTH_SHORT).show()
        }
        else if(!binding.signupRadioButton.isChecked){
            Toast.makeText(this, "Please Agree our Terms and Conditions", Toast.LENGTH_SHORT).show()
        }
        else if(country == "Nationality")
        {
            Toast.makeText(this, "Please Choose Your Nationality", Toast.LENGTH_SHORT).show()
        }
        else if(!mobileValidation(phoneNumber))
            Toast.makeText(this, "Invalid Phone Number Pattern", Toast.LENGTH_SHORT).show()
        else {
            createUserAccount()
        }
    }

    private fun mobileValidation(text: String?):Boolean{
        var p = Pattern.compile("[0-9]{9,11}")
        var m = p.matcher(text)
        return m.matches()
    }

    private fun createUserAccount(){
        //3) Create an Account - FireBase Auth

        //show progress
        progressDialog.setMessage("Creating Account.. Please wait")
        progressDialog.show()

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //account created
                progressDialog.dismiss()
                updateUserInfo()
            }
            .addOnFailureListener { e->
                //account created
                progressDialog.dismiss()
                Toast.makeText(this, "Failed creating account. ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun updateUserInfo(){
        /*4. Save User Info - Firebase Realtime DataBase*/
        progressDialog.setMessage("Saving user info...")

        //timestamp
        val timestamp = System.currentTimeMillis()

        //get current user uid, since user is registered so we can get it now
        val uid = firebaseAuth.uid

        //setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = "" //add empty, will do in profile edit
        hashMap["timestamp"] = timestamp
        hashMap["nationality"] = country
        hashMap["phoneNumber"] = phoneNumber
        hashMap["dateOfBirth"] = datePick.trim()
        hashMap["currentPoints"] = 0
        hashMap["totalPoints"] = 0

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //user info save, open user dash board
                progressDialog.dismiss()
                Toast.makeText(this, "Account created...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java ))
                finish()
            }
            .addOnFailureListener { e->
                //account created
                progressDialog.dismiss()
                Toast.makeText(this, "Failed saving user due to ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}