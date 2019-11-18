package org.test.model

class Warehouse(
    val name: String,
    private var stock: List<Cargo>
) {
    fun checkNextLoad():Cargo? = stock.firstOrNull()

    fun take():Cargo {
        if(stock.isEmpty()){
            throw IllegalStateException("Take on empty stock. checkNextLoad should be called to check before")
        }
        val cargo = stock[0]
        stock = stock.takeLast(stock.size - 1)
        return cargo
    }


    fun drop(load: Cargo) {
        this.stock += load
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Warehouse

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}