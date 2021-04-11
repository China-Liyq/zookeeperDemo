package org.example;

import com.sun.org.glassfish.external.statistics.Stats;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App {
    private static String path = "/xxx1";
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );
        CountDownLatch cd = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("192.168.0.112:2181,192.168.0.115:2181,192.168.0.116:2181,192.168.0.117:2181",
                300000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                System.out.println(event.toString());
                System.out.println("启动");
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected....");
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

                switch (type) {
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
        cd.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("connet...ing...");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("connet...ed...");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }
        Stat stat = new Stat();
        String pathName = zk.create(path, "abcdef".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(pathName);

        byte[] data = zk.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("回调的内容---getData Watch：\t" + event.toString()+"|\t|"+event.getPath());
                //再次回调并监听
                try {
                    zk.getData(path,this,stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println(stat);
        System.out.println("---------------------");
        System.out.println(new String(data));
        System.out.println("---------------------");

        System.out.println("首次");
        Stat stat1 = zk.setData("/xxx1", "newxxx1".getBytes(), 0);
        byte[] data1 = zk.getData(path, false, stat);
        System.out.println(new String(data1));
        System.out.println("再次");
        Stat stat2 = zk.setData("/xxx1", "newxxx2".getBytes(), stat1.getVersion());
        byte[] data2 = zk.getData(path, false, stat);
        System.out.println(new String(data2));
        System.out.println("**************异步开始******************");
        zk.getData(path,false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("asyn--------doing");
                System.out.println("i:"+i+",\ts:"+s+",\to:"+o);
                System.out.println(new String(bytes));
                System.out.println(stat);
            }
        },"abc");
        System.out.println("**************异步结束******************");
        Thread.sleep(120222);
    }
}
