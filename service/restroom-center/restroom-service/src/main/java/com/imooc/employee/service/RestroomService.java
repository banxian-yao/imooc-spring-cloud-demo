package com.imooc.employee.service;

import com.imooc.employee.api.IRestroomService;
import com.imooc.employee.converter.ToiletConverter;
import com.imooc.employee.dao.ToiletDao;
import com.imooc.employee.entity.ToiletEntity;
import com.imooc.employee.pojo.Toilet;
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
        List<ToiletEntity> result = toiletDao.findAllByCleanAndAvailable(true, true);

        return result.stream()
                .map(ToiletConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkAvailability(Long id) {
        return false;
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

}
