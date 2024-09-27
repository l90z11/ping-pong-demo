package priv.lee90.learn.util

import spock.lang.*

class FileLockRateLimiterTest extends Specification {

    FileLockRateLimiter fileLockRateLimiter

    def "test try Acquire true"() {
        when:
        def seconds = System.currentTimeSeconds()
        fileLockRateLimiter = new FileLockRateLimiter(seconds + "ping.lock", seconds + "pingLockState.txt", 1)
        boolean result = fileLockRateLimiter.tryAcquire()

        then:
        result == true
    }

    def "test try Acquire"() {
        when:
        fileLockRateLimiter = new FileLockRateLimiter("ping.lock", "pingLockState.txt", 1)
        def result1 = fileLockRateLimiter.tryAcquire()
        def result2 = fileLockRateLimiter.tryAcquire()

        then:
        result1 == true
        result2 == false
    }
}

