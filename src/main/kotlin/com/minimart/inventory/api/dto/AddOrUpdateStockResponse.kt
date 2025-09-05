package com.minimart.inventory.api.dto

import com.minimart.inventory.api.examples.RequestExamples
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(
  description = "Response containing the current stock levels after an add/update operation",
  example = RequestExamples.ADD_OR_UPDATE_STOCK_REQUEST
)
data class AddOrUpdateStockResponse(
    @field:Schema(description = "Unique identifier of the product") val sku: String,
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
