package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findRouteServlet")
public class FindRouteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int rid = Integer.parseInt(request.getParameter("rid"));
        UserService userService = new UserServiceImp();
        Route one = userService.findOne(rid);
        if (one != null){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(),one);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
