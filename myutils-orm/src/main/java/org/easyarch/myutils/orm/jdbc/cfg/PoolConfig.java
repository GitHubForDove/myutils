package org.easyarch.myutils.orm.jdbc.cfg;/**
 * Description : 
 * Created by YangZH on 16-11-4
 *  下午3:57
 */

import java.util.Properties;

/**
 * Description :
 * Created by code4j on 16-11-4
 * 下午3:57
 */

public class PoolConfig {

    private static final String MAXPOOLSIZE = "pool.maxPoolSize";
    private static final String MINIDLE = "pool.minIdle";
    private static final String MAXIDLE = "pool.maxIdle";
    private static final String KEEPALIVETIME = "pool.keepAliveTime";

    private int maxPoolSize;

    private int minIdle;

    private int maxIdle;

    private long keepAliveTime;

    private Properties properties;
    private PoolConfig(){
        properties = new Properties();
    }

    public static PoolConfig config(int maxPoolSize,int maxIdle,
                              int minIdle,long keepAliveTime){
        PoolConfig config = new PoolConfig();
        config.maxPoolSize = maxPoolSize<=0?Integer.MAX_VALUE:maxPoolSize;
        config.minIdle = minIdle<=0?0:minIdle;
        config.maxIdle = maxIdle<=0?Integer.MAX_VALUE:maxIdle;
        config.keepAliveTime = keepAliveTime<=0?60:keepAliveTime;
        return config;
    }

    public static PoolConfig config(Properties prop){
        PoolConfig config = new PoolConfig();
        int maxPoolSize = prop.getProperty(MAXPOOLSIZE)==null?
                Runtime.getRuntime().availableProcessors()*4:Integer.parseInt(prop.getProperty(MAXPOOLSIZE));
        int minIdle = prop.getProperty(MINIDLE)==null?
                0:Integer.parseInt(prop.getProperty(MINIDLE));
        int maxIdle = prop.getProperty(MAXIDLE)==null?
                512:Integer.parseInt(prop.getProperty(MAXIDLE));
        int keepAliveTime = prop.getProperty(KEEPALIVETIME)==null?
                60:Integer.parseInt(prop.getProperty(KEEPALIVETIME));
        config.maxPoolSize = maxPoolSize<=0?Integer.MAX_VALUE:maxPoolSize;
        config.minIdle = minIdle<=0?0:minIdle;
        config.maxIdle = maxIdle<=0?Integer.MAX_VALUE:maxIdle;
        config.keepAliveTime = keepAliveTime<=0?60:keepAliveTime;
        ConnConfig.config(prop);
        return config;
    }

    public Properties getProperties(){
        properties.setProperty(MAXPOOLSIZE,String.valueOf(maxPoolSize));
        properties.setProperty(MINIDLE,String.valueOf(maxIdle));
        properties.setProperty(MAXIDLE,String.valueOf(maxIdle));
        properties.setProperty(KEEPALIVETIME,String.valueOf(keepAliveTime));
        return properties;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void print() {
        System.out.println("maxPoolSize:"+maxPoolSize+
                "\nmaxIdle:"+maxIdle+
                "\nminIdle:"+minIdle+
                "\nkeepAliveTime:"+keepAliveTime);
    }
}
