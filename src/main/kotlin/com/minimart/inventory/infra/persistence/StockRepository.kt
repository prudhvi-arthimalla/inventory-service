package com.minimart.inventory.infra.persistence

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface StockRepository : ReactiveMongoRepository<StockDocument, String> {
    fun findBySkuCode(skuCode: String): Mono<StockDocument>
}
