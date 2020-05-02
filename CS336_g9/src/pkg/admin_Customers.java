package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class admin_Customers
 */
@WebServlet("/admin_Customers")
public class admin_Customers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String insertUser = "INSERT INTO User (userName, password, role) VALUES (?,?,?);";
	private String insertCustomer = "INSERT INTO Customers(firstName, lastName, userName, email, telephone, address, city, state, zipcode, DOB)" 
			+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
	private String delete = "DELETE FROM User WHERE userName = ?";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_Customers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		if (req.getParameter("type").contentEquals("add")) {
			try {
				String fname = req.getParameter("fname");
				String lname = req.getParameter("lname");
				String uname = req.getParameter("uname");
				String pw = req.getParameter("pw");
				String email =  req.getParameter("email");
				String phone =  req.getParameter("phone");
				String address = req.getParameter("address");
				String city = req.getParameter("city");
				String state = req.getParameter("state");
				String zip = req.getParameter("zip");
				String dob = req.getParameter("dob");

				// Create a new user 
				PreparedStatement insertUsertatement = con.prepareStatement(insertUser);
				insertUsertatement.setString(1, uname);
				insertUsertatement.setString(2, pw);
				insertUsertatement.setString(3, "Customer");
				insertUsertatement.executeUpdate();
				
				// Create a new customer with the user name
				PreparedStatement ps = con.prepareStatement(insertCustomer);
				ps.setString(1, fname);
				ps.setString(2, lname);
				ps.setString(3, uname);
				ps.setString(4, email);
				ps.setString(5, phone);
				ps.setString(6, address);
				ps.setString(7, city);
				ps.setString(8, state);
				ps.setString(9, zip);
				ps.setString(10, dob);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} else if (req.getParameter("type").contentEquals("edit")) {
			// Edit customer information
			String uname = req.getParameter("uname");
			String newUname = req.getParameter("new-uname");
			String fname = req.getParameter("fname");
			String lname = req.getParameter("lname");
			String pass = req.getParameter("pw");
			String email = req.getParameter("email");
			String dob = req.getParameter("dob");
			String address = req.getParameter("address");
			String city = req.getParameter("city");
			String state = req.getParameter("state");
			String zip = req.getParameter("zip");
			String phone = req.getParameter("phone");
			
			if (Common.StringIsNotEmpty(newUname) || Common.StringIsNotEmpty(pass)) {
				// update User table
				ArrayList<String> columns = new ArrayList<String>();
				ArrayList<String> values = new ArrayList<String>();
						
						if(Common.StringIsNotEmpty(newUname)) {
							columns.add("userName");
							values.add(newUname);
						}
						
						if(Common.StringIsNotEmpty(pass)) {
							columns.add("password");
							values.add(pass);
						}
						
						String editUserStatement = Common.createEditStatement("User", columns, values, " WHERE userName = " + "\"" + uname + "\"");
						System.out.println(editUserStatement);
						
						try {
							Statement sm = con.createStatement();
							sm.executeUpdate(editUserStatement);
						} catch(Exception e) {
							e.printStackTrace();
						}
			}
			
			ArrayList<String> columns = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			
			if (Common.StringIsNotEmpty(fname)) {
				columns.add("firstName");
				values.add(fname);
			}
			
			if (Common.StringIsNotEmpty(lname)) {
				columns.add("lastName");
				values.add(lname);
			}
			
			
			if (Common.StringIsNotEmpty(email)) {
				columns.add("email");
				values.add(email);
			}
			
			if (Common.StringIsNotEmpty(dob)) {
				columns.add("DOB");
				values.add(dob);
			}
			
			if (Common.StringIsNotEmpty(address)) {
				columns.add("address");
				values.add(address);
			}
			
			if (Common.StringIsNotEmpty(city)) {
				columns.add("city");
				values.add(city);
			}
			
			if (Common.StringIsNotEmpty(state)) {
				columns.add("state");
				values.add(state);
			}
			
			if (Common.StringIsNotEmpty(zip)) {
				columns.add("zipcode");
				values.add(zip);
			}
			
			if (Common.StringIsNotEmpty(phone)) {
				columns.add("telephone");
				values.add(phone);
			} 
			
			if (columns.size() != 0) {
				uname = Common.StringIsNotEmpty(newUname) ? newUname : uname;
				String editStatement = Common.createEditStatement("Customers", columns, values, " WHERE userName = " + "\"" + uname + "\"");
				
				System.out.print(editStatement);
				
				try {
					Statement sm = con.createStatement();
					sm.executeUpdate(editStatement);
					ap.closeConnection(con);
				} catch(SQLException e) {
					e.printStackTrace();
				}	
			}
			
			
		} else {
			try {
				PreparedStatement ps = con.prepareStatement(delete);
				ps.setString(1, req.getParameter("uname"));
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		res.sendRedirect(req.getContextPath() + "/admin.jsp#adminCus");
		ap.closeConnection(con);
	}
	
//	private static String createEditStatement(String tableName, ArrayList<String> columns, ArrayList<String> values, String condition) {
//		String editStatement = "UPDATE " + tableName + " SET " + columns.get(0) + " = \"" + values.get(0) + "\"";
//		byte numCols = (byte) columns.size();
//		
//		for(byte i = 1; i < numCols; i ++) {
//			editStatement += ", " + columns.get(i) + "= \"" + values.get(i) + "\"";
//		}
//		
//		editStatement += condition;
//		
//		return editStatement;
//	}

}
