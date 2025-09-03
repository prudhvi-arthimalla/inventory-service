package com.minimart.inventory.api.examples

object ResponseExamples {
  const val ADD_OR_UPDATE_STOCK_RESPONSE =
    """
        {
            "productId": "SKU-123",
            "quantity": 50,
            "mode": "UPSERT"
        }
    """
  const val RESERVE_STOCK_RESPONSE =
    """
        {
          "productId": "SKU-123",
          "reservedQty": 3,
          "available": 97
        }
    """
}
