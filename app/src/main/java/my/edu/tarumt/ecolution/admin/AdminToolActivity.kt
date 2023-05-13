package my.edu.tarumt.ecolution.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import my.edu.tarumt.ecolution.databinding.ActivityAdminToolBinding


class AdminToolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminToolBinding
    val recycleType = arrayOf("Plastic", "Tin", "Paper")


    private var recycleItem = ""
    private var weight = ""
    private var plasticWeightRate = 10.0
    private var tinWeightRate = 15.0
    private var paperWeightRate = 12.0
    private var bonusRate = 1.05
    private var totalPoint = 0.0
    private var userIdScan = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminToolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        //create Alert Dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
            .setMessage("Do not Share Your Company ID & Key To Others")
            .setCancelable(true)
            .setPositiveButton("Okay"){dialog, which ->

            }
        //show alert dialog
        val dialog = builder.create()
        dialog.show()

        val arrayAdapter = ArrayAdapter (this@AdminToolActivity, android.R.layout.simple_spinner_dropdown_item, recycleType)
        binding.recyclerTypeSpinner.adapter = arrayAdapter

        binding.recyclerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.calculateButton.setOnClickListener{
            validateData()
        }

        binding.qrCode.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(true)
            scanner.initiateScan()

        }
        binding.pointIn.setOnClickListener{
            validateData()
        }

        binding.pointIn.setOnClickListener{
            validateUserData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            result?.contents?.let {
                userIdScan = result.contents
                binding.userId.setText(userIdScan)
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            } ?: run {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun validateData(){

        weight = binding.recycleWeight.text.toString().trim()
        recycleItem = binding.recyclerTypeSpinner.selectedItem.toString().trim()

        //2) Validate Data

        if (weight.isEmpty()) {
            Toast.makeText(this, "Weight cannot be empty", Toast.LENGTH_SHORT).show()
        }
        else if(weight.toDouble() <= 0){
            Toast.makeText(this, "Make sure weight is greater than 0", Toast.LENGTH_SHORT).show()
        }
        else {
            calculate()
        }
    }

    private fun calculate(){


        if(recycleItem == "Plastic"){
            totalPoint = weight.toDouble() * plasticWeightRate

        }else if(recycleItem == "Tin"){
            totalPoint = weight.toDouble() * tinWeightRate

        }
        else if(recycleItem == "Paper"){
            totalPoint = weight.toDouble() * paperWeightRate

        }
        //more than 10 kg bonus
        if(weight.toDouble() > 10){
            totalPoint *= bonusRate
            binding.bonus.text = "Bonus 5%"
        }

        else if(weight.toDouble() <= 10){
            binding.bonus.text = "Bonus 0%"
        }
        totalPoint = kotlin.math.ceil(totalPoint)
        binding.showWeightAmount.text = String.format("%s KG", weight)
        binding.showPointAmount.text = "${totalPoint.toInt()} p."


    }

    private fun validateUserData(){

        userIdScan = binding.userId.text.toString().trim()
        //2) Validate Data

        if (userIdScan.isEmpty()) {
            Toast.makeText(this, "Please Enter or Scan User ID", Toast.LENGTH_SHORT).show()
        }
        else if (totalPoint.toInt() == 0) {
            Toast.makeText(this, "Please Calculate Your Point", Toast.LENGTH_SHORT).show()
        }
        else {
            pointIn()
        }
    }

    private fun pointIn(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get user info
                if(snapshot.hasChild(userIdScan)){

                    val point = "${snapshot.child(userIdScan).child("currentPoints").value}"
                    val totalPoints ="${snapshot.child(userIdScan).child("totalPoints").value}"
                    val newCurrentPoint = point.toInt() + totalPoint.toInt()
                    val newTotalPoint = totalPoints.toInt() + totalPoint.toInt()

                    val hashMap = HashMap<String, Any>()
                    hashMap["currentPoints"] = newCurrentPoint
                    hashMap["totalPoints"] = newTotalPoint

                    val dbRef = FirebaseDatabase.getInstance().getReference("Users")
                    dbRef.child(userIdScan)
                        .updateChildren(hashMap)
                    Toast.makeText(this@AdminToolActivity, "Point Added Successfully", Toast.LENGTH_SHORT).show()
                }else {
                    // user ID does not exist
                    Toast.makeText(this@AdminToolActivity, "User ID not found", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}





