package com.imooc.restroom.service;

import com.imooc.restroom.pojo.Toilet;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface IRestroomTccService extends com.imooc.restroom.api.IRestroomService {

    @TwoPhaseBusinessAction(
            name = "releaseTCC",
            commitMethod = "releaseCommit",
            rollbackMethod = "releaseCancel"
    )
    public Toilet releaseTCC(
                             @BusinessActionContextParameter(paramName = "id") Long id);

    public boolean releaseCommit(BusinessActionContext actionContext);

    public boolean releaseCancel(BusinessActionContext actionContext);

}