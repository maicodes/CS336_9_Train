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
	private String insert = "INSERT INTO Customers(firstName, lastName, userName, password, email, telephone, address, city, state, zipcode, DOB)" 
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private String delete = "DELETE FROM Customers WHERE userName = ?";
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
				PreparedStatement ps = con.prepareStatement(insert);
				ps.setString(1, req.getParameter("fname"));
				ps.setString(2, req.getParameter("lname"));
				ps.setString(3, req.getParameter("uname"));
				ps.setString(4, req.getParameter("pw"));
				ps.setString(5, req.getParameter("email"));
				ps.setString(6, req.getParameter("phone"));
				ps.setString(7, req.getParameter("address"));
				ps.setString(8, req.getParameter("city"));
				ps.setString(9, req.getParameter("state"));
				ps.setString(10, req.getParameter("zip"));
				ps.setString(11, req.getParameter("dob"));

				ps.executeUpdate();
				res.sendRedirect(req.getContextPath() + "/admin.jsp#adminCus");
				ap.closeConnection(con);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		} else if (req.getParameter("type").contentEquals("edit")) {
			String userName = req.getParameter("uname");
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
			
			String editStatement = "UPDATE Customers";
			boolean isFirst = true;
			
			if (!fname.isEmpty()) {
				editStatement += " SET firstName = \"" + fname +"\"";
			}
			
			if (!lname.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET lastName = \"" + lname + "\"" : editStatement + ", lastName = \"" + lname + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (pass != null) {
				editStatement = isFirst ? editStatement + " SET password = \"" + pass + "\"" : editStatement + ", password = \"" + pass + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!email.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET email = \"" + email + "\"" : editStatement + ", email = \"" + email + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!dob.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET dob = \"" + dob + "\"" : editStatement + ", dob = \"" + dob + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!address.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET address = \"" + address + "\"" : editStatement + ", address = \"" + address + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!city.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET city = \"" + city + "\"" : editStatement + ", city = \"" + city + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!state.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET state = \"" + state + "\"" : editStatement + ", state = \"" + state + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!zip.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET zipcode = \"" + zip + "\"" : editStatement + ", zipcode = \"" + zip + "\"";
				isFirst = isFirst ? false : false;
			}
			
			if (!phone.isEmpty()) {
				editStatement = isFirst ? editStatement + " SET telephone = \"" + phone + "\"" : editStatement + ", telephone = \"" + phone + "\"";
				isFirst = isFirst ? false : false;
			} 
			
			editStatement += " WHERE userName = \"" + userName + "\"";
			
			System.out.print(editStatement);
			
			try {
				Statement sm = con.createStatement();
				sm.executeUpdate(editStatement);
				res.sendRedirect(req.getContextPath() + "/admin.jsp#adminCus");
				ap.closeConnection(con);
			} catch(SQLException e) {
				e.printStackTrace();
			}	
			
		} else {
			try {
				PreparedStatement ps = con.prepareStatement(delete);
				ps.setString(1, req.getParameter("uname"));

				ps.executeUpdate();
				res.sendRedirect(req.getContextPath() + "/admin.jsp#adminCus");
				ap.closeConnection(con);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
