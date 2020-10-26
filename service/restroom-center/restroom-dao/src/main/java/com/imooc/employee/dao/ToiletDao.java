package com.imooc.employee.dao;

import com.imooc.employee.entity.ToiletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToiletDao extends JpaRepository<ToiletEntity, Long> {

    List<ToiletEntity> findAllByCleanAndAvailable(boolean clean, boolean available);

}
