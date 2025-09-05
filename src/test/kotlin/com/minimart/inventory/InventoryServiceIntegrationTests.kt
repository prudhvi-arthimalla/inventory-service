package com.minimart.inventory

import com.minimart.inventory.api.dto.AddOrUpdateStockRequest
import com.minimart.inventory.api.dto.AddOrUpdateStockResponse
import com.minimart.inventory.api.dto.StockRequestMode
import com.minimart.inventory.persistence.StockDocument
import com.minimart.inventory.persistence.StockRepository
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ContextConfiguration(
    classes = [InventoryServiceApplication::class, TestcontainersConfiguration::class]
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class InventoryServiceIntegrationTests {
  @LocalServerPort private var port: Int = 0

  @Autowired lateinit var webTestClient: WebTestClient

  @Autowired lateinit var stockRepository: StockRepository // your reactive Mongo repository

  val uri = "http://localhost:$port/api/v1/stock"

  @BeforeEach
  fun setUp() {
    stockRepository.deleteAll().block()
  }

  @Test
  fun `addOrUpdateStock updates stock and returns 200 with response`() {
    // given
    val request =
        AddOrUpdateStockRequest(skuCode = "sku-123", quantity = 10, mode = StockRequestMode.UPSERT)

    // when & then
    webTestClient
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(AddOrUpdateStockResponse::class.java)
        .consumeWith { result ->
          val response = result.responseBody!!
          assertEquals(request.skuCode, response.sku)
          assertEquals(request.quantity, response.onHand)
        }

    // verify database
    StepVerifier.create(stockRepository.findBySku(request.skuCode)).assertNext { document ->
      assertEquals(document.sku, request.skuCode)
      assertEquals(document.onHand, request.quantity)
    }
  }

  @Test
  fun `addOrUpdateStock with INCREMENT mode adds to existing stock and returns 200 with response`() {
    // given
    val document = StockDocument(sku = "sku-123", onHand = 10, reserved = 2)
    stockRepository.save(document).block()

    val request =
        AddOrUpdateStockRequest(
            skuCode = "sku-123",
            quantity = 8,
            mode = StockRequestMode.INCREMENT,
        )

    // when & then
    webTestClient
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(AddOrUpdateStockResponse::class.java)
        .consumeWith { result ->
          val response = result.responseBody!!
          assertEquals(request.skuCode, response.sku)
          assertEquals(18, response.onHand)
        }
  }

  @Test
  fun `addOrUpdateStock returns 409 when quantity is less than reserved`() {
    val document = StockDocument(sku = "sku-123", onHand = 10, reserved = 5)
    stockRepository.save(document).block()

    val request =
        AddOrUpdateStockRequest(skuCode = "sku-123", quantity = 3, mode = StockRequestMode.UPSERT)

    // when & then
    webTestClient
        .post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT)
  }
}
