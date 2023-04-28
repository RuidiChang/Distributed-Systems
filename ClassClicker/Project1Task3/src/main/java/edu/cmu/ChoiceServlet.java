package edu.cmu;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "ChoiceServlet", urlPatterns = {"/submit", "/getResults"})
public class ChoiceServlet extends HttpServlet {
    public static int[] counter = new int[4];

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("Choice"));
        String choice = req.getParameter("Choice");
        count(choice);
        req.setAttribute("PreviousChoice", choice);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void count(String choice) {
        if (choice.equals("A")) {
            counter[0] += 1;
        } else if (choice.equals("B")) {
            counter[1] += 1;
        } else if (choice.equals("C")) {
            counter[2] += 1;
        } else if (choice.equals("D")) {
            counter[3] += 1;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("A", ChoiceServlet.counter[0]);
        req.setAttribute("B", ChoiceServlet.counter[1]);
        req.setAttribute("C", ChoiceServlet.counter[2]);
        req.setAttribute("D", ChoiceServlet.counter[3]);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("result.jsp");
        requestDispatcher.forward(req, resp);
        ChoiceServlet.counter = new int[4];
        System.out.println("123");
    }
}

