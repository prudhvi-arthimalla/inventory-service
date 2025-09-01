package com.minimart.inventory.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Schema(
  description = "Request for adding or updating product stock inventory per order",
  example =
    """
        {
            "productId": "SKU-123",
            "quantity": 3,
            "orderId": "ORD-789"
        }
    """
)
data class ReserveStockRequest(
  @field:NotBlank(message = "Product Id cannot be blank")
  @field:Schema(description = "Unique identifier of the product")
  val skuCode: String,
  @field:Min(0, message = "Quantity cannot be negative")
  @field:Max(1000, message = "Quantity cannot exceed 1,000")
  @field:Schema(
    description = "The quantity of stock to add, update, or set.",
    example = "50",
    minimum = "0",
    maximum = "1000000"
  )
  val quantity: Long,
  @field:NotBlank(message = "Order Id cannot be blank")
  @field:Schema(description = "Unique identifier of the order")
  val orderId: String
)
