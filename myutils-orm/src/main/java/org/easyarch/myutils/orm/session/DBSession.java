package org.easyarch.myutils.orm.session;

import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by xingtianyu on 16-12-28
 * 上午1:09
 * description:
 */

public interface DBSession {

    public <T> T selectOne(String bindOrSql,Class<T> clazz,Object ...parameter);

    public <E> List<E> selectList(String bindOrSql,Class<E> clazz,Object... parameter);

    public int selectCount(String bindOrSql,Object... parameters);

    public List<Map<String,Object>> selectMap(String bindOrSql,Object ... parameters);

    public int update(String bindOrSql,Object... parameter);

    public int delete(String bindOrSql,Object... parameter);

    public int insert(String bindOrSql,Object... parameter);

    public <T> T getMapper(Class<T> clazz);

    public Configuration getConfiguration();
    public void close();
    public void rollback();

}
