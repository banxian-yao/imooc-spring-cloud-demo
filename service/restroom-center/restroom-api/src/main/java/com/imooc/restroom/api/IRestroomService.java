package com.imooc.restroom.api;

import com.imooc.restroom.pojo.Toilet;

import java.util.List;

public interface IRestroomService {

    public Toilet getToilet(Long id);

    public List<Toilet> getAvailableToilet();

    public Toilet occupy(Long id);

    public Toilet release(Long id);

    abstract void test(Long id);
    public Toilet test2(String id);
}
