package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class respondMessage
 */
@WebServlet("/respondMessage")
public class respondMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public respondMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		String res = request.getParameter("response");
		String id = request.getParameter("qid");
		try {
			PreparedStatement stmt = con.prepareStatement("UPDATE Messages SET response = ? WHERE qid = ?");
			stmt.setString(1, res);
			stmt.setString(2, id);
			System.out.println(stmt);
			stmt.executeUpdate();
			
			response.sendRedirect(request.getContextPath() + "/browseCusRep.jsp");
			ap.closeConnection(con);
			
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "Unsuccessful request. Please try again later.");
			request.getRequestDispatcher("messages.jsp").forward(request, response);
			
		}
	}

}
