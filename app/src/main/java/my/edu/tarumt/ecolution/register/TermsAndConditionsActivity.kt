package my.edu.tarumt.ecolution.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.edu.tarumt.ecolution.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hide action bar
        supportActionBar?.hide()

        //back button
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }
}