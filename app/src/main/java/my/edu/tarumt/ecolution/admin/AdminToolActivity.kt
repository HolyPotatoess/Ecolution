package my.edu.tarumt.ecolution.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.zxing.integration.android.IntentIntegrator
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

        binding.qrCode.setOnClickListener{
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(true)
            scanner.initiateScan()

        }

    }

    fun OnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
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
        } else {
            calculate()
        }
    }
    private fun calculate(){
        var totalPoint = 0.0

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
        binding.showPointAmount.text = "$totalPoint .p"


    }



}


