package org.easyarch.myutils.orm.jdbc.handler;/**
 * Description : 
 * Created by YangZH on 16-11-3
 *  上午12:00
 */

import org.easyarch.myutils.orm.jdbc.wrapper.BeanWrapper;
import org.easyarch.myutils.orm.jdbc.wrapper.Wrapper;
import org.easyarch.myutils.orm.binding.FieldBinder;

import java.sql.ResultSet;
import java.util.List;

/**
 * Description :
 * Created by code4j on 16-11-3
 * 上午12:00
 */

public class BeanListResultSetHadler<T> implements ResultSetHandler<List<T>> {

    protected Wrapper<T> wrapper;

    protected Class<T> type;

    public BeanListResultSetHadler(Class<T> type){
        this(new BeanWrapper<T>(new FieldBinder(type)),type);
    }

    public BeanListResultSetHadler(Wrapper<T> wrapper ,Class<T> type) {
        this.wrapper = wrapper;
        this.type = type;
    }


    @Override
    public List<T> handle(ResultSet rs) throws Exception {
        return wrapper.list(rs, type);
    }

}
