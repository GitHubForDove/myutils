package org.easyarch.myutils.orm.parser;

import java.io.InputStream;

/**
 * Description :
 * Created by xingtianyu on 17-1-19
 * 上午10:01
 * description:
 */

public interface Parser<T> {

    public void parse(String src);

    public void parse(InputStream is);

    public T parse();

}
