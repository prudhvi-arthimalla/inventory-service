package com.minimart.inventory.api.examples

object RequestExamples {
  const val ADD_OR_UPDATE_STOCK_REQUEST =
    """
        {
            "productId": "PROD-001",
            "onHand": 150,
            "reserved": 25,
            "available": 125,
            "lastUpdated": "2025-01-15T10:30:00Z"
        }
    """
}
