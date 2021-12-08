package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/routeServlet")
public class RouteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize;
        String pageSize1 = request.getParameter("pageSize");
        if (pageSize1 == null || pageSize1.length()<=0){
            pageSize = 8;
        }else {
            pageSize = Integer.parseInt(pageSize1);
        }

        PageBean pageBean = new PageBean();
        pageBean.setCid(cid);
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        UserService userService = new UserServiceImp();
        PageBean route = userService.findRoute(pageBean);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(),route);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
