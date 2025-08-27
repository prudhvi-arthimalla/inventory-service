package com.minimart.inventory.infra.persistence

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface StockRepository : ReactiveMongoRepository<StockDocument, String> {}
