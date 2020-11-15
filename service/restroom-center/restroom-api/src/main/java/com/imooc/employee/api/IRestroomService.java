package com.imooc.employee.api;

import com.imooc.employee.pojo.Toilet;

import java.util.List;

public interface IRestroomService {

    public Toilet getToilet(Long id);

    public List<Toilet> getAvailableToilet();

    public Toilet occupy(Long id);

    public Toilet release(Long id);

    abstract void test(Long id);
}
