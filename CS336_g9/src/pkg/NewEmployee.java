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
 * Servlet implementation class newEmployee
 */
@WebServlet("/NewEmployee")
public class NewEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String insert = "INSERT INTO Employees" 
			+ "VALUES (?,?,?,?,?)";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewEmployee() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("here");
		response.sendRedirect("/index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApplicationDB ap = new ApplicationDB();
		
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
	
		String ssn = req.getParameter("emp-ssn");
		String fname = req.getParameter("emp-fname");
		String lname = req.getParameter("emp-lname");
		String uname = req.getParameter("emp-uname");
		String pass = req.getParameter("emp-pass");

		try {
			PreparedStatement ps = con.prepareStatement(insert);
			ps.setString(1, ssn);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, uname);
			ps.setString(5, pass);

			ps.executeUpdate();
			
			ap.closeConnection(con);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		res.sendRedirect("/admin.jsp");
	}

}
