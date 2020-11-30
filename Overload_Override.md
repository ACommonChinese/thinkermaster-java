# Overload_Override  

### overload

overload是方法重载,发生在同一类中, 由参数个数不同或参数类型不同而区分;  

方法重载overload与下列因素相关:   
1. 方法同名
2. 参数个数不同
3. 参数类型不同
4. 参数的多类型顺序不同  

```java
public static int sum(int a, int b) {
    return a + b;
}
public static double sum(double a, double b) {
    return a + b;
}
public static double sum(int a, double b) {
    return a + b;
}
public static double sum(double a, int b) {
    return a + b;
}
```

### override

override是方法重写: 
1. 发生在父类与子类之间
2. 方法名，参数列表，返回类型（除过子类中方法的返回类型是父类中返回类型的子类）必须相同
3. 访问修饰符的限制一定要大于被重写方法的访问修饰符（public>protected>default>private)
4. 重写方法一定不能抛出新的检查异常或者比被重写方法申明更加宽泛的检查型异常

### override

