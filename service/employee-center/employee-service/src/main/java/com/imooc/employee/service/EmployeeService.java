package com.imooc.employee.service;

import com.imooc.employee.api.IEmployeeActivityService;
import com.imooc.employee.api.IRestroomService;
import com.imooc.employee.dao.EmployeeActivityDao;
import com.imooc.employee.entity.EmployeeActivityEntity;
import com.imooc.employee.feign.RestroomFeignClient;
import com.imooc.employee.pojo.ActivityType;
import com.imooc.employee.pojo.EmployeeActivity;
import com.imooc.employee.pojo.Toilet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeService implements IEmployeeActivityService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmployeeActivityDao employeeActivityDao;

    @Autowired
    private RestroomFeignClient restroomService;

    @Transactional
    @PostMapping("/test")
    // 注意要打开配置文件中的压缩支持
    public ResponseEntity<byte[]> test(Long count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2000; i++) {
            sb.append(i);
        }
        return restroomService.test2(sb.toString());
//        restTemplate.getForObject(
//                "http://restroom-service/toilet-service/checkAvailability?id="+count,
//                Boolean.class);
    }

    @Override
    @Transactional
    @PostMapping("/toilet-break")
    public EmployeeActivity useToilet(Long employeeId) {
        int count = employeeActivityDao.countByEmployeeIdAndActivityTypeAndActive(
                employeeId, ActivityType.TOILET_BREAK, true);
        if (count > 0) {
            throw new RuntimeException("快拉！");
        }

        List<Toilet> toilets = restroomService.getAvailableToilet();
        if (CollectionUtils.isEmpty(toilets)) {
            throw new RuntimeException("shit in urinal");
        }

        // 抢坑，分布式事务一致性后面再说
        Toilet toilet = restroomService.occupy(toilets.get(0).getId());

        // 保存如厕记录
        EmployeeActivityEntity toiletBreak = EmployeeActivityEntity.builder()
                .employeeId(employeeId)
                .active(true)
                .activityType(ActivityType.TOILET_BREAK)
                .resourceId(toilet.getId())
                .build();
        employeeActivityDao.save(toiletBreak);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(toiletBreak, result);
        return result;
    }

    @Override
    @Transactional
    @PostMapping("/done")
    public EmployeeActivity restoreToilet(Long activityId) {
        EmployeeActivityEntity record = employeeActivityDao.findById(activityId)
                .orElseThrow(() -> new RuntimeException("record not found"));

        if (!record.isActive()) {
            throw new RuntimeException("activity is no longer active");
        }

        // 分布式事务一致性后面再说
        restroomService.release(record.getResourceId());

        record.setActive(false);
        record.setEndTime(new Date());
        employeeActivityDao.save(record);

        EmployeeActivity result = new EmployeeActivity();
        BeanUtils.copyProperties(record, result);
        return result;
    }
}
