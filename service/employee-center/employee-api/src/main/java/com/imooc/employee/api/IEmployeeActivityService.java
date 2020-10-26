package com.imooc.employee.api;

import com.imooc.employee.pojo.EmployeeActivity;

public interface IEmployeeActivityService {

    EmployeeActivity useToilet(Long employeeId);

    EmployeeActivity restoreToilet(Long employeeId);

}
