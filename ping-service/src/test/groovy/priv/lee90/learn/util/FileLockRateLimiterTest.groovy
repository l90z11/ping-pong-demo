package priv.lee90.learn.util

import spock.lang.*

class FileLockRateLimiterTest extends Specification {

    FileLockRateLimiter fileLockRateLimiter

    def setup() {
        fileLockRateLimiter = new FileLockRateLimiter("lockFilePath", "stateFilePath", 1)
    }

    def "test try Acquire"() {
        when:
        boolean result = fileLockRateLimiter.tryAcquire()

        then:
        result == true
    }
}

