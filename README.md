# ODBS

#### 介绍

Java对象序列化和反序列化，支持二进制和JSON格式

#### 软件架构

ODBS会预先扫描对象定义，根据对象定义建立对象描述信息，根据定义描述执行二进制和JSON序列化。
预先扫描对象定义是一种严格机制，这意味着序列化和反序列化必须是已知的对象否则将会收到异常；
这种机制的好处是性能更优，无须在序列化结果中嵌入过多的对象信息，并且结果是预知且安全的；
对象定义（实体对象）遵循JavaBean规范，ODBS将通过getter和setter方法获取和设置值，
这也意味着只有成对出现的setXXX()和getXXX()方法值会被序列化，
执行序列化时ODBS将通过getXXX()方法获取值，
执行反序列化时ODBS将通过setXXX()方法设置值。

1. 没有任何第三方依赖；
2. 支持值类型：boolean,byte,char,short,int,long,float,double；
3. 支持基础类型：Boolean,Byte,Character,Short,Integer,Long,Float,Double,BigDecimal,Date,LocalTime,LocalDate,LocalDateTime,String；
4. 支持枚举类型和自定义枚举类型：EnumCode,EnumText,EnumCodeText；
5. 支持集合类型：Array,List,Set,Map；
6. 无须在序列化实体对象中嵌入注解，遵循非常简单的规范即可。

