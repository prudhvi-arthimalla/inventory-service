package com.minimart.inventory.api.examples

object ResponseExamples {
  const val ADD_OR_UPDATE_STOCK_RESPONSE =
    """
        {
            "productId": "SKU-123",
            "onHand": 150,
            "reserved": 25,
            "available": 125,
            "lastUpdated": "2025-01-15T10:30:00Z"
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
