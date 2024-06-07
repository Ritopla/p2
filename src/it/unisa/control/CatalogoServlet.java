package it.unisa.control;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/CatalogoServlet")
public class CatalogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProdottoDao prodDao = new ProdottoDao();
        ProdottoBean bean = new ProdottoBean();
        String sort = request.getParameter("sort");
        String action = request.getParameter("action");
        String redirectedPage = request.getParameter("page");

        try {
            if (action != null) {
                if (action.equalsIgnoreCase("add")) {
                    bean.setNome(escapeHtml(request.getParameter("nome")));
                    bean.setDescrizione(escapeHtml(request.getParameter("descrizione")));
                    bean.setIva(escapeHtml(request.getParameter("iva")));
                    bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    bean.setQuantity(Integer.parseInt(request.getParameter("quantità")));
                    bean.setPiattaforma(escapeHtml(request.getParameter("piattaforma")));
                    bean.setGenere(escapeHtml(request.getParameter("genere")));
                    bean.setImmagine(escapeHtml(request.getParameter("img")));
                    bean.setDataUscita(escapeHtml(request.getParameter("dataUscita")));
                    bean.setDescrizioneDettagliata(escapeHtml(request.getParameter("descDett")));
                    bean.setInVendita(true);
                    prodDao.doSave(bean);
                } else if (action.equalsIgnoreCase("modifica")) {
                    bean.setIdProdotto(Integer.parseInt(request.getParameter("id")));
                    bean.setNome(escapeHtml(request.getParameter("nome")));
                    bean.setDescrizione(escapeHtml(request.getParameter("descrizione")));
                    bean.setIva(escapeHtml(request.getParameter("iva")));
                    bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    bean.setQuantity(Integer.parseInt(request.getParameter("quantità")));
                    bean.setPiattaforma(escapeHtml(request.getParameter("piattaforma")));
                    bean.setGenere(escapeHtml(request.getParameter("genere")));
                    bean.setImmagine(escapeHtml(request.getParameter("img")));
                    bean.setDataUscita(escapeHtml(request.getParameter("dataUscita")));
                    bean.setDescrizioneDettagliata(escapeHtml(request.getParameter("descDett")));
                    bean.setInVendita(true);
                    prodDao.doUpdate(bean);
                }

                request.getSession().removeAttribute("categorie");
            }
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }

        try {
            request.getSession().removeAttribute("products");
            request.getSession().setAttribute("products", prodDao.doRetrieveAll(sort));
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/" + redirectedPage);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Funzione di escape HTML per prevenire XSS
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '&':
                    escaped.append("&amp;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&#x27;");
                    break;
                case '/':
                    escaped.append("&#x2F;");
                    break;
                default:
                    escaped.append(c);
                    break;
            }
        }
        return escaped.toString();
    }
}

// Classe di esempio per il DAO (ProdottoDao) con PreparedStatement
class ProdottoDao {
    public static final String TABLE_NAME = "prodotti";

    // Supponiamo che ds sia il DataSource configurato correttamente
    private DataSource ds;

    public ProdottoDao() {
        // Inizializza il DataSource (ad es., tramite JNDI)
    }

    public void doSave(ProdottoBean bean) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (nome, descrizione, iva, prezzo, quantity, piattaforma, genere, immagine, dataUscita, descrizioneDettagliata, inVendita) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, bean.getNome());
            preparedStatement.setString(2, bean.getDescrizione());
            preparedStatement.setString(3, bean.getIva());
            preparedStatement.setDouble(4, bean.getPrezzo());
            preparedStatement.setInt(5, bean.getQuantity());
            preparedStatement.setString(6, bean.getPiattaforma());
            preparedStatement.setString(7, bean.getGenere());
            preparedStatement.setString(8, bean.getImmagine());
            preparedStatement.setString(9, bean.getDataUscita());
            preparedStatement.setString(10, bean.getDescrizioneDettagliata());
            preparedStatement.setBoolean(11, bean.isInVendita());
            preparedStatement.executeUpdate();
        }
    }

    public void doUpdate(ProdottoBean bean) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET nome = ?, descrizione = ?, iva = ?, prezzo = ?, quantity = ?, piattaforma = ?, genere = ?, immagine = ?, dataUscita = ?, descrizioneDettagliata = ?, inVendita = ? WHERE idProdotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, bean.getNome());
            preparedStatement.setString(2, bean.getDescrizione());
            preparedStatement.setString(3, bean.getIva());
            preparedStatement.setDouble(4, bean.getPrezzo());
            preparedStatement.setInt(5, bean.getQuantity());
            preparedStatement.setString(6, bean.getPiattaforma());
            preparedStatement.setString(7, bean.getGenere());
            preparedStatement.setString(8, bean.getImmagine());
            preparedStatement.setString(9, bean.getDataUscita());
            preparedStatement.setString(10, bean.getDescrizioneDettagliata());
            preparedStatement.setBoolean(11, bean.isInVendita());
            preparedStatement.setInt(12, bean.getIdProdotto());
            preparedStatement.executeUpdate();
        }
    }

    public List<ProdottoBean> doRetrieveAll(String sort) throws SQLException {
        // Supponiamo che sia già stata implementata la logica di doRetrieveAll
        // e che usi preparedStatement correttamente per evitare SQL Injection
        // La logica di sanificazione di "sort" sarà simile a quella mostrata prima
        return null; // Placeholder
    }
}

// Classe di esempio per il bean (ProdottoBean)
class ProdottoBean {
    private int idProdotto;
    private String nome;
    private String descrizione;
    private String iva;
    private double prezzo;
    private int quantity;
    private String piattaforma;
    private String genere;
    private String immagine;
    private String dataUscita;
    private String descrizioneDettagliata;
    private boolean inVendita;

    // Getter e Setter
    // ...
}
