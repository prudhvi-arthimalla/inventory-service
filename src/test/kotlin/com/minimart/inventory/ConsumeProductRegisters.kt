package com.minimart.inventory

import com.github.avrokotlin.avro4k.Avro
import com.minimart.inventory.KafkaTestcontainersConfig.Companion.redpanda
import com.minimart.inventory.domain.events.ProductCreated
import com.minimart.inventory.persistence.StockRepository
import java.util.Properties
import java.util.UUID
import kotlin.jvm.java
import kotlin.test.Test
import kotlinx.serialization.serializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [InventoryServiceApplication::class])
private class ConsumeProductRegisters(
    @param:Autowired private val stockRepository: StockRepository
) {
    @Test
    fun `consumes ProductRegisteredV1 and creates stock zero`() {
        val sku = "SKU-${System.currentTimeMillis()}"

        val evt =
            ProductCreated(
                skuCode = sku,
                occurredAtEpochMs = System.currentTimeMillis(),
                eventId = UUID.randomUUID().toString())
        val gr: GenericRecord = Avro.default.toRecord(serializer<ProductCreated>(), evt)

        // produce to the same topic the service listens to
        val props =
            Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, redpanda.bootstrapServers)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
                put(
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    io.confluent.kafka.serializers.KafkaAvroSerializer::class.java.name)
                put("schema.registry.url", redpanda.schemaRegistryAddress)
            }
        KafkaProducer<String, Any>(props).use { p ->
            p.send(ProducerRecord("catalog.product.registered.v1", sku, gr)).get()
        }

        // simple await until the consumer processes it
        awaitBusy {
            val stock = stockRepository.findBySku(sku).block()
            stock?.onHand?.toInt() == 0
        }
    }

    // tiny await helper
    private fun awaitBusy(timeoutMs: Long = 5000, check: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (check()) return
            Thread.sleep(100)
        }
        error("condition not met in ${timeoutMs}ms")
    }
}
