package my.edu.tarumt.ecolution.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import my.edu.tarumt.ecolution.R

class RankingFragment : Fragment() {

    private lateinit var database : DatabaseReference
    private lateinit var userDataRecyclerView: RecyclerView
    private lateinit var userDataArrayList: ArrayList<UserData>
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataRecyclerView = view.findViewById(R.id.leaderboardList)
        userDataRecyclerView.layoutManager = LinearLayoutManager(activity)
        userDataRecyclerView.setHasFixedSize(true)

        userDataArrayList = arrayListOf<UserData>()
        getUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    private fun getUserData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.orderByChild("totalPoints").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userDataSnapshot in snapshot.children){
                        val userData = userDataSnapshot.getValue(UserData::class.java)
                        userDataArrayList.add(userData!!)
                    }
                    userDataRecyclerView.adapter = RankingAdapter(userDataArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}
