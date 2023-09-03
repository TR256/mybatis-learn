package plugin;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Properties;

/**
 * @author: tr256
 * @time: 2023/4/20
 */
@Intercepts({
        @Signature(type = Executor.class,//类Class
        method = "query",//方法名称
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class//参数列表class
        })
})
public class TestPlugin implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println(invocation.getMethod());
        System.out.println(invocation.getTarget());
        Object[] args = invocation.getArgs();
        for (Object arg : args) {
            System.out.println("arg: " + arg);
        }
        System.out.println(invocation.getClass());
        System.out.println("拦截方法");
        System.out.println("////////////////////////////////////////////////////////////");

        MappedStatement mappedStatement = (MappedStatement) args[0];
        MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
        if (args[1] != null){
            if (args[1] instanceof MapperMethod.ParamMap){
                System.out.println("map");
                paramMap.putAll((Map<? extends String, ?>) args[1]);
            }else {
                System.out.println(args[1].getClass());
                String mappedStatementId = mappedStatement.getId();
                System.out.println(mappedStatementId);
                Class<?> parameterType = args[1].getClass();
                System.out.println(parameterType);
                String className = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
                String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf("." )+1);
                Class<?> mapperClass = Class.forName(className);
                Method method = mapperClass.getMethod(methodName, parameterType);
                Parameter[] parameters = method.getParameters();
                String parameterName = parameters[0].getName();
                System.out.println(args[1]);
                System.out.println(parameterName);
                for (String s : paramMap.keySet()) {
                    System.out.println(s + "\t\t\t" + paramMap.get(s));
                }
                paramMap.put(parameterName, args[1]);
            }
        }
        paramMap.put("test", 1111);
        args[1] = paramMap;
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        System.out.println("将插件添加到拦截器链中");
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        System.out.println("读取配置文件属性值"+properties);
    }
}
