import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/xss-05/BenchmarkTest02712")
public class BenchmarkTest02712 extends HttpServlet {

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
        String param = scr.getTheValue("BenchmarkTest02712");

        String bar = doSomething(request, param);

        response.setHeader("X-XSS-Protection", "0");
        response.getWriter().write("Parameter value: " + bar);
    } // end doPost

    private static String doSomething(HttpServletRequest request, String param)
            throws ServletException, IOException {

        String bar = "safe!";
        java.util.HashMap<String, Object> map52216 = new java.util.HashMap<String, Object>();
        map52216.put("keyA-52216", "a_Value"); // put some stuff in the collection
        map52216.put("keyB-52216", param); // put it in a collection
        map52216.put("keyC", "another_Value"); // put some stuff in the collection
        bar = (String) map52216.get("keyB-52216"); // get it back out
        bar = (String) map52216.get("keyA-52216"); // get safe value back out

        return bar;
    }
}
