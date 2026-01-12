package ma.abla.pricing_service.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/prices")
public class PricingController {

    private static final double BASE_PRICE = 50.0;
    private static final double PRICE_INCREMENT = 5.0;
    private static final int FAILURE_PROBABILITY = 30;

    @GetMapping("/{bookId}")
    public double getBookPrice(@PathVariable("bookId") long bookId,
                               @RequestParam(name = "fail", defaultValue = "false") boolean shouldFail) {

        if (shouldFail) {
            throw new IllegalStateException("Pricing down (forced)");
        }

        ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();
        int randomValue = randomGenerator.nextInt(100);
        
        if (randomValue < FAILURE_PROBABILITY) {
            throw new IllegalStateException("Random failure");
        }

        long remainder = bookId % 10;
        double calculatedPrice = BASE_PRICE + (remainder * PRICE_INCREMENT);
        return calculatedPrice;
    }
}
