package org.easyarch.myutils.orm.pool.ds;

import java.sql.Connection;

/**
 * Description :
 * Created by xingtianyu on 16-12-31
 * 上午10:15
 * description:
 */

public interface Monitor {

    public void onBroken(Connection connection);
    public void onCreate(Connection connection);
}
