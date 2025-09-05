package com.minimart.inventory.api.examples

object RequestExamples {
  const val ADD_OR_UPDATE_STOCK_REQUEST =
    """
        {
            "productId": "SKU-123",
            "quantity": 50,
            "mode": "UPSERT"
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
