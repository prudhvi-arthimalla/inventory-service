package com.minimart.inventory.api.controller

import com.minimart.inventory.api.dto.AddOrUpdateStockRequest
import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.domain.service.InventoryService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/stock")
class InventoryController(val inventoryService: InventoryService) {

  fun addOrUpdateStock(
    addOrUpdateStockRequest: AddOrUpdateStockRequest
  ): Mono<AddOrUpdateStockResponse> {
    return inventoryService.addOrUpdateStock(addOrUpdateStockRequest)
  }
}
