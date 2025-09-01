package com.minimart.inventory.domain.service

import com.minimart.inventory.api.dto.AddOrUpdateStockRequest
import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.api.dto.StockRequestMode
import com.minimart.inventory.api.mapper.InventoryMapper
import com.minimart.inventory.infra.persistence.StockDocument
import com.minimart.inventory.infra.persistence.StockRepository
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

class InventoryService(val stockRepository: StockRepository) {
  /**
   * Adds or updates stock for a given SKU with validation to ensure stock levels remain consistent.
   * Supports both absolute setting (UPSERT) and relative adjustment (INCREMENT) operations.
   *
   * @param addOrUpdateStockRequest The request containing SKU, quantity, and operation mode
   * @return Mono<AddOrUpdateStockResponse> Response containing the updated stock information
   */
  fun addOrUpdateStock(
    addOrUpdateStockRequest: AddOrUpdateStockRequest
  ): Mono<AddOrUpdateStockResponse> {
    val skuCode = addOrUpdateStockRequest.skuCode
    // Find existing stock document by SKU, or create default if not found
    return stockRepository
      .findBySku(skuCode)
      .defaultIfEmpty(StockDocument(sku = skuCode, onHand = 0, reserved = 0))
      .flatMap { document ->
        val quantity = addOrUpdateStockRequest.quantity
        when (addOrUpdateStockRequest.mode) {
          StockRequestMode.UPSERT -> {
            // Validate that we can't set onHand below currently reserved stock
            // to avoid over selling and non-negative reserves
            if (quantity < document.reserved) {
              return@flatMap Mono.error(
                ResponseStatusException(
                  HttpStatus.CONFLICT,
                  "Cannot set onHand $quantity below ${document.reserved}"
                )
              )
            }
            document.onHand = quantity
            stockRepository.save(document)
          }
          StockRequestMode.INCREMENT -> {
            // Calculate new stock value after increment
            val newOnHand = document.onHand + quantity
            // Validate that we can't set onHand below currently reserved stock
            // to avoid over selling and non-negative reserves
            if (newOnHand < document.reserved) {
              return@flatMap Mono.error(
                ResponseStatusException(
                  HttpStatus.CONFLICT,
                  "Increment would set onHand $quantity below ${document.reserved}"
                )
              )
            }
            document.onHand = newOnHand
            stockRepository.save(document)
          }
        }
      }
      // Retry mechanism for handling concurrent updates
      // If two requests try to update the same stock simultaneously,
      // OptimisticLockingFailureException will be thrown and we retry up to 3 times
      .retryWhen(
        reactor.util.retry.Retry.max(3).filter {
          it is org.springframework.dao.OptimisticLockingFailureException
        }
      )
      .map(InventoryMapper::toAddOrUpdateStockResponse)
  }
}
