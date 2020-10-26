package com.imooc.employee.converter;

import com.imooc.employee.entity.ToiletEntity;
import com.imooc.employee.pojo.Toilet;

public class ToiletConverter {



    public static Toilet convert(ToiletEntity entity) {
        return Toilet.builder()
                .id(entity.getId())
                .clean(entity.isClean())
                .available(entity.isAvailable())
                .build();
    }

}
