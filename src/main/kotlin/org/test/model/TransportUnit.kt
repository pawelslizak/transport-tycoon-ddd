package org.test.model

class TransportUnit(originWarehouse: Warehouse) {

    private var load: Cargo? = null
    private var travel: Travel
    private val originWarehouse: Warehouse

    init {
        this.travel = Travel.empty(originWarehouse)
        this.originWarehouse = originWarehouse
    }

    fun setUpTravel(dispatcher: Dispatcher){
            if(!travel.isAtWarehouse()){
                return
            }

            val currentWarehouse = travel.destination
            require(load == null){"When on move in warehouse ${currentWarehouse.name} load $load should be empty. " +
                    "Originating from: ${originWarehouse.name} "}

            val nextCargo = currentWarehouse.checkNextLoad()
            if(nextCargo == null){
                if(currentWarehouse != originWarehouse){
                    this.travel = dispatcher.navigateWithWarehouse(currentWarehouse, originWarehouse)
                }
                return
            }

            val nextTravel = dispatcher.navigateWithCargo(nextCargo, currentWarehouse)

            if(nextTravel != null &&
                ( currentWarehouse == originWarehouse || nextTravel.destination == originWarehouse)){
                travel = nextTravel
                load = currentWarehouse.take()
                return
            }

            if(currentWarehouse != originWarehouse){
                travel = dispatcher.navigateWithWarehouse(currentWarehouse, originWarehouse)
            }
    }

    fun move() {
        if (load != null || (
                    !travel.isAtWarehouse() && travel.destination == originWarehouse
                    )
        ) {
            travel.proceed()
        }

    }


    fun unload(){
        if(!travel.isAtWarehouse()){
            return
        }
        this.load?.let {
            val warehouse = travel.destination
            warehouse.drop(it)
            this.load = null
        }
    }



}