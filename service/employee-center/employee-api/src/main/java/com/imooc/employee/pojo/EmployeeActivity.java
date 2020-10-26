package com.imooc.employee.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeActivity {

    private Long id;

    // 员工工号
    private Long employeeId;

    // 活动类型
    private ActivityType activityType;

    // 目标ID
    private Long resourceId;

    // 开始时间
    private Date startTime;

    // 结束时间
    private Date endTime;

    // 当前是否有效
    private boolean active;

}
