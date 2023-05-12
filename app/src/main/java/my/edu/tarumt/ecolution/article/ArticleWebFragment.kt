package my.edu.tarumt.ecolution.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.FragmentArticleWebBinding

class ArticleWebFragment : Fragment() {
    private lateinit var binding: FragmentArticleWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleWebBinding.inflate(inflater, container, false)

        val rowIndex: Int = arguments?.getInt("position") ?: 0

        loadUrlArray(rowIndex, binding)

        binding.articleBackbtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
        return binding.root
    }

    private fun loadUrlArray(rowIndex: Int, binding: FragmentArticleWebBinding){
        val webView: WebView = binding.webviewArticle
        val website = resources.getStringArray(R.array.website).toList()
        val url = website[rowIndex]
        webView.loadUrl(url)
    }
}