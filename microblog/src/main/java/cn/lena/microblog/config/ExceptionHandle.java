package cn.lena.microblog.config;

import cn.lena.microblog.domain.utils.Msg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

    //使用注解说明要捕获哪一个异常类,Exception是我们抛出异常使用的类
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Msg handle(Exception e){
        return Msg.fail(e.getMessage());
    }

}
