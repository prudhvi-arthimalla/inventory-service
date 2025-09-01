package com.minimart.inventory.domain.service

import com.minimart.inventory.api.dto.AddOrUpdateStockRequest
import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.api.mapper.InventoryMapper
import com.minimart.inventory.infra.persistence.StockRepository
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

class InventoryService(val stockRepository: StockRepository) {
  fun addOrUpdateStock(
    addOrUpdateStockRequest: AddOrUpdateStockRequest
  ): Mono<AddOrUpdateStockResponse> {
    val skuCode = addOrUpdateStockRequest.skuCode
    return stockRepository
      .findBySkuCode(skuCode)
      .switchIfEmpty(
        Mono.error {
          ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Product does not exist with provided productId"
          )
        }
      )
      .flatMap { productDocument ->
        productDocument.onHand = addOrUpdateStockRequest.quantity
        stockRepository.save(productDocument).map(InventoryMapper::toAddOrUpdateStockResponse)
      }
  }
}
