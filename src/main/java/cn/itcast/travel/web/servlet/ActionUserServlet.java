package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/actionUserServlet")
public class ActionUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        UserService service = new UserServiceImp();
        boolean flag = service.action(code);
        String msg = null;
        if (flag){
            msg = "激活成功，请<a href='login.html'>登录</a>";
        }else {
            msg = "激活失败，请稍后再试！";
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
