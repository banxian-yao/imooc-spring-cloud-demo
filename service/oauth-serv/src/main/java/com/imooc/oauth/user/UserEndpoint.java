package com.imooc.oauth.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class UserEndpoint {

    @RequestMapping(value = "/user/endpoint", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        log.info("principal={}", principal.toString());
        return principal;
    }
}