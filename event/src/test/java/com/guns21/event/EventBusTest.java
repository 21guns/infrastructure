package com.guns21.event;

import com.guns21.event.boot.config.EventBusConfig;
import com.guns21.event.domain.NotifyEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = EventBusConfig.class)
@Import(EventBusTest.EventBusTestConfig.class)
public class EventBusTest {

    @Autowired
    private EventBus eventBus;

    @Test
    public void testPublish() {

        eventBus.publish(new NotifyEvent() {});

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @TestConfiguration
    public static class EventBusTestConfig {
        @Bean
        public Listener listener() {
            return new Listener();
        }
    }

    public static class Listener {
        @EventListener
        public void test(NotifyEvent event) {
            System.err.println("dds");
        }
    }
}
