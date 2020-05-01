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
 * Servlet implementation class submitMessage
 */
@WebServlet("/submitMessage")
public class submitMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public submitMessage() {
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
		
		String u = request.getParameter("username");
		String t = request.getParameter("title");
		String m = request.getParameter("mbox");
		try {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO Messages (userName, title, content) VALUES (?,?,?);");
			stmt.setString(1, u);
			stmt.setString(2, t);
			stmt.setString(3, m);
			stmt.executeUpdate();
			
			request.setAttribute("success", "Thank you for your message. One of our representatives will respond as soon as possible.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			//response.sendRedirect(request.getContextPath() + "/");
			ap.closeConnection(con);
			
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "Unsuccessful request. Check whether username is valid.");
			request.getRequestDispatcher("messages.jsp").forward(request, response);
			
		}
	}

}
