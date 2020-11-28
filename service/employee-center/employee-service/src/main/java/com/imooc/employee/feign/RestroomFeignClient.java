package com.imooc.employee.feign;

import com.imooc.employee.FeignProtoConfiguration;
import com.imooc.restroom.pojo.Toilet;
import com.imooc.restroom.proto.beans.ToiletResponse;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(value = "restroom-service")
public interface RestroomFeignClient {

    @GetMapping("/toilet-service/checkAvailability")
    public Toilet getToilet(@RequestParam("id") Long id);

    @GetMapping("/toilet-service/checkAvailable")
    public List<Toilet> getAvailableToilet();

    @PostMapping("/toilet-service/occupy")
    public Toilet occupy(@RequestParam("id") Long id);

    @PostMapping("/toilet-service/release")
    public Toilet release(@RequestParam("id") Long id);

    // 测试方法
    @GetMapping("/toilet-service/checkAvailability")
    public void test(@RequestParam("id") Long id);

    @GetMapping("/toilet-service/test")
    public ResponseEntity<byte[]> test2(@RequestParam("id") String id);

    @RequestMapping(value = "/toilet-service/testProto",
            method = POST,
            consumes = "application/x-protobuf",
            produces = "application/x-protobuf")
    public ToiletResponse proto(@RequestParam("id") String id);

//    @PostMapping("/toilet-service/releaseTCC")
//    public Toilet releaseTCC(@RequestBody BusinessActionContext actionContext,
//                                     @RequestParam("id") Long id);

    @PostMapping("/toilet-service/releaseTCC")
    public Toilet releaseTCC(@RequestParam("id") Long id);

}
