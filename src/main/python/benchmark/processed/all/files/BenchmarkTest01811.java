import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/sqli-03/BenchmarkTest01811")
public class BenchmarkTest01811 extends HttpServlet {

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
        String param = scr.getTheValue("BenchmarkTest01811");

        String bar = new Test().doSomething(request, param);

        String sql =
                "SELECT TOP 1 userid from USERS where USERNAME='foo' and PASSWORD='" + bar + "'";
        try {
            java.util.Map<String, Object> results =
                    org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate.queryForMap(sql);
            response.getWriter().println("Your results are: ");

            //		System.out.println("Your results are");
            response.getWriter()
                    .println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(results.toString()));
            //		System.out.println(results.toString());
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            response.getWriter()
                    .println(
                            "No results returned for query: "
                                    + org.owasp.esapi.ESAPI.encoder().encodeForHTML(sql));
        } catch (org.springframework.dao.DataAccessException e) {
            if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
                response.getWriter().println("Error processing request.");
            } else throw new ServletException(e);
        }
    } // end doPost

    private class Test {

        public String doSomething(HttpServletRequest request, String param)
                throws ServletException, IOException {

            String bar = "safe!";
            java.util.HashMap<String, Object> map31047 = new java.util.HashMap<String, Object>();
            map31047.put("keyA-31047", "a_Value"); // put some stuff in the collection
            map31047.put("keyB-31047", param); // put it in a collection
            map31047.put("keyC", "another_Value"); // put some stuff in the collection
            bar = (String) map31047.get("keyB-31047"); // get it back out
            bar = (String) map31047.get("keyA-31047"); // get safe value back out

            return bar;
        }
    } // end innerclass Test
} // end DataflowThruInnerClass
