package com.minimart.inventory.infra.persistence

import java.time.Instant
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "stock")
data class StockDocument(
  @Id val id: String? = null,
  @Indexed(unique = true) val sku: String,
  var onHand: Long,
  var reserved: Long,
  @CreatedDate var createdAt: Instant? = null,
  @LastModifiedDate var updatedAt: Instant? = null,
  @Version var version: Long? = null,
)
