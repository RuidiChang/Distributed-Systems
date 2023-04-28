package edu.cmu;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;

import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "abc",urlPatterns = {"/submit"} )
public class ComputeHashes extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("encryptType"));
        System.out.println(req.getParameter("textInput"));

        String encryptType=req.getParameter("encryptType");
        String word=req.getParameter("textInput");
        req.setAttribute("hex", ToHex(computeHash(word, encryptType)));
        req.setAttribute("base64", ToBase64(computeHash(word, encryptType)));

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("result.jsp");
        requestDispatcher.forward(req,resp);


    }

    private byte[] computeHash(String text, String method) {
        byte[] d = null;
        try{
            MessageDigest md = MessageDigest.getInstance(method);
            md.update(text.getBytes());
            d = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ComputeHashes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(d);
        return d;
    }
    private String ToHex(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    private String ToBase64(byte[] data) {
        return DatatypeConverter.printBase64Binary(data);
    }
}

