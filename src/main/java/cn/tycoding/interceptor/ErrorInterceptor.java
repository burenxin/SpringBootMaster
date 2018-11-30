package cn.tycoding.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        System.out.println(">>>MyInterceptor1>>>在请求处理之前调用(controller调用之前)");
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,
                           ModelAndView modelAndView) throws Exception{
        System.out.println(">>>MyInterceptor>>>请求处理之后调用,视图被渲染之前(Controller调用之后)");
        if(response.getStatus()==500){
            modelAndView.setViewName("/errorpage/500");
        }else if(response.getStatus()==404){
            modelAndView.setViewName("/errorpage/404");
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception e) throws  Exception{
        System.out.println(">>>MyInterceptor>>>在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后执行");

    }
}
