package com.minimart.inventory.api.examples

object RequestExamples {
  const val ADD_OR_UPDATE_STOCK_REQUEST =
    """
        {
            "productId": "SKU-123",
            "onHand": 150,
            "reserved": 25,
            "available": 125,
            "lastUpdated": "2025-01-15T10:30:00Z"
        }
    """
  const val RESERVE_STOCK_REQUEST =
    """
        {
            "productId": "SKU-123",
            "quantity": 3,
            "orderId": "ORD-789"
        }
    """
}
