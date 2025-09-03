package com.minimart.inventory.api.dto

import com.minimart.inventory.api.examples.ResponseExamples
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
  description = "Response for adding or updating product stock inventory per order",
  example = ResponseExamples.RESERVE_STOCK_RESPONSE
)
data class ReserveStockResponse(
  @field:Schema(description = "Unique identifier of the product") val sku: String,
  @field:Schema(
    description = "The quantity of stock requested to add, update, or set.",
    example = "50"
  )
  val requestedQty: Long,
  @field:Schema(description = "The quantity of stock available.", example = "50")
  val available: Long
)
