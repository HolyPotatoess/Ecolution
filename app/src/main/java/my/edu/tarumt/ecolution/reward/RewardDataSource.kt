package my.edu.tarumt.ecolution.reward

import my.edu.tarumt.ecolution.R

class RewardDataSource {
    fun loadRewards(): List<Reward>{
        return listOf<Reward>(
            Reward(R.string.reward_spotify, R.drawable.reward_spotify),
            Reward(R.string.reward_starbucks, R.drawable.reward_starbucks),
            Reward(R.string.reward_subway, R.drawable.reward_subway),
            Reward(R.string.reward_grab, R.drawable.reward_grab),
            Reward(R.string.reward_milo, R.drawable.reward_milo),
        )
    }
}