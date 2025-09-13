package com.minimart.inventory.domain.service

import com.github.avrokotlin.avro4k.Avro
import com.minimart.inventory.domain.events.ProductCreated
import com.minimart.inventory.domain.messaging.EventDeduper
import kotlinx.serialization.serializer
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ProductRegisteredListener(
    private val inventoryService: InventoryService,
    private val eventDeduper: EventDeduper
) {
    @KafkaListener(
        topics = ["\${kafka.topics.productRegistered}"],
        containerFactory = "kafkaListenerContainerFactory")
    fun onProductRegistered(value: GenericRecord) {
        val evt = Avro.default.fromRecord(serializer<ProductCreated>(), value)

        if (eventDeduper.alreadyProcessed(evt.eventId)) {
            log.info("duplicate eventId={}, skipping", evt.eventId)
            return
        }

        inventoryService
            .seedIfAbsent(evt.skuCode, 0)
            .doOnError { log.error("Seed failed for ${evt.skuCode}", it) }
            .subscribe()

        eventDeduper.markProcessed(evt.eventId)
        log.info("seeded stock=0 for sku={} from ProductRegisteredV1", evt.skuCode)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ProductRegisteredListener::class.java)
    }
}
