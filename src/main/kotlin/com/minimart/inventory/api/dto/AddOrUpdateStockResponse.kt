package com.minimart.inventory.api.dto

import com.minimart.inventory.infra.persistence.StockDocument
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(
  description = "Response containing the current stock levels after an add/update operation",
  example =
    """
        {
            "productId": "PROD-001",
            "onHand": 150,
            "reserved": 25,
            "available": 125,
            "lastUpdated": "2025-01-15T10:30:00Z"
        }
    """
)
data class AddOrUpdateStockResponse(
  @field:Schema(description = "Unique identifier of the product") val productId: String,
  @field:Schema(
    description = "Total physical stock quantity available in inventory",
    example = "150",
    minimum = "0"
  )
  val onHand: Long,
  @field:Schema(
    description =
      "Stock quantity that is reserved for pending orders and cannot be allocated to new orders",
    example = "25",
    minimum = "0"
  )
  val reserved: Long,
  @field:Schema(
    description =
      "Stock quantity that is free and available for allocation to new orders (onHand - reserved)",
    example = "125",
    minimum = "0"
  )
  val available: Long,
  @field:Schema(
    description = "ISO 8601 timestamp when this stock information was last updated",
    example = "2025-01-15T10:30:00Z",
    type = "string",
    format = "date-time"
  )
  val lastUpdated: Instant
)

fun toResponse(doc: StockDocument) =
  AddOrUpdateStockResponse(
    productId = doc.sku,
    onHand = doc.onHand,
    reserved = doc.reserved,
    available = doc.onHand - doc.reserved,
    lastUpdated = doc.updatedAt ?: Instant.now()
  )
