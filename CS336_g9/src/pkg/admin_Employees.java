package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
	private String addUser = "INSERT INTO User (userName, password, role)"
			+ "VALUES (?, ?, ?)";
	private String addEmployee = "INSERT INTO Employees (ssn, FirstName, LastName, userName)" 
			+ "VALUES (?,?,?,?)";
	private String deleteStatement = "DELETE FROM User WHERE userName = ?";
       
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
		//  Add a new Employee
			String ssn = req.getParameter("add-emp-ssn");
			String fname = req.getParameter("emp-fname");
			String lname = req.getParameter("emp-lname");
			String uname = req.getParameter("emp-uname");
			String pass = req.getParameter("emp-pass");
			String role = req.getParameter("emp-role");
			
			addNewEmployee(uname, pass, role, fname, ssn, lname, con);
			
			res.sendRedirect(req.getContextPath() + "/admin.jsp");
			ap.closeConnection(con);

		} else if (req.getParameter("edit-emp-ssn") != null) {
			
		// Edit an employee information
			String ssn = req.getParameter("edit-emp-ssn");
			String newSSN = req.getParameter("emp-new-ssn");
			String fname = req.getParameter("emp-fname");
			String lname = req.getParameter("emp-lname");
			String uname = req.getParameter("emp-uname");
			String pass = req.getParameter("emp-pass");
			
			if ((uname != null && !uname.isEmpty())|| (pass != null && !pass.isEmpty())) {
				// update User table
				String oldUName = "";
				if(uname != null && !uname.isEmpty()) {
					// get old user name
					Statement statement;
					try {
						statement = con.createStatement();
						String query = "SELECT userName FROM Employees WHERE ssn = " + ssn;
						ResultSet rs = statement.executeQuery(query);
						
						if(rs.next())
							oldUName = rs.getString("userName");
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					oldUName = uname;
							
				ArrayList<String> columns = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
						
						if(uname != null && !uname.isEmpty()) {
							columns.add("userName");
							values.add(uname);
						}
						
						if(pass != null && !pass.isEmpty()) {
							columns.add("password");
							values.add(pass);
						}
						
						String editUserStatement = Common.createEditStatement("User", columns, values, " WHERE userName = " + "\"" + oldUName + "\"");
						System.out.println(editUserStatement);
						
						try {
							Statement sm = con.createStatement();
							sm.executeUpdate(editUserStatement);
						} catch(Exception e) {
							e.printStackTrace();
						}

				}
			
				if((newSSN != null && !newSSN.isEmpty()) || (fname != null && !fname.isEmpty()) || (lname != null && !lname.isEmpty())) {
					// Update Employees table
					
					ArrayList<String> columns = new ArrayList<String>();
					ArrayList<String> values = new ArrayList<String>();
					
					if(newSSN != null && !newSSN.isEmpty()) {
						columns.add("ssn");
						values.add(newSSN);
					}
					
					if(fname != null && !fname.isEmpty()) {
						columns.add("FirstName");
						values.add(fname);
					}
					
					if(lname != null && !lname.isEmpty()) {
						columns.add("LastName");
						values.add(lname);
					}
					
					String editEmployeeStatement = Common.createEditStatement("Employees", columns, values, " WHERE ssn = " + "\"" + ssn + "\"");
					System.out.println(editEmployeeStatement);
					
					try {
						Statement sm = con.createStatement();
						sm.executeUpdate(editEmployeeStatement);
						res.sendRedirect(req.getContextPath() + "/admin.jsp");
						ap.closeConnection(con);
					} catch(SQLException e) {
						e.printStackTrace();
					}	
				}
		} 
		else {
			String ssn = req.getParameter("del-emp-ssn"), uname;
			Statement statement;
			try {
				statement = con.createStatement();
				String query = "SELECT userName FROM Employees WHERE ssn = " + ssn;
				ResultSet rs = statement.executeQuery(query);
				
				if(rs.next())
					uname = rs.getString("userName");
				else {
					System.out.println("user name not found!");
					return;
				}

				PreparedStatement ps = con.prepareStatement(deleteStatement);
				ps.setString(1, uname);

				ps.executeUpdate();
				res.sendRedirect(req.getContextPath() + "/admin.jsp");
				ap.closeConnection(con);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	private void addNewEmployee(String uname, String pass, String role, String fname, String ssn, String lname, Connection con) {
		try {
			// Add new user
			PreparedStatement userQuery = con.prepareStatement(addUser);
			userQuery.setString(1, uname);
			userQuery.setString(2, pass);
			userQuery.setString(3, role);
			userQuery.executeUpdate();
			
			// Add new employee
			PreparedStatement ps = con.prepareStatement(addEmployee);
			ps.setString(1, ssn);
			ps.setString(2, fname);
			ps.setString(3, lname);
			ps.setString(4, uname);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

}
