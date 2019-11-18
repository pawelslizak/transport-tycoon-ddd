package org.test.model

class Travel(
    val destination: Warehouse,
    private var arrivalInHours: Int
) {


    fun proceed(){
        arrivalInHours--
        require(arrivalInHours >= 0){
            "Travel to warehouse: ${this.destination.name} exceeded limit"
        }
    }

    fun isAtWarehouse() : Boolean =  arrivalInHours == 0

    companion object Factory {
        fun empty(destination: Warehouse) : Travel = Travel(destination, 0)
    }

}