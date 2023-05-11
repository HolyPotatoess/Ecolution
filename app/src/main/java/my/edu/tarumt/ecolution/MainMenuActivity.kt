package my.edu.tarumt.ecolution

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.edu.tarumt.ecolution.databinding.ActivityMainMenuBinding
import my.edu.tarumt.ecolution.login.LoginActivity
import my.edu.tarumt.ecolution.login.SplashActivity

class MainMenuActivity : AppCompatActivity() {
    /* mainmenu fragment change to wrap content and below got space for navBar
    below got navigation bar with 5 button control fragments (do at activity_main_menu.xml)
    TO: ABOUT US, PROFILE & RANKING
    Main menu for article fragment*/
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        var navController= findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf
                (
                R.id.nav_article,
                R.id.nav_aboutUs,
                R.id.nav_reward,
                R.id.nav_ranking,
                R.id.nav_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}