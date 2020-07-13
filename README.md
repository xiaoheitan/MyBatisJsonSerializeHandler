# MyBatisJsonSerializeHandler
MyBatisJsonSerializeHandler

Usage:  

public class YourBean&lt;A&gt; {  
　　private A&lt;B&gt; a;  
　　public void setA(A&lt;B&gt; a)  {  
　　　　this.a = a;  
　　}  
　　public A&lt;B&gt; getA()  {  
　　　　return this.a;  
　　}  
}  

public class YourHandler&lt;A&lt;B&gt;&gt; extends JsonSerializeHandler&lt;A&lt;B&gt;&gt; {}

MyBatisTypeHandler config  

&lt;resultMap id="BeanMapId" type="YourPath"&gt;  
        &lt;result column="yourColumnName" property="yourPropertyName" typeHandler="YourHandlerPath"/&gt;  
&lt;/resultMap&gt;

