package org.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.example.config.MyConfig;
import org.example.config.MyWatch;
import org.example.config.ZKUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: liyq
 * @Description: 测试
 * @Date: 2021/4/12 - 13:55
 * @Version: 1.0
 **/
public class ConfigTest {
    ZooKeeper zk;
    String codePath = "/a";
    String dataValue = "123456";

    @Before
    public void init(){
        zk = ZKUtils.getZookeeper();
    }
    @Test
    public void TestConfig(){
        MyWatch myWatch = new MyWatch();
        MyConfig myConfig = new MyConfig();
        myWatch.setMyConfig(myConfig);
        myWatch.setZk(zk);
        myWatch.setNodepath(codePath);
        try {
            String s = zk.create(codePath, dataValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(s);

        } catch (Exception e) {
            e.printStackTrace();
        }

        myWatch.aWait();

        while (true){
            System.out.println(myConfig.getConf());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
