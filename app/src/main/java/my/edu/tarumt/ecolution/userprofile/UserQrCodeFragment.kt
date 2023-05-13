package my.edu.tarumt.ecolution.userprofile

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.FragmentUserQrCodeBinding

class UserQrCodeFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentUserQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserQrCodeBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        getDatabaseData(binding)
        generateQr(binding)
        return binding.root
    }
    private fun getDatabaseData(binding: FragmentUserQrCodeBinding){

        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(firebaseAuth.uid!!).get().addOnSuccessListener {
            val userId = it.child("uid").value
            binding.userIdValue.text = userId.toString()
        }

    }

    private fun generateQr(binding: FragmentUserQrCodeBinding){
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(firebaseAuth.uid!!).get().addOnSuccessListener {
            val userId = it.child("uid").value as String
            try {
                binding.imageViewQr.setImageBitmap(BarcodeEncoder().encodeBitmap(userId, BarcodeFormat.QR_CODE, 200, 200))
            }catch (_: Exception) {

            }
        }
    }
}