package com.imooc.restroom.service;

import com.imooc.restroom.api.IRestroomService;
import com.imooc.restroom.converter.ToiletConverter;
import com.imooc.restroom.dao.ToiletDao;
import com.imooc.restroom.entity.ToiletEntity;
import com.imooc.restroom.pojo.Toilet;
import com.imooc.restroom.proto.beans.ToiletRequest;
import com.imooc.restroom.proto.beans.ToiletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("toilet-service")
public class RestroomService implements IRestroomService {

    @Autowired
    private ToiletDao toiletDao;

    @Override
    @GetMapping("/get")
    public Toilet getToilet(Long id) {
        ToiletEntity entity = toiletDao.findById(id)
                .orElseThrow(() -> new RuntimeException("toilet not found"));
        return ToiletConverter.convert(entity);
    }

    @Override
    @GetMapping("/checkAvailable")
    public List<Toilet> getAvailableToilet() {

//        throw new RuntimeException("test");
//        Thread.sleep(1000);
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
        log.info("test proto id={}", id);
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
}
