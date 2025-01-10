import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-01/BenchmarkTest00887")
public class BenchmarkTest00887 extends HttpServlet {

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
        String param = scr.getTheValue("BenchmarkTest00887");

        String bar = "safe!";
        java.util.HashMap<String, Object> map39726 = new java.util.HashMap<String, Object>();
        map39726.put("keyA-39726", "a-Value"); // put some stuff in the collection
        map39726.put("keyB-39726", param); // put it in a collection
        map39726.put("keyC", "another-Value"); // put some stuff in the collection
        bar = (String) map39726.get("keyB-39726"); // get it back out

        response.setHeader("X-XSS-Protection", "0");
        response.getWriter().println(bar.toCharArray());
    }
}
