import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-01/BenchmarkTest00879")
public class BenchmarkTest00879 extends HttpServlet {

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
        String param = scr.getTheValue("BenchmarkTest00879");

        String bar = "safe!";
        java.util.HashMap<String, Object> map11407 = new java.util.HashMap<String, Object>();
        map11407.put("keyA-11407", "a_Value"); // put some stuff in the collection
        map11407.put("keyB-11407", param); // put it in a collection
        map11407.put("keyC", "another_Value"); // put some stuff in the collection
        bar = (String) map11407.get("keyB-11407"); // get it back out
        bar = (String) map11407.get("keyA-11407"); // get safe value back out

        response.setHeader("X-XSS-Protection", "0");
        Object[] obj = {"a", "b"};
        response.getWriter().format(java.util.Locale.US, bar, obj);
    }
}
