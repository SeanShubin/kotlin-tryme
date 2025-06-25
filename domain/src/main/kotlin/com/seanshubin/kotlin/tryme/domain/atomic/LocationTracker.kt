package com.seanshubin.kotlin.tryme.domain.atomic

import java.util.concurrent.atomic.AtomicReference

class LocationTracker(initialLocation:Location) {
    private var locationReference:AtomicReference<Location> = AtomicReference(initialLocation)

    fun moveX(deltaX: Int) {
        var result = false
        while(!result){
            val oldLocation = locationReference.get()
            val newLocation = oldLocation.moveX(deltaX)
            result = locationReference.compareAndSet(oldLocation, newLocation)
        }
    }

    fun moveY(deltaY: Int) {
        var result = false
        while(!result) {
            val oldLocation = locationReference.get()
            val newLocation = oldLocation.moveY(deltaY)
            result = locationReference.compareAndSet(oldLocation, newLocation)
        }
    }

    fun getLocation(): Location {
        return locationReference.get()
    }
}