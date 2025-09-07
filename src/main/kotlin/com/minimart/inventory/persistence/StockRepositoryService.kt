package com.minimart.inventory.persistence

import org.springframework.data.mongodb.MongoExpression
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

data class StockItem(val sku: String, val requestedQty: Long, val reserved: Boolean)

@Repository
class StockRepositoryService(private val mongoTemplate: ReactiveMongoTemplate) {
    fun reserveStock(sku: String, quantity: Long) {
        val expression =
            MongoExpression.create(
                "\$expr: {\$gte: [{\$subtract: ['\$onHand', '\$reserved']}, $quantity]}")
        val criteria =
            Criteria().andOperator(Criteria.where("sku").`is`(sku), Criteria.expr(expression))
        val query = Query.query(criteria)
    }
}
