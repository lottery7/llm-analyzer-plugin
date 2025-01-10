import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-00/BenchmarkTest00388")
public class BenchmarkTest00388 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String param = request.getParameter("BenchmarkTest00388");
        if (param == null) param = "";

        StringBuilder sbxyz30382 = new StringBuilder(param);
        String bar = sbxyz30382.append("_SafeStuff").toString();

        response.setHeader("X-XSS-Protection", "0");
        response.getWriter().println(bar.toCharArray());
    }
}
