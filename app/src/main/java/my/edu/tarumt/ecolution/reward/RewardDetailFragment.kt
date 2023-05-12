package my.edu.tarumt.ecolution.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import my.edu.tarumt.ecolution.R
import my.edu.tarumt.ecolution.databinding.FragmentRewardDetailBinding

class RewardDetailFragment : Fragment() {

    private lateinit var  binding: FragmentRewardDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRewardDetailBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        //Get intent from rewardItemAdapter.kt
        val rowIndex: Int = arguments?.getInt("position") ?: 0

        loadRewardDatabase(rowIndex, binding)

        binding.rewardBackButton.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    private fun loadRewardDatabase(rowIndex: Int, binding: FragmentRewardDetailBinding) {
        val rewardRow = when (rowIndex) {
            0 -> "reward1"
            1 -> "reward2"
            2 -> "reward3"
            3 -> "reward4"
            else -> "reward5"
        }

        val ref = FirebaseDatabase.getInstance().getReference("Reward")
        ref.child(rewardRow)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                    val description = "${snapshot.child("description").value}"
                    val startDate = "${snapshot.child("startDate").value}"
                    val endDate = "${snapshot.child("endDate").value}"
                    val point = "${snapshot.child("point").value}"
                    val stockLeft = "${snapshot.child("stockLeft").value}"

                    binding.rewardName.text = name
                    binding.rewardDescriptionValue.text = description
                    binding.rewardDurationValue.text =
                        getString(R.string.valid_duration_value, startDate, endDate)
                    binding.rewardPointsRequiredValue.text =
                        getString(R.string.points_required_value, point)

                    binding.tcValue1.text = getString(R.string.t_c_value1, startDate, endDate)
                    binding.tcValue2.text = getString(R.string.t_c_value2, endDate)

                    loadUserPoint(binding, stockLeft.toInt(), point.toInt(), rowIndex)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    /**
     * Load user current point from Firebase @param[userPoint]
     **/
    private fun loadUserPoint(
        binding: FragmentRewardDetailBinding,
        RewardLeft: Int,
        RewardPoint: Int,
        rowIndex: Int
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userPoint = "${snapshot.child("currentPoints").value}"
                    checkAvailable(RewardLeft, binding, RewardPoint, userPoint.toInt(), rowIndex)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    /**
     * Check whether Reward is still available based on @param[RewardLeft]
     **/
    private fun checkAvailable(
        RewardLeft: Int,
        binding: FragmentRewardDetailBinding,
        RewardPoint: Int,
        currentPoint: Int,
        rowIndex: Int
    ) {
        if (RewardLeft < 1) {
            binding.claimRewardButton.isEnabled = false
            binding.claimRewardButton.text = "Reward fully redeemed"
        }

        binding.claimRewardButton.setOnClickListener {
            if (currentPoint < RewardPoint) {
                val difference: Int = RewardPoint - currentPoint
                val toast = Toast.makeText(
                    context,
                    "Insufficient Point. You need $difference pts more.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                Toast.makeText(
                    context,
                    "You have claimed this Reward successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                //make changes on user's point in firebase
                updateUserDatabase(RewardPoint)
                //make changes in Reward left firebase
                updateRewardDatabase(rowIndex)
            }
        }
    }


    /**
     * Update user current point in Firebase @param [userPoint]
     **/
    private fun updateUserDatabase(voucherPoint: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info

                    val point = "${snapshot.child("currentPoints").value}"
                    val newCurrentPoint = point.toInt() - voucherPoint

                    val hashMap = HashMap<String, Any>()
                    hashMap["currentPoints"] = newCurrentPoint

                    val dbRef = FirebaseDatabase.getInstance().getReference("Users")
                    dbRef.child(firebaseAuth.uid!!)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    /**
     * Update quantity of reward available in Firebase @param[rewardLeft]
     **/
    private fun updateRewardDatabase(rowIndex: Int) {
        val rewardRow = when (rowIndex) {
            0 -> "reward1"
            1 -> "reward2"
            2 -> "reward3"
            3 -> "reward4"
            else -> "reward5"
        }

        val ref = FirebaseDatabase.getInstance().getReference("Reward")
        ref.child(rewardRow)//
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //get user info

                    val stockLeft = "${snapshot.child("stockLeft").value}"
                    val newCurrentPoint = stockLeft.toInt() - 1

                    val hashMap = HashMap<String, Any>()
                    hashMap["stockLeft"] = newCurrentPoint

                    val dbRef = FirebaseDatabase.getInstance().getReference("Reward")
                    dbRef.child(rewardRow)//ref.child(firebaseAuth.uid!!)
                        .updateChildren(hashMap)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}