package org.example.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: liyq
 * @Description: 链接默认watch
 * @Date: 2021/4/12 - 13:45
 * @Version: 1.0
 **/
public class DefaultWatch implements Watcher {
    //阻塞对象
    private CountDownLatch cd;
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
        Event.KeeperState state = watchedEvent.getState();//状态
        Event.EventType type = watchedEvent.getType();
        switch (state) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                System.out.println("连接成功..");
                //阻塞结束
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
    }
    public DefaultWatch(CountDownLatch cd) {
        this.cd = cd;
    }
    public CountDownLatch getCd() {
        return cd;
    }
    public void setCd(CountDownLatch cd) {
        this.cd = cd;
    }
}
