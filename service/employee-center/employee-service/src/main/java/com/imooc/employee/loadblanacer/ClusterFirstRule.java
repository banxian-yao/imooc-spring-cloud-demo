package com.imooc.employee.loadblanacer;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
// Component注释掉，不然会被当做默认的负载均衡策略使用
//@Component
public class ClusterFirstRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosProps;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        String cluster = nacosProps.getClusterName();
        String group = nacosProps.getGroup();
        BaseLoadBalancer lb = (BaseLoadBalancer) getLoadBalancer();

        String name = lb.getName();

        NamingService namingService = nacosServiceManager.getNamingService(nacosProps.getNacosProperties());
        try {
            List<Instance> instances = namingService.selectInstances(name, group, true);

            if (StringUtils.isNotBlank(cluster)) {
                List<Instance> clusterInstances = instances.stream()
                        .filter(i -> StringUtils.equalsIgnoreCase(i.getClusterName(), cluster))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(clusterInstances)) {
                    instances = clusterInstances;
                }
            }

            Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("lb error", e);
        }
        return null;
    }
}
