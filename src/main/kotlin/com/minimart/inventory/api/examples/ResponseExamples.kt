package com.minimart.inventory.api.examples

object ResponseExamples {
  const val ADD_OR_UPDATE_STOCK_RESPONSE =
    """
        {
            "productId": "PROD-001",
            "quantity": 50,
            "mode": "UPSERT"
        }
    """
}
