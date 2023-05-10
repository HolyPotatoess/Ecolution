package my.edu.tarumt.ecolution.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import my.edu.tarumt.ecolution.R

class UpdateProfileFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var nationalitySpinner: Spinner
    private lateinit var selectedNational : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(firebaseAuth.uid!!).get().addOnSuccessListener {
            val dtUsername = it.child("name").value
            val dtBioInfo = it.child("bioInfo").value
            val dtPhoneNo = it.child("phoneNumber").value
            view?.findViewById<TextInputEditText>(R.id.update_username)?.setText("$dtUsername")
            view?.findViewById<TextInputEditText>(R.id.update_bioInfo)?.setText("$dtBioInfo")
            view?.findViewById<TextInputEditText>(R.id.update_phoneNo)?.setText("$dtPhoneNo")
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDatabaseData(view)

        nationalitySpinner = view.findViewById(R.id.nationality_spinner)
        val national = resources.getStringArray(R.array.nationality_array)
        val arrayAdapterNational = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, national)

        nationalitySpinner.adapter = arrayAdapterNational
        nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedNational = p0?.getItemAtPosition(p2).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun updateDatabaseData(view: View){
        val updateButton = view.findViewById<Button>(R.id.update_info)
        val database = FirebaseDatabase.getInstance().getReference("Users")
        updateButton.setOnClickListener {
            val name = view.findViewById<TextInputEditText>(R.id.update_username).text.toString()
            val bioInfo = view.findViewById<TextInputEditText>(R.id.update_bioInfo).text.toString()
            val phoneNumber =
                view.findViewById<TextInputEditText>(R.id.update_phoneNo).text.toString()
            val selectedRadioId = view.findViewById<RadioButton>(R.id.update_radioGroupGender).isChecked
            val gender = selectedRadioId.toString()

            // Validation when update
            when {
                name == "" -> {
                    Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
                }
                bioInfo == "" -> {
                    Toast.makeText(context, "Please enter your Bio Information", Toast.LENGTH_SHORT).show()
                }
                phoneNumber == "" -> {
                    Toast.makeText(context, "Please enter your Phone Number", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    database.child(firebaseAuth.uid!!).child("name").setValue(name)
                    database.child(firebaseAuth.uid!!).child("bioInfo").setValue(bioInfo)
                    database.child(firebaseAuth.uid!!).child("phoneNumber").setValue(phoneNumber)
                    database.child(firebaseAuth.uid!!).child("gender").setValue(gender)
                    database.child(firebaseAuth.uid!!).child("nationality").setValue(selectedNational)
                    Toast.makeText(context, "Your Information has been updated. Please refresh your page", Toast.LENGTH_LONG).show()
                    parentFragmentManager.beginTransaction().remove(this).commit()
                }
            }
        }
    }
}

