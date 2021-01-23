package com.imooc.restroom.mq;

import com.imooc.restroom.dao.ToiletDao;
import com.imooc.restroom.entity.ToiletEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CleanRestroomConsumer {

    @Autowired
    private ToiletDao toiletDao;

    @StreamListener(CleanRestroomQueue.INPUT)
    @Transactional
    public void consume(Message<Long> message,
                        @Header("version") String version) throws InterruptedException {

        if (StringUtils.equalsIgnoreCase(version, "1.0")) {
            log.info("api 1.0 is no longer supported");
            return;
        }

        log.info("message consumed, body={}", message);
        ToiletEntity entity = toiletDao.findById(message.getPayload())
                .orElseThrow(() -> new RuntimeException("toilet not found"));
        entity.setAvailable(true);
        entity.setClean(true);
        toiletDao.save(entity);

    }

//    // 多次失败触发降级流程
//    @ServiceActivator(inputChannel = "request-coupon-topic.coupon-user-serv-group.errors")
//    public void fallback(Message<RequestCoupon> message) {
//        log.info("fallback logic here");
//        throw new RuntimeException("error");
//    }
}
