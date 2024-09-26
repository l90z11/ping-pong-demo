package priv.lee90.learn.util


import org.mockito.MockitoAnnotations
import spock.lang.Specification
import spock.lang.Title

@Title("LeakyBucketRateLimiter Test")
class LeakyBucketRateLimiterTest extends Specification {

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    def "test is Allowed"() {
        when:
        LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(1, 1)
        boolean result1 = leakyBucketRateLimiter.isAllowed()
        boolean result2 = leakyBucketRateLimiter.isAllowed()

        then:
        result1 == true
        result2 == false
    }
}