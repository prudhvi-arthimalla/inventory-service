package com.minimart.inventory.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Schema(
  description = "Request for adding or updating product stock inventory",
  example =
    """
        {
            "productId": "PROD-001",
            "quantity": 50,
            "mode": "UPSERT"
        }
    """
)
data class AddOrUpdateStockRequest(
  @field:NotBlank(message = "Product Id cannot be blank")
  @field:Schema(description = "Unique identifier of the product")
  val skuCode: String,
  @field:Min(0, message = "Quantity cannot be negative")
  @field:Max(1_000_000, message = "Quantity cannot exceed 1,000,000")
  @field:Schema(
    description = "The quantity of stock to add, update, or set.",
    example = "50",
    minimum = "0",
    maximum = "1000000"
  )
  val quantity: Long,
  @field:Valid
  @field:Schema(
    description = "The operation mode for how the quantity should be applied to existing stock",
    example = "UPSERT",
    required = false,
    defaultValue = "UPSERT"
  )
  val mode: StockRequestMode = StockRequestMode.UPSERT
)

enum class StockRequestMode {
  UPSERT,
  INCREMENT
}
