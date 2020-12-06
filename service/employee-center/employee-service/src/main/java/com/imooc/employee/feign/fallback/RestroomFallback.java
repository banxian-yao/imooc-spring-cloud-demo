package com.imooc.employee.feign.fallback;

import com.imooc.employee.feign.RestroomFeignClient;
import com.imooc.restroom.pojo.Toilet;
import com.imooc.restroom.proto.beans.ToiletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Component
@Slf4j
public class RestroomFallback implements RestroomFeignClient {

    @Override
    public Toilet getToilet(Long id) {
        log.info("fallback");
        return null;
    }

    @Override
    public List<Toilet> getAvailableToilet() {
        log.info("fallback");
        return null;
    }

    @Override
    public Toilet occupy(Long id) {
        log.info("fallback");
        return null;
    }

    @Override
    public Toilet release(Long id) {
        log.info("fallback");
        return null;
    }

    @Override
    public void test(Long id) {
        log.info("fallback");
    }

    @Override
    public ResponseEntity<byte[]> test2(String id) {
        log.info("fallback");
        return null;
    }

    @Override
    public ToiletResponse proto(String id) {
        log.info("fallback");
        return null;
    }

    @Override
    public Toilet releaseTCC(Long id) {
        log.info("fallback");
        return null;
    }
}
