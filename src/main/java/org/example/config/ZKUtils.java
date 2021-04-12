package org.example.config;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: liyq
 * @Description: 获取zookeeper类
 * @Date: 2021/4/11 - 22:37
 * @Version: 1.0
 **/
public class ZKUtils {
    private static final String ports= "192.168.0.112:2181,192.168.0.115:2181,192.168.0.116:2181,192.168.0.117:2181/aaaa1";
    private static ZooKeeper zk;
    private static CountDownLatch dc = new CountDownLatch(1);
    private static Watcher watcher = new DefaultWatch(dc);

    public static ZooKeeper getZookeeper(){
        try {
            zk = new ZooKeeper(ports,10000,watcher);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }


}
