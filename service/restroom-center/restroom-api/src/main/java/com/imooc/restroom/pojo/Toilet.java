package com.imooc.restroom.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Toilet {

    private Long id;

    private boolean clean;

    private boolean available;

    private String test;

}
