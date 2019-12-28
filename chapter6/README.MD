## Spring Boot中Web应用的统一异常处理

### 默认error映射
Spring Boot提供了一个默认的异常映射：/error，当应用中抛出异常之后，会转到该请求中处理，并且该请求有一个全局的错误页面用来展示异常内容。

### 自定义统一异常处理
通过使用@ControllerAdvice定义统一的异常处理类，而不是在每个Controller中逐个定义。@ExceptionHandler用来定义函数针对的异常类型，最后将Exception对象和请求URL映射到error.html中
```
@ControllerAdvice
class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}
```

实现error.html页面展示：在templates目录下创建error.html，将请求的URL和Exception对象的message输出。
```
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title>统一异常处理</title>
</head>
<body>
    <h1>Error Handler</h1>
    <div th:text="${url}"></div>
    <div th:text="${exception.message}"></div>
</body>
</html>

```
此时，当应用再次发生异常时，将会被重定向到自定义的error.html，当然我们可以为不同的异常定义不同的错误页面


### 自定义JSON格式异常

创建自定义异常返回对象
```
public class ErrorInfo<T> {
    public static final Integer OK = 0;
    public static final Integer ERROR = -1;
    /**
     * 消息类型
     */
    private Integer code;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 请求的url
     */
    private String url;
    /**
     * 请求返回的数据
     */
    private T data;
    ...
}
```

创建一个自定义异常
```
public class MyException extends Exception {

    public MyException(String message) {
        super(message);
    }
    
}
```

为自定义异常创建对应的处理
```
@ControllerAdvice
public class GlobalExceptionHandler {
    ...
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ErrorInfo jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorInfo.ERROR);
        r.setData("Some Data");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }

}
```

抛出自定义异常
```
    @RequestMapping("/myException")
    public String myException() throws MyException {
        throw new MyException("自定义异常");
    }
```

启动应用，访问：/myException，可以得到如下返回内容：
```
{
    "code":-1,
    "message":"自定义异常",
    "url":"http://localhost:8080/myException",
    "data":"Some Data"
}
```