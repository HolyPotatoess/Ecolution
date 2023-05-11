package my.edu.tarumt.ecolution.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.FragmentRewardBinding
import my.edu.tarumt.ecolution.ranking.UserData

class RewardFragment : Fragment() {

    private lateinit var binding: FragmentRewardBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRewardBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize data.
        val myDataset = RewardDataSource().loadRewards()
        val recyclerView = binding.rewardRecycleView

        //load user current point and pass to DetailVoucher
        loadUserPoint()
        val point = binding.point2.text


        recyclerView.adapter = RewardItemAdapter(requireContext(), myDataset)

        //add a divider between each of the item in recycle view
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
        return binding.root
    }


    private fun loadUserPoint(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info [point]
                    binding.point2.text = "${snapshot.child("currentPoints").value}"
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}