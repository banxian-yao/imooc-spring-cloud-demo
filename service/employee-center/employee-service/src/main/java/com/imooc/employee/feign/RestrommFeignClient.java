package com.imooc.employee.feign;

import com.imooc.employee.api.IRestroomService;
import com.imooc.employee.pojo.Toilet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "restroom-service")
public interface RestrommFeignClient extends IRestroomService {

    @GetMapping("/toilet-service/get")
    public Toilet getToilet(@RequestParam("id") Long id);

    @GetMapping("/toilet-service/checkAvailable")
    public List<Toilet> getAvailableToilet();

    @PostMapping("/toilet-service/occupy")
    public Toilet occupy(@RequestParam("id") Long id);

    @PostMapping("/toilet-service/release")
    public Toilet release(@RequestParam("id") Long id);

}
