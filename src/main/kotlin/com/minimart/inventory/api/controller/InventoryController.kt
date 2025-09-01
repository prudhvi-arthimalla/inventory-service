package com.minimart.inventory.api.controller

import com.minimart.inventory.api.dto.AddOrUpdateStockRequest
import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.api.examples.RequestExamples
import com.minimart.inventory.domain.service.InventoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/stock")
class InventoryController(val inventoryService: InventoryService) {

  @PostMapping(
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  @ResponseStatus(HttpStatus.OK)
  @Operation(
    summary = "Add or update stock for a given SKU",
    description =
      "Add or update stock either absolutely (UPSERT) or relatively (INCREMENT)" +
        "Return 200 with Response body",
    responses =
      [
        ApiResponse(
          responseCode = "200",
          description = "Successfully add or update the stock",
          content = [Content(schema = Schema(implementation = AddOrUpdateStockResponse::class))]
        ),
        ApiResponse(
          responseCode = "410",
          description = "Quantity can not be below reserved",
          content = [Content(schema = Schema(implementation = Error::class))]
        ),
        ApiResponse(
          responseCode = "400",
          description = "Invalid request",
          content = [Content(schema = Schema(implementation = Error::class))]
        )
      ]
  )
  fun addOrUpdateStock(
    @RequestBody(
      description = "Payload to add or update stock",
      required = true,
      content = [Content(schema = Schema(implementation = AddOrUpdateStockRequest::class))]
    )
    @org.springframework.web.bind.annotation.RequestBody
    @Valid
    addOrUpdateStockRequest: AddOrUpdateStockRequest
  ): Mono<AddOrUpdateStockResponse> {
    return inventoryService.addOrUpdateStock(addOrUpdateStockRequest)
  }
}
