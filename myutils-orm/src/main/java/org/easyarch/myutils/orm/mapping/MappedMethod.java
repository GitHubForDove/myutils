package org.easyarch.myutils.orm.mapping;

import org.easyarch.myutils.collection.CollectionUtils;
import org.easyarch.myutils.orm.annotation.sql.SqlParam;
import org.easyarch.myutils.orm.build.SqlBuilder;
import org.easyarch.myutils.orm.cache.CacheFactory;
import org.easyarch.myutils.orm.cache.SqlMapCache;
import org.easyarch.myutils.orm.entity.SqlEntity;
import org.easyarch.myutils.orm.session.Configuration;
import org.easyarch.myutils.orm.session.impl.MapperDBSession;
import org.easyarch.myutils.reflection.ReflectUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.easyarch.myutils.orm.parser.Token.BIND_SEPARATOR;

/**
 * Description :
 * Created by xingtianyu on 17-1-22
 * 下午11:17
 * description:
 */

public class MappedMethod {
    private MapperDBSession session;

    private CacheFactory factory = CacheFactory.getInstance();

    public MappedMethod(MapperDBSession session) {
        this.session = session;
    }

//        String sql = "select * from t_user where id = $id$ and username like CONCAT('%',$username$,'%') and c > $age$";
    public Object delegateExecute(String interfaceName, Method method, Object[] args) {
        Configuration configuration = session.getConfiguration();
        SqlMapCache cache = factory.getSqlMapCache();
        ///检查缓存的sql
        SqlBuilder builder = new SqlBuilder();
        if (cache.isHit(interfaceName,method.getName())){
            System.out.println("sqlBuilder hit the cache");
            SqlEntity entity = cache.getSqlEntity(interfaceName,method.getName());
            builder.buildEntity(entity);
        }else{
            System.out.println("sqlBuilder didnt hit the cache");
            Parameter[] parameters = method.getParameters();
            String[] paramNames = ReflectUtils.getMethodParameter(method);
            int paramIndex = 0;
            for (int index=0;index<parameters.length;index++) {
                if (args[index] instanceof Map) {
                    builder.buildParams((Map<String,Object>)args[index]);
                    continue;
                }
                if (ReflectUtils.isFrequentlyUseType(parameters[index].getType())) {
                    SqlParam sqlParam = parameters[index].getAnnotation(SqlParam.class);
                    if (sqlParam == null) {
                        builder.buildParams(args[index]);
                        paramIndex++;
                    }else{
                        builder.buildParams(args[index],sqlParam.name());
                    }
                }else{
                    builder.buildParams(args[index]);
                }
            }
            //先构造参数，根据参数获得动态sql,然后缓存
            SqlEntity entity = new SqlEntity();
            entity.setParams(CollectionUtils.flatMapLists(builder.getMapperParameters()));
            entity.setBinder(interfaceName + BIND_SEPARATOR + method.getName());
            configuration.parseMappedSql(entity);
            String sql = configuration.getMappedSql(interfaceName, method.getName());
            //jsqlparser 在这一步，相对其他代码会慢一点
            builder.buildSql(sql);
            builder.prepareParams();
            SqlEntity se = builder.buildEntity(interfaceName + BIND_SEPARATOR + method.getName());
            cache.addSqlEntity(se);
        }
        System.out.println("sql param goto:"+builder.getParameters());
        switch (builder.getType()){
            case SELECT:
                Class<?> returnType = ReflectUtils.getReturnType(method);
                if (Collection.class.isAssignableFrom(returnType)){
                    return session.selectList(builder.getPreparedSql(),
                            ReflectUtils.getGenericReturnType(method),
                            CollectionUtils.gatherMapListsValues(builder.getParameters()));
                }else{
                    return session.selectOne(builder.getPreparedSql(),
                            returnType, CollectionUtils.gatherMapListsValues(builder.getParameters()));
                }
            case INSERT:
                System.out.println("go to insert params:"+builder.getParameters());
                return session.insert(builder.getPreparedSql(),
                    CollectionUtils.gatherMapListsValues(builder.getParameters()));
            case UPDATE:return session.update(builder.getPreparedSql(),
                    CollectionUtils.gatherMapListsValues(builder.getParameters()));
            case DELETE:return session.delete(builder.getPreparedSql(),
                    CollectionUtils.gatherMapListsValues(builder.getParameters()));
        }
        return null;
    }


    public void method(@SqlParam(name = "b")  String b, @SqlParam(name = "a") String a,@SqlParam(name = "c") String c) {
    }

    public int insert(int i){
        return 0;
    }
    public List<String> query(Map<String,Object> map){
        return new ArrayList<>();
    }

    public static void main(String[] args) throws NoSuchMethodException, IOException, InterruptedException {
//        List<String> list = new ArrayList();
//        Class clazz = list.getClass();
//        System.out.println(Collection.class.isAssignableFrom(clazz));
//        Method method = MappedMethod.class.getDeclaredMethod("query", Map.class);
//        Type returnType = method.getGenericReturnType();// 返回类型
//        System.out.println("  " + returnType);
//        if (returnType instanceof ParameterizedType)/**//* 如果是泛型类型 */{
//            Type[] types = ((ParameterizedType) returnType)
//                    .getActualTypeArguments();// 泛型类型列表
//            System.out.println("  TypeArgument: ");
//            for (Type type : types) {
//                System.out.println("   " + type);
//            }
//        }
//        Class cls = UserMapper.class;
//        Class clss = MappedMethod.class;
//        Method method = cls.getMethod("findById",String.class);
//        Method mthd = clss.getMethod("query",Map.class);
//        System.out.println(ReflectUtils.getReturnType(mthd));

//        System.out.println("paramNames:"+paramNames[0]);
        Runtime.getRuntime().exec("top").waitFor();
    }

}
