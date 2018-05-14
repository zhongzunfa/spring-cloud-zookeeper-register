package com.web;


import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.cloud.zookeeper.support.StatusConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private ZookeeperDiscoveryClient client;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/hello")
    public String GetHelloString(@RequestParam String userName){
        return "Hello:"+userName+" ,I am from 8811 port !";
    }


    @RequestMapping("/del")
    @ResponseBody
    public String del() throws Exception {

        List<String> services = client.getServices();
        for (String service : services){
            List<org.springframework.cloud.client.ServiceInstance> instances = client.getInstances(service);
            for (org.springframework.cloud.client.ServiceInstance instance : instances){

                Collection<ServiceInstance<ZookeeperInstance>> zkInstances = serviceDiscovery.queryForInstances(instance.getServiceId());
                for (ServiceInstance<ZookeeperInstance> zkInstance : zkInstances) {
                    ZookeeperServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(serviceDiscovery);
                    ZookeeperRegistration registration = ServiceInstanceRegistration.builder()
                            .uriSpec(zkInstance.getUriSpec())
                            .address(zkInstance.getAddress())
                            .port(zkInstance.getPort())
                            .name(zkInstance.getName())
                            .payload(zkInstance.getPayload())
                            .build();

                    if(null == zkInstance.getPayload()){
                        System.out.println("unknown");
                    }else{
                        Object status = serviceRegistry.getStatus(registration);
                        System.out.println("this status is :  " + status);
                        if(zkInstance.getName().equals("spring-cloud-zookeeper-server2")){
                            serviceRegistry.setStatus(registration, StatusConstants.STATUS_OUT_OF_SERVICE);
                        }
                    }
                }
            }
        }


        return "执行完成";
    }

}
