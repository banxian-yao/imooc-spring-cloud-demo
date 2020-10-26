package com.imooc.employee.dao;

import com.imooc.employee.entity.EmployeeActivityEntity;
import com.imooc.employee.pojo.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeActivityDao extends JpaRepository<EmployeeActivityEntity, Long> {

    int countByEmployeeIdAndActivityTypeAndActive(Long employeeId, ActivityType activityType, boolean active);

}
