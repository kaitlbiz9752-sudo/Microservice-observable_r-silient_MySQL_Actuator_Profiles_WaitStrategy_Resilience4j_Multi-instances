package ma.abla.book_service.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PricingClient {

    private final RestTemplate restTemplate;
    private final String pricingServiceUrl;

    public PricingClient(RestTemplate restTemplate,
                         @Value("${pricing.base-url}") String pricingServiceUrl) {
        this.restTemplate = restTemplate;
        this.pricingServiceUrl = pricingServiceUrl;
    }

    @Retry(name = "pricing")
    @CircuitBreaker(name = "pricing", fallbackMethod = "getFallbackPrice")
    public double getPrice(long bookId) {
        String requestUrl = pricingServiceUrl + "/api/prices/" + bookId;
        Double priceResult = restTemplate.getForObject(requestUrl, Double.class);
        
        if (priceResult == null) {
            return 0.0;
        }
        return priceResult;
    }

    public double getFallbackPrice(long bookId, Throwable exception) {
        return 0.0;
    }
}