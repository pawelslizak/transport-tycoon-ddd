package org.test.model

import java.lang.IllegalStateException

class Dispatcher(vararg routes: Route) {
    private val routes: Map<Pair<Warehouse, Warehouse>, Int>
    private val destinations: Map<Pair<Cargo, Warehouse>, Warehouse>

    init {
        val routesMap = mutableMapOf<Pair<Warehouse, Warehouse>, Int>()
        val destinationsMap = mutableMapOf<Pair<Cargo, Warehouse>, Warehouse>()
        routes.forEach { route ->
            routesMap[route.from to route.to]=route.length
            routesMap[route.to to route.from]=route.length

            route.forwardTransport.forEach{cargo ->
                destinationsMap[cargo to route.from] = route.to
            }
            route.backwardTransport.forEach{cargo ->
                destinationsMap[cargo to route.to] = route.from
            }
        }
        this.routes = routesMap
        this.destinations = destinationsMap
    }

    fun navigateWithCargo(cargo:Cargo, from: Warehouse): Travel? {
        val searchedDirection = cargo to from
        val to = destinations[searchedDirection]?:return null
        return navigateWithWarehouse(from, to)
    }

    fun navigateWithWarehouse(from:Warehouse, to: Warehouse): Travel {
        val route = from to to
        val arrivalInHours = routes[route]?:throw IllegalStateException("Route for ${from.name} to ${to.name} not found")
        return Travel(to, arrivalInHours)
    }

    data class Route(val from: Warehouse, val to: Warehouse, val length: Int, val forwardTransport: Set<Cargo>,
                     val backwardTransport: Set<Cargo>){
        init {
            require(from != to){"Route to self is forbidden: ${from.name}, ${to.name}"}
            require(length > 0){"Length should be greater than zero: $length"}
            require(forwardTransport.intersect(backwardTransport).isEmpty()){"Cargo should be move in one way $forwardTransport, $backwardTransport"}
        }
    }

}