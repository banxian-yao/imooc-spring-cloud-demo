package com.imooc.restroom.converter;

import com.imooc.restroom.entity.ToiletEntity;
import com.imooc.restroom.pojo.Toilet;

public class ToiletConverter {



    public static Toilet convert(ToiletEntity entity) {
        return Toilet.builder()
                .id(entity.getId())
                .clean(entity.isClean())
                .available(entity.isAvailable())
                .build();
    }

}
