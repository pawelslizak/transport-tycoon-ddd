package org.test.model

import spock.lang.Specification
import spock.lang.Unroll

import static org.test.model.Cargo.*

class JourneySpec extends Specification {

    @Unroll("Initial stock: #initialStock, expectedElapsedHours: #expectedElapsedHours")
    def "should make journey"(){
        given:
        Warehouse factory = new Warehouse("factory", initialStock)
        Warehouse port = new Warehouse("port", [])
        Warehouse a = new Warehouse("a", [])
        Warehouse b = new Warehouse("b", [])

        Dispatcher dispatcher = new Dispatcher(
              new Dispatcher.Route(factory, port, 1, [A].toSet(), new HashSet<Cargo>()),
              new Dispatcher.Route(factory, b, 5, [B].toSet(), new HashSet<Cargo>()),
              new Dispatcher.Route(port, a, 4, [A].toSet(), new HashSet<Cargo>())
        )

        TransportUnit truck1 = new TransportUnit(factory)
        TransportUnit truck2 = new TransportUnit(factory)
        TransportUnit ship = new TransportUnit(port)

        def allTransport = [truck1, truck2, ship]

        int elapsedHours = 0

        def print = {println " ==== Hour: $elapsedHours ====="
        println "${factory.name}: ${factory.stock}"
        println "${port.name}: ${port.stock}"
        println "${a.name}: ${a.stock}"
        println "${b.name}: ${b.stock}"
        println "Truck1 cargo: ${truck1.load}, travels to: ${truck1.travel.destination.name} - ${truck1.travel.arrivalInHours}"
        println "Truck2 cargo: ${truck2.load}, travels to: ${truck2.travel.destination.name} - ${truck2.travel.arrivalInHours}"
        println "Ship cargo: ${ship.load}, travels to: ${ship.travel.destination.name} - ${ship.travel.arrivalInHours}"}

        print()

        when:
        while (a.stock != initialStock.findAll{it == A} || b.stock != initialStock.findAll {it == B}){
            elapsedHours++
            allTransport*.setUpTravel(dispatcher)
            allTransport*.move()
            allTransport*.unload()

            print()

            assert elapsedHours < 100
        }

        then:
        elapsedHours == expectedElapsedHours

        where:
        initialStock | expectedElapsedHours
        [A] | 5
        [A, B] | 5
        [B, B] | 5
        [B, B, B] | 15
        [B, B, B, B] | 15
        [B, B, B, B, B] | 25
        [A, B, B] | 7
        [A, A, B, A, B, B, A, B] | 29

    }

}
