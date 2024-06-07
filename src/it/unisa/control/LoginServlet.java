package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.*;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao userDao = new UserDao();
        String username = request.getParameter("un");
        String password = request.getParameter("pw");

        try {
            // Recupera l'utente dal database utilizzando il nome utente
            UserBean user = userDao.doRetrieveByUsername(username);

            if (user != null) {
                // Verifica se la password immessa corrisponde all'hash della password nel database
                if (checkPassword(password, user.getPassword())) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("currentSessionUser", user);

                    String checkout = request.getParameter("checkout");
                    if (checkout != null)
                        response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                    else
                        response.sendRedirect(request.getContextPath() + "/Home.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error"); // Pagina di errore
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error"); // Pagina di errore
            }
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Funzione per verificare se la password immessa corrisponde all'hash della password nel database
    private boolean checkPassword(String inputPassword, String hashedPassword) {
        // Qui dovresti implementare la logica per confrontare l'hash della password immessa
        // con l'hash della password nel database. 
        // Ad esempio, puoi utilizzare lo stesso algoritmo di hashing utilizzato per creare l'hash
        // e confrontare i risultati.
        // Nell'esempio precedente, abbiamo utilizzato SHA-256, quindi dovresti utilizzare lo stesso algoritmo qui.
        // Se i due hash corrispondono, restituisci true, altrimenti restituisci false.
        return hashPassword(inputPassword).equals(hashedPassword);
    }

    // Funzione per generare l'hash SHA-256 della password
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
