package my.edu.tarumt.ecolution.ranking

data class UserData(var name : String ?= null, var totalPoints : Int ?= null, var uid : String ?= null) : Comparable<UserData>{
    override fun compareTo(other: UserData)
            = compareValuesBy(this, other, {it.totalPoints})
}
