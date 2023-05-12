package my.edu.tarumt.ecolution.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarumt.ecolution.R

import my.edu.tarumt.ecolution.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentArticleBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerview

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = ArticleItemAdapter(DataSource().loadArticle(),this.requireContext())
    }

    //read username from database from Edison
    private fun loadUserInfo() {
        //db reference

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                    //get user info from real time database

                    val name = "${snapshot.child("name").value}"
                    binding.menuUsername.text = name

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT).show()
                }

            })

    }
}