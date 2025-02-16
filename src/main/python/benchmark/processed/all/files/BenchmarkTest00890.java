import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-01/BenchmarkTest00890")
public class BenchmarkTest00890 extends HttpServlet {

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

        org.owasp.benchmark.helpers.SeparateClassRequest scr =
                new org.owasp.benchmark.helpers.SeparateClassRequest(request);
        String param = scr.getTheValue("BenchmarkTest00890");

        String bar = "safe!";
        java.util.HashMap<String, Object> map61765 = new java.util.HashMap<String, Object>();
        map61765.put("keyA-61765", "a-Value"); // put some stuff in the collection
        map61765.put("keyB-61765", param); // put it in a collection
        map61765.put("keyC", "another-Value"); // put some stuff in the collection
        bar = (String) map61765.get("keyB-61765"); // get it back out

        response.setHeader("X-XSS-Protection", "0");
        response.getWriter().write(bar.toCharArray());
    }
}
