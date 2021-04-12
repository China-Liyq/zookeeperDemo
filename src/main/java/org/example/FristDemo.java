package org.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: liyq
 * @Description: 连接zookeeper并使用watch callback
 * @Date: 2021/4/12 - 11:46
 * @Version: 1.0
 **/
public class FristDemo {
    private final static String ports = "192.168.0.112:2181,192.168.0.115:2181,192.168.0.116:2181,192.168.0.117:2181";

    private final static String codePath = "/aaaa1";


    public static void main(String[] args) throws Exception {
        System.out.println("start zookeeper ");
        CountDownLatch cd = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(ports, 10000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("首次启动");
                String eventPath = event.getPath();
                Event.EventType eventType = event.getType();
                Event.KeeperState state = event.getState();
                System.out.println(event + ",\t" + eventPath + ",\t" + eventType + ",\t" + state);
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("已连接  ed....");
                        cd.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
                switch (eventType) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }
            }
        });
        System.out.println("阻塞状态进入1");
        cd.await();
        System.out.println("阻塞结束进2222");
        Stat stat;
        String s = zk.create(codePath, "fristValues".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        stat = zk.setData(codePath, "fristValues".getBytes(), 0);
        System.out.println("路径："+s);
        stat = new Stat();
        byte[] value = zk.getData(codePath, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("*******开始*********");
                System.out.println("检查数据change");
                String eventPath = event.getPath();
                Event.EventType eventType = event.getType();
                Event.KeeperState state = event.getState();
                System.out.println(event + ",\n" + eventPath + "," + eventType + "," + state);
                try {
                    byte[] data = zk.getData(codePath, this, stat);
                    System.out.println("变更的值为：" + new String(data));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                zk.getData(codePath,this, new AsyncCallback.DataCallback() {
//                    @Override
//                    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
//                        System.out.println("*****");
//                        System.out.println(i);
//                        System.out.println(s);
//                        System.out.println(o);
//                        System.out.println(new String(bytes));
//                        System.out.println(stat);
//                        System.out.println("*****");
//                    }
//                },"chuanru");
                System.out.println("*******end*********");
            }
        }, stat);
        System.out.println("取出值为22:" + new String(value));


        Stat stat1 = zk.setData(codePath, "123456".getBytes(), 0);
        System.out.println("初始stat1："+stat1);
        byte[] data = zk.getData(codePath, false, stat);
        System.out.println("外部"+new String(data));
        Stat stat2 = zk.setData(codePath, "abcd".getBytes(), stat1.getVersion());
        data = zk.getData(codePath, false, stat);
        System.out.println("外部"+new String(data));
        System.out.println("初始stat2："+stat2);




    }

}
