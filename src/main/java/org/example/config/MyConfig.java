package org.example.config;

/**
 * @Author: liyq
 * @Description: zookeeper
 * @Date: 2021/4/12 - 14:10
 * @Version: 1.0
 **/
public class MyConfig {
    private String conf;

    @Override
    public String toString() {
        return "MyConfig{" +
                "conf='" + conf + '\'' +
                '}';
    }


    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}
