package my.edu.tarumt.ecolution.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import my.edu.tarumt.ecolution.ranking.UserData
import my.edu.tarumt.ecolution.R

class RankingAdapter(private val userDataList : ArrayList<UserData>) : RecyclerView.Adapter<RankingAdapter.MyViewHolder>() {

    private lateinit var firebaseAuth : FirebaseAuth
    private var i = userDataList.size - 1
    private var rankCount = 1

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.leaderboard_name)
        val totalPoints : TextView = itemView.findViewById(R.id.pointsView)
        val rankView : TextView = itemView.findViewById(R.id.ranking)
        val medalView : ImageView = itemView.findViewById(R.id.leaderboard_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_leaderboard_list_item, parent, false)
        firebaseAuth = FirebaseAuth.getInstance()
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val database = FirebaseDatabase.getInstance().getReference("Users")
        val currentItem = userDataList
        if (i < userDataList.size) {
            val currentUid = currentItem[i].uid
            when (rankCount) {
                1 -> {
                    holder.name.text = currentItem[i].name
                    holder.totalPoints.text = currentItem[i].totalPoints.toString() + " pts"
                    holder.medalView.setImageResource(R.drawable.gold_medal)
                    database.child(currentUid.toString()).child("Rank").setValue(1)
                    i--
                    rankCount++
                }
                2 -> {
                    holder.name.text = currentItem[i].name
                    holder.totalPoints.text = currentItem[i].totalPoints.toString() + " pts"
                    holder.medalView.setImageResource(R.drawable.silver_medal)
                    database.child(currentUid.toString()).child("Rank").setValue(2)
                    i--
                    rankCount++
                }
                3 -> {
                    holder.name.text = currentItem[i].name
                    holder.totalPoints.text = currentItem[i].totalPoints.toString() + " pts"
                    holder.medalView.setImageResource(R.drawable.bronze_medal)
                    database.child(currentUid.toString()).child("Rank").setValue(3)
                    i--
                    rankCount++
                }
                else -> {
                    holder.name.text = currentItem[i].name
                    holder.totalPoints.text = currentItem[i].totalPoints.toString() + " pts"
                    holder.medalView.visibility = View.GONE
                    holder.rankView.visibility = View.VISIBLE
                    holder.rankView.text = "#$rankCount"
                    database.child(currentUid.toString()).child("Rank").setValue(rankCount)
                    i--
                    rankCount++
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }
}