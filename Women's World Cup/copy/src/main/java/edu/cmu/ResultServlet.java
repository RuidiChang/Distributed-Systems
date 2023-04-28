package edu.cmu;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ResultServlet",urlPatterns = {"/getResults"} )
public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("Nickname", StateServlet.nickname);
        req.setAttribute("Population", StateServlet.population);
        req.setAttribute("Flower", StateServlet.flower);
        req.setAttribute("Capital", StateServlet.capital);
        req.setAttribute("Song", StateServlet.song);
        req.setAttribute("Flag", StateServlet.flag);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("result.jsp");
        requestDispatcher.forward(req,resp);
    }
}