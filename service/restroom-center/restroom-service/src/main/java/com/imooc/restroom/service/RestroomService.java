package com.imooc.restroom.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.google.common.collect.Lists;
import com.imooc.restroom.converter.ToiletConverter;
import com.imooc.restroom.dao.ToiletDao;
import com.imooc.restroom.entity.ToiletEntity;
import com.imooc.restroom.pojo.Toilet;
import com.imooc.restroom.proto.beans.ToiletResponse;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("toilet-service")
@LocalTCC
@RefreshScope
public class RestroomService implements IRestroomTccService {

    @Autowired
    private ToiletDao toiletDao;

    @Value("${test.value:defaultvalue}")
    private String nacosValue;

    @Value("${restroom.disable:false}")
    private boolean disableRestroom;

    @Override
    @GetMapping("/get")
    @SentinelResource(value = "getToilet",
            blockHandler = "getToiletFallback"
            //, fallback = "getToiletFallback"
    )
    public Toilet getToilet(Long id) {
        ToiletEntity entity = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("toilet not found"));
        return ToiletConverter.convert(entity);
    }

    public Toilet getToiletFallback(Long id, BlockException ex) {
        return Toilet.builder()
                .test("blocked" + ex.getRuleLimitApp())
                .build();
    }


    @Override
    @GetMapping("/checkAvailable")
    @SentinelResource(value = "checkAvailable", fallback = "getAvailableToiletFallback")
    public List<Toilet> getAvailableToilet() {
//        throw new RuntimeException("test");
//        Thread.sleep(1000);
        if (disableRestroom) {
            log.info("restroom service is unavailable");
            return Lists.newArrayList();
        }
        List<ToiletEntity> result = toiletDao.findAllByCleanAndAvailable(true, true);

        return result.stream()
                .map(ToiletConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PostMapping("/occupy")
    @ResponseBody
    public Toilet occupy(Long id) {
        try {
            ToiletEntity entity = toiletDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("toilet not found"));

            if (!entity.isAvailable() || !entity.isClean()) {
                throw new RuntimeException("restromm is unavailable or unclean");
            }

            entity.setAvailable(false);
            entity.setClean(false);
            toiletDao.save(entity);

            return ToiletConverter.convert(entity);
        } catch (Exception e) {
            log.error("cannot occupy the restromm", e);
            throw e;
        }
    }

    @Override
    @Transactional
    @PostMapping("/release")
    public Toilet release(Long id) {
        try {
            ToiletEntity entity = toiletDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("toilet not found"));
            entity.setAvailable(true);
            entity.setClean(true);
            toiletDao.save(entity);

            return ToiletConverter.convert(entity);
        } catch (Exception e) {
            log.error("cannot occupy the restromm", e);
            throw e;
        }
    }

    @Override
    public void test(Long id) {
    }


    @GetMapping("/test")
    public Toilet test2(String id) {
        return Toilet.builder()
                .test(id)
                .build();
    }

//    @PostMapping("/testProto")
    @RequestMapping(value = "/testProto",
            produces = "application/x-protobuf",
            method = RequestMethod.POST)
    @ResponseBody
    public ToiletResponse testProto(@RequestParam("id") Long id) {
        log.info("test proto id={}, nacosValue={}", id, nacosValue);
        return ToiletResponse.newBuilder()
                .setId(id)
                .build();
    }

    @PostMapping("/testProto2")
    public Toilet testProto2(@RequestParam("id") Long id) {
        return Toilet.builder()
                .id(id)
                .build();
    }

    @Transactional
    @PostMapping("/releaseTCC")
    public Toilet releaseTCC(Long id) {
        try {
            log.info("**** Try release TCC **** id={}, xid={}", id, id);
            ToiletEntity entity = toiletDao.findById(id)
                    .orElseThrow(() -> new RuntimeException("toilet not found"));
            entity.setReserved(true);
            toiletDao.save(entity);
            return ToiletConverter.convert(entity);
        } catch (Exception e) {
            log.error("cannot occupy the restromm", e);
            throw e;
        }
    }

    @Transactional
    public boolean releaseCommit(BusinessActionContext actionContext) {
        try {
            log.info("class = " + actionContext.getActionContext("id").getClass());
            Long id = Long.parseLong(actionContext.getActionContext("id").toString());
            log.info("**** Confirm release TCC **** id={}, xid={}", id, actionContext);
            Optional<ToiletEntity> optional = toiletDao.findById(id);
            if (optional.isPresent()) {
                ToiletEntity entity = optional.get();
                entity.setClean(true);
                entity.setAvailable(true);
                entity.setReserved(false);
                toiletDao.save(entity);
            }
            return true;
        } catch (Exception e) {
            log.error("cannot occupy the restromm", e);
            return false;
        }
    }

    @Transactional
    public boolean releaseCancel(BusinessActionContext actionContext) {
        try {
            Long id = Long.parseLong(actionContext.getActionContext("id").toString());
            log.info("**** Cancel release TCC **** id={}, xid={}", id, actionContext);
            Optional<ToiletEntity> optional = toiletDao.findById(id);
            if (optional.isPresent()) {
                ToiletEntity entity = optional.get();
                entity.setClean(false);
                entity.setAvailable(false);
                entity.setReserved(false);
            }
            return true;
        } catch (Exception e) {
            log.error("cannot occupy the restromm", e);
            return false;
        }
    }
}
