package com.minimart.inventory.persistence

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface StockRepository : ReactiveMongoRepository<StockDocument, String> {
    fun findBySku(sku: String): Mono<StockDocument>
}