JSON规范参考：RFC4627, [www.json.org](https://www.json.org)

#### 使用说明

添加 Maven 依赖，在项目的pom.xml文件中

```xml
<dependency>
	<groupId>com.joyzl</groupId>
	<artifactId>odbs</artifactId>
	<version>2.2.1</version>
</dependency>
```

1.  定义要用于序列化和反序列化的对象

```java
package com.joyzl.example;

public class Entity{

	private int index;
	private String name;
	private Set<String> functions;

	public int getIndex(){
		return index;
	}
	
	public void setIndex(int value){
		index = value;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String value){
		name = value;
	}

	public Set<String> getFunctions(){
		return functions;
	}

	public void setFunctions(Set<String> values){
		if(functions == null){
			functions = values;
		}else if(functions != values){
			functions.clear();
			functions.addAll(values);
		}
	}

	public boolean hasFunctions(){
		return functions != null && functions.size()>0;
	}
}
```

以上Entity对象在序列化时将调用getIndex(),getName(),getFunctions()获取对象值，
在执行反序列化时将调用setIndex(),setName(),setFunctions()设置对象值；

对象参照JavaBean规范，实体必须具有无参构造函数，反序列化过程中会自动通过无参构造函数创建对象，
实体对象不能存在循环引用，这将导致堆栈溢出(抛出StackOverflowError)；
getXXX/isXXX必须具有非void的返回值和public访问级别，setXXX必须具有与getXXX/isXXX返回类型相同的1个输入参数和public访问级别。
实体对象的getXXX方法不希望被序列化时，请调整命名方式，例如:将getName()改成theName()即可。
方法名不能使用中文,否则可能会导致签名校验失败。

2.  初始化ODBS对象实例

```java
// 扫描要序列化的对象包，建立对象描述实例
final ODBS odbs = ODBS.initialize("com.joyzl.example");

// 创建二进制序列化和反序列化对象实例
ODBSBinary odbsBINARY = new ODBSBinary(odbs);

// 创建JSON序列化和反序列化对象实例
ODBSJson odbsJSON = new ODBSJson(odbs);

```

ODBSBinary和ODBSJson分别对应二进制和JSON格式，实例对象可以重复使用，是多线程安全的。

如果你的项目工程使用了模块化，你应该在module-info.java文件中添加实体对象包对ODBS开放，
否则初始化时将出现不能访问实体类的异常。

```
exports com.joyzl.example to com.joyzl.odbs;
```

3.  执行对象序列化

```java
Entity entity = new Entity();
...
// 序列化对象实例为字节
odbsBINARY.writeEntity(entity, writer);
// 序列化对象实例为JSON字符
odbsJSON.writeEntity(entity, writer);
```

4.  执行反序列化对象

用已实例化对象执行反序列化

```java
Entity target = new Entity();
...
// 反序列化字节为对象实例
target = odbsBINARY.readEntity(target, reader);
// 反序列化JSON为对象实例
target = (Entity) odbsJSON.readEntity(target, reader);
```

用对象类型执行反序列化

```java
Entity target;
...
// 反序列化字节为对象实例
target = odbsBINARY.readEntity(Entity.class, reader);
// 反序列化JSON为对象实例
target = (Entity) odbsJSON.readEntity(Entity.class, reader);
```

#### 其它特性

##### JSON序列化对枚举的特殊处理

可设置枚举对象序列化输出为JSON对象或值，默认为输出枚举对象，如下格式：

```json
{
	"State": {
		"value": 8,
		"name": "DANGER",
		"text": "严重"
	}
}
```

其中"value"为枚举的值，"name"为枚举的定义名称，"text"为枚举的文本，
Java默认的枚举对象只能输出名称和值，如果要输出文本枚举对象应实现ODBS定义的EnumText或EnumCodeText接口。
可通过ODBSJson.setEnumObject(false)改变此默认行为。

```json
{
	"State": 8
}
```

枚举执行反序列化时不受此设置影响，两种格式均可获得正确得枚举值；
通常情况下输出枚举为对象，有助于WEB前端显示枚举文本，而前端请求时仅需要提供枚举值。

##### JSON序列化null值处理

默认情况下JSON序列化不会输出值为null的键名和值，这将有助于减少JSON字符数量；
已Entity为例，如果值均保持默认，仅输出以下字符：

```json
{
	"Index": 0
}
```

可通过ODBSJson.setIgnoreNull(false)改变此默认行为。

```json
{
	"Index": 0,
	"Name": null,
	"Functions": null
}
```

##### JSON日期时间

JSON序列化和反序列化日期时间的默认格式化为：
1. LocalTime 默认格式"HH:mm:ss"
2. LocalDate 默认格式"uuuu-MM-dd"
3. LocalDateTime 默认格式"uuuu-MM-dd HH:mm:ss"
4. Date 默认格式 "yyyy-MM-dd HH:mm:ss"
可通过ODBSJson.setTimeFormatter(DateTimeFormatter),ODBSJson.setDateFormatter(DateTimeFormatter),ODBSJson.setDateTimeFormatter(DateTimeFormatter),ODBSJson.setDateFormat(SimpleDateFormat)改变此默认行为。
Data对象已不推荐使用，建议使用LocalTime,LocalDate,LocalDateTime对象；
受日期时间格式化影响，JSON序列化和反序列化过程中将丢失时间的精度，
例如LocalTime包含纳秒部分，按默认格式序列化输出然后执行反序列化将丢失纳秒部分；
二进制格式序列化和反序列化不会丢失任何精度。

##### JSON键名格式

JSON序列化键名默认格式为大写字母开头驼峰格式 "UserName"，
可通过ODBSJson.setKeyNameFormat(JSONName)改变此默认行为，
此设置将同时影响JSON序列化和反序列化。

##### 二进制序列化对集合的限制

集合的泛型必须是明确的基本类型或实体对象类型并且位于被扫描的包中，
例如：List&#60;Entity&#62;是可被正确序列化，但是List&#60;Object&#62;将无法执行序列化；
集合的定义不应使用Collection&#60;T&#62;类型，应使用明确的Array、List、Set和Map；
当实体对象未默认实例化集合，此时无法准确确定集合类型时，默认采用ArrayList/HashSet/HashMap三种集合；
在数组和集合中不能含有null元素值，这将导致序列化失败，这主要基于以下两点考虑：
1. 在绝大部分应用场景中，数组和集合中的null元素并无实际意义；
2. 实现在数组和集合中能够传递null元素的多种序列化方案，都将导致字节增多。

Map集合的键类型不能使用Array/List/Set/Map作为Map集合的键类型，如果使用这些类型作为Map键类型将导致序列化失败。


#### 参与贡献

中翌智联 www.joyzl.com

华腾智联 www.huatens.com

张希 ZhangXi


---


中国制造，智造中国

Made in China, Intelligent China
