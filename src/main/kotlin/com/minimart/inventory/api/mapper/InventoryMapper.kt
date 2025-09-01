package com.minimart.inventory.api.mapper

import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.infra.persistence.StockDocument
import java.time.Instant

object InventoryMapper {
  fun toAddOrUpdateStockResponse(stockDocument: StockDocument) =
    AddOrUpdateStockResponse(
      productId = stockDocument.sku,
      onHand = stockDocument.onHand,
      reserved = stockDocument.reserved,
      available = stockDocument.onHand - stockDocument.reserved,
      lastUpdated = stockDocument.updatedAt ?: Instant.now()
    )
}
