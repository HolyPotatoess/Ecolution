package my.edu.tarumt.ecolution.aboutus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import my.edu.tarumt.ecolution.databinding.FragmentAboutUsBinding


class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAboutUsBinding.inflate(inflater, container, false)

        binding.imageButtonFacebook.setOnClickListener{
            val url = "https://www.facebook.com/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.imageButtonInstagram.setOnClickListener {
            val url = "https://www.instagram.com/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.imageButtonTiktok.setOnClickListener {
            val url = "https://www.tiktok.com/en/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        return binding.root
    }
}