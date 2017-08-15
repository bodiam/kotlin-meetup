package au.com.kotlin.meetup.crypto

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import javax.annotation.PostConstruct
import javax.persistence.Entity
import javax.persistence.Id


@RestController
@RequestMapping("/crypto")
class CryptoController(val tickerService: TickerService, val cryptoRepository: CryptoRepository) {

    val logger: Logger = LoggerFactory.getLogger(CryptoController::class.java)

    @PostConstruct
    fun setup() {
        cryptoRepository.saveAll(listOf(
                Crypto(id = "1", name = "BTC", tickerName = "BTC-USD"),
                Crypto(id = "2", name = "ETH", tickerName = "ETH-USD")
        ))
    }

    @GetMapping("/ticker")
    fun getTicker(@RequestParam name: String): Ticker {
        logger.info("Ticker name {}", name)
        val info = tickerService.findTickerInfo(name)
        if (info != null) return info else throw IllegalArgumentException("Couldn't find $name")
    }

    @GetMapping
    fun getCrypto() = cryptoRepository.findAll()

    @GetMapping("/{id}")
    fun getCrypto(@PathVariable id: Long) = cryptoRepository.findById(id)
}


@Repository
interface CryptoRepository : JpaRepository<Crypto, Long>

@Service
class TickerService(templateBuilder: RestTemplateBuilder) {

    val restTemplate: RestTemplate = templateBuilder.rootUri("https://api.gdax.com/products").build()

    fun findTickerInfo(symbol: String): Ticker? {
        return restTemplate.getForObject("/$symbol/ticker")
    }
}

data class Ticker(val price: BigDecimal, val bid: BigDecimal, val ask: BigDecimal)

@Entity
data class Crypto(@Id val id: String, val name: String, val tickerName: String)
