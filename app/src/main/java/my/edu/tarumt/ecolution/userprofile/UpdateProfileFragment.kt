package my.edu.tarumt.ecolution.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.FragmentUpdateProfileBinding
import my.edu.tarumt.ecolution.databinding.FragmentUserProfileBinding

class UpdateProfileFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var nationalitySpinner: Spinner
    private lateinit var selectedNational: String
    private lateinit var binding: FragmentUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(firebaseAuth.uid!!).get().addOnSuccessListener {
            val dtUsername = it.child("name").value
            val dtBioInfo = it.child("bioInfo").value
            val dtPhoneNo = it.child("phoneNumber").value
            binding.updateUsername.setText("$dtUsername")
            binding.updateBioInfo.setText("$dtBioInfo")
            binding.updatePhoneNo.setText("$dtPhoneNo")
        }

        updateDatabaseData(binding)

        nationalitySpinner = binding.nationalitySpinner
        val national = resources.getStringArray(R.array.nationality_array)
        val arrayAdapterNational =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, national)

        nationalitySpinner.adapter = arrayAdapterNational
        nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedNational = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateDatabaseData(binding: FragmentUpdateProfileBinding) {
        val updateButton = binding.updateInfo
        val database = FirebaseDatabase.getInstance().getReference("Users")
        updateButton.setOnClickListener {
            val name = binding.updateUsername.text.toString()
            val bioInfo = binding.updateBioInfo.text.toString()
            val phoneNumber = binding.updatePhoneNo.text.toString()
            val selectedGenderId = binding.updateRadioGroupGender.checkedRadioButtonId
            val gender = binding.root.findViewById<RadioButton>(selectedGenderId).text

            // Validation when update
            when {
                name == "" -> {
                    Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
                }
                bioInfo == "" -> {
                    Toast.makeText(context, "Please enter your Bio Information", Toast.LENGTH_SHORT)
                        .show()
                }
                phoneNumber == "" -> {
                    Toast.makeText(context, "Please enter your Phone Number", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    database.child(firebaseAuth.uid!!).child("name").setValue(name)
                    database.child(firebaseAuth.uid!!).child("bioInfo").setValue(bioInfo)
                    database.child(firebaseAuth.uid!!).child("phoneNumber").setValue(phoneNumber)
                    database.child(firebaseAuth.uid!!).child("gender").setValue(gender)
                    database.child(firebaseAuth.uid!!).child("nationality")
                        .setValue(selectedNational)
                    Toast.makeText(
                        context,
                        "Your Information has been updated. Please refresh your page",
                        Toast.LENGTH_LONG
                    ).show()
                    parentFragmentManager.beginTransaction().remove(this).commit()
                }
            }
        }
    }
}



