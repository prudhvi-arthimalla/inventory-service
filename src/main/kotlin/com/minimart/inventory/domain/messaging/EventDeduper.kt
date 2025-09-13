package com.minimart.inventory.domain.messaging


import org.springframework.context.annotation.Configuration
import java.util.concurrent.ConcurrentHashMap


interface EventDeduper {
    fun alreadyProcessed(eventId: String): Boolean

    fun markProcessed(eventId: String)
}

@Configuration
class InMemoryEventDeduper : EventDeduper {
    private val processed = ConcurrentHashMap.newKeySet<String>()

    override fun alreadyProcessed(eventId: String): Boolean {
        return !processed.add(eventId)
    }

    override fun markProcessed(eventId: String) {
        /* already added in set */
    }
}
