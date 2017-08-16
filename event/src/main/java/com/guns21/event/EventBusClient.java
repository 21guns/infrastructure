package com.guns21.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EventBusClient {
    //spring cloud 系统内部事件队列
    String INPUT = "event-bus-input";
    String OUTPUT = "event-bus-output";

    @Output("event-bus-output")
    MessageChannel eventBusOutput();

    @Input("event-bus-input")
    SubscribableChannel eventBusInput();

    //和非spring cloud系统事件队列
    String INPUT_EXT = "event-bus-input-ext";
    String OUTPUT_EXT = "event-bus-output-ext";

    @Output("event-bus-output-ext")
    MessageChannel eventBusOutputExt();

    @Input("event-bus-input-ext")
    SubscribableChannel eventBusInputExt();
}