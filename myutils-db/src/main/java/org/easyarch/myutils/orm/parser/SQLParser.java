package org.easyarch.myutils.orm.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.easyarch.myutils.collection.CollectionUtils;
import org.easyarch.myutils.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.easyarch.myutils.orm.parser.Token.PLACEHOLDER;
import static org.easyarch.myutils.orm.parser.Token.SEPERTOR;

/**
 * Description :
 * Created by xingtianyu on 17-1-11
 * 上午12:41
 * description:
 * 语法：
 * 1.select * from user where id = $user.id$    //对象反射取值
 * 2.select * from user where id = $map.id$     //从map的键中取值
 * 3.select * from user where id = $id$         //从@SqlParam中取值
 * 3.select * from user where id = ?            //通过左值表达式和@Column中的映射取值
 */
public class SQLParser implements Parser{

    private Statement statement;

    private String sql;

    private String preparedSql;

    private List<String> params;

    @Override
    public void parse(String src) {
        this.sql = src;
        preparedSql = sql;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        params = new ArrayList<>();
        Select select = (Select) statement;
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        Expression where = plain.getWhere();
        filterWhereColumns(where,params);
        for (String param:params){
            preparedSql = preparedSql.replace(StringUtils.center(param,0,SEPERTOR),PLACEHOLDER);
        }
    }
    public List<String> getSqlParams(){
        return params;
    }

    /**
     * @param whereAfter
     * @param params
     * 注意：Column对象在getColumnName的时候会根据 . 做分割
     */
    private void filterWhereColumns(Expression whereAfter,List<String> params){
        if (whereAfter instanceof Column){
            return;
        }
        if (whereAfter instanceof BinaryExpression){
            BinaryExpression binaryExpression = (BinaryExpression) whereAfter;
            Expression leftExpression = binaryExpression.getLeftExpression();
            Expression rightExpression = binaryExpression.getRightExpression();
            if (leftExpression instanceof Column &&rightExpression instanceof Column){
                String columnName = rightExpression.toString();
                params.add(columnName);
            }
            // 访问左子树
            filterWhereColumns(leftExpression,params);
            // 访问右子树
            filterWhereColumns(rightExpression,params);
        }else if (whereAfter instanceof Between){
            Between between = (Between) whereAfter;
            //between 没有只有左子树有column，右子树没有
            Expression frontVal = between.getBetweenExpressionStart();
            Expression backVal = between.getBetweenExpressionEnd();
            params.add(frontVal.toString());
            params.add(backVal.toString());
            filterWhereColumns(between.getLeftExpression(),params);
        }else if (whereAfter instanceof InExpression){
            InExpression inExpression = (InExpression) whereAfter;
            ItemsList itemsList = inExpression.getRightItemsList();
            if (itemsList instanceof ExpressionList){
                ExpressionList expressionList = (ExpressionList) itemsList;
                List<Expression> expressions = expressionList.getExpressions();
                if (CollectionUtils.isNotEmpty(expressions)){
                    for (Expression e:expressions){
                        params.add(e.toString());
                    }
                }
            }
            filterWhereColumns(inExpression.getLeftExpression(),params);
        }else if (whereAfter instanceof LikeExpression){
            LikeExpression likeExpression = (LikeExpression) whereAfter;
            Expression val = likeExpression.getRightExpression();
            params.add(val.toString());
            filterWhereColumns(likeExpression,params);
        }
    }

    public String getOriginSql(){
        return sql;
    }

    public String getPreparedSql(){
        return preparedSql;
    }
    public static void main(String[] args) throws JSQLParserException {
//        Statement statement = CCJSqlParserUtil.parse("select a,b,c from test where test.id = ? and oid in (?,?,?) " +
//                "and  user.age = ? and user.create_at between ? and ? and label like ?");
//        Select select = (Select) statement;
//        PlainSelect plain = (PlainSelect) select.getSelectBody();
//        Expression where = plain.getWhere();
        SQLParser parser = new SQLParser();
        parser.parse("select a,b,c from test where id = $user.id$ and oid in ($map.pid$,$map.oid$,$map.mid$) " +
                "and age = $map.age$ and create_at between $map.begin$ and $map.end$ and label like $map.label$");
        for (String param:parser.getSqlParams()){
            System.out.println(StringUtils.strip(param,SEPERTOR));
        }
        System.out.println("preparedSql:"+parser.getPreparedSql());
//        Map<Integer,String> map = new HashMap<>();
//        map.put(2,"2");
//        map.put(3,"3");
//        map.put(1,"1");
//        System.out.println(map.get(map.size()));
//        System.out.println(parser.getCurrentIndex(map));
    }


}