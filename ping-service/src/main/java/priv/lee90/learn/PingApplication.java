package priv.lee90.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import priv.lee90.learn.service.RequestPongTimer;

import java.util.concurrent.ExecutionException;

/**
 * @author Ethan Lee
 * ping service
 */
@SpringBootApplication
public class PingApplication {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PingApplication.class, args);
        RequestPongTimer requestPongTimer = applicationContext.getBean(RequestPongTimer.class);
        requestPongTimer.helloPong();
    }
}
