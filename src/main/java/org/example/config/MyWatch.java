package org.example.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: liyq
 * @Description: zookeeper
 * @Date: 2021/4/12 - 14:03
 * @Version: 1.0
 **/
public class MyWatch implements Watcher, AsyncCallback.StatCallback,AsyncCallback.DataCallback {
    private ZooKeeper zk;
    private MyConfig myConfig;
    private CountDownLatch cd = new CountDownLatch(1);
    private String nodepath;

    public void aWait(){
        zk.exists(nodepath, this,this,"zaaa");
        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData(nodepath,this,this,"ccc");
                break;
            case NodeDeleted:
                //清空配置数据和阻塞状态并监听
                myConfig.setConf("");
                cd = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData(nodepath,this,this,"ddd");
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (stat != null){
            zk.getData(nodepath,this,this,"aaa");
        }

    }

    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if (null != bytes){
            myConfig.setConf(new String(bytes));
            cd.countDown();
        }

    }

    public String getNodepath() {
        return nodepath;
    }

    public void setNodepath(String nodepath) {
        this.nodepath = nodepath;
    }

    public CountDownLatch getCd() {
        return cd;
    }

    public void setCd(CountDownLatch cd) {
        this.cd = cd;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public MyConfig getMyConfig() {
        return myConfig;
    }

    public void setMyConfig(MyConfig myConfig) {
        this.myConfig = myConfig;
    }

}
