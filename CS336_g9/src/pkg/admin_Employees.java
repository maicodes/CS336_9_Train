package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class admin_Employees
 */
@WebServlet("/admin_Employees")
public class admin_Employees extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String insertStatement = "INSERT INTO Employees (ssn, FirstName, LastName, userName, password)" 
			+ "VALUES (?,?,?,?,?)";
	private String deleteStatement = "DELETE FROM Employees WHERE ssn = ?";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_Employees() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		
		if (req.getParameter("add-emp-ssn") != null) {
			String ssn = req.getParameter("add-emp-ssn");
			String fname = req.getParameter("emp-fname");
			String lname = req.getParameter("emp-lname");
			String uname = req.getParameter("emp-uname");
			String pass = req.getParameter("emp-pass");
			
			try {
				PreparedStatement ps = con.prepareStatement(insertStatement);
				ps.setString(1, ssn);
				ps.setString(2, fname);
				ps.setString(3, lname);
				ps.setString(4, uname);
				ps.setString(5, pass);

				ps.executeUpdate();
				res.sendRedirect(req.getContextPath() + "/admin.jsp");
				ap.closeConnection(con);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} else if (req.getParameter("edit-emp-ssn") != null) {
			String ssn = req.getParameter("edit-emp-ssn");
			String fname = req.getParameter("emp-fname");
			String lname = req.getParameter("emp-lname");
			String uname = req.getParameter("emp-uname");
			String pass = req.getParameter("emp-pass");
			
			String editStatement = "UPDATE Employees";
			boolean isFirst = true;
			
			if (fname != null) {
				editStatement += " SET FirstName = \"" + fname +"\"";
				isFirst = false;
			} else if (lname != null) {
				if(isFirst) {
					editStatement += " SET LastName = " + lname;
					isFirst = false;
				} else 
					editStatement += ", LastName = " + lname;
			} else if (uname != null) {
				if(isFirst) {
					editStatement += " SET userName = " + uname;
					isFirst = false;
				} else 
					editStatement += ", userName = " + uname;
			}  else if (pass != null) {
				if(isFirst) {
					editStatement += " SET password = " + pass;
					isFirst = false;
				} else 
					editStatement += ", password = " + pass;
			} 
			
			editStatement += " WHERE ssn = \"" + ssn + "\"";
			
			System.out.print(editStatement);
			
			try {
				Statement sm = con.createStatement();
				sm.executeUpdate(editStatement);
				res.sendRedirect(req.getContextPath() + "/admin.jsp");
				ap.closeConnection(con);
			} catch(SQLException e) {
				e.printStackTrace();
			}	
			
		} else {
			String ssn = req.getParameter("del-emp-ssn");

			try {
				PreparedStatement ps = con.prepareStatement(deleteStatement);
				ps.setString(1, ssn);

				ps.executeUpdate();
				res.sendRedirect(req.getContextPath() + "/admin.jsp");
				ap.closeConnection(con);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

}
