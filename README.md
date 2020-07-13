# MyBatisJsonSerializeHandler
MyBatisJsonSerializeHandler

Usage:


public class YourBean<A> extends JsonSerializeHandler<A> {}  
        
OR  

public class YourBean<A<B>> extends JsonSerializeHandler<A<B>> {}

MyBatisTypeHandler config  

<resultMap id="BeanMapId" type="YourPath">  
        
        <result column="yourColumnName" property="yourPropertyName" typeHandler="YourBeanPath"/>  
        
</resultMap>  

