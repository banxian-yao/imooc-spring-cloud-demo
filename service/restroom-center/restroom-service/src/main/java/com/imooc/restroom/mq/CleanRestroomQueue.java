package com.imooc.restroom.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CleanRestroomQueue {

    String INPUT = "rm-consumer";

    String OUTPUT = "rm-producer";

    @Input(INPUT)
    public SubscribableChannel input();

    @Output(OUTPUT)
    public MessageChannel output();
}
