package pkg;

import pkg.Models.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Auth
 */
@WebServlet("/Auth")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ResultSet userInfo;
	private static Customer loginUser = new Customer();
    
	private String insert = "INSERT INTO Customers(firstName, lastName, userName, password, email, telephone, address, city, state, zipcode)" 
							+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
	private String select = "SELECT * FROM Customers WHERE userName = ?";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Auth() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
		ApplicationDB ap = new ApplicationDB();	
		Connection con = ap.getConnection();		
		PreparedStatement ps = con.prepareStatement(select);
		
		//Get the userName text input from the auth.jsp
		loginUser.setUserName( request.getParameter("userName"));
		
		ps.setString(1, loginUser.getUserName());
		
		//Run the query against the database.
		userInfo = ps.executeQuery();
		
		
		if(userInfo.next()) {
			loginUser.setFirstName(userInfo.getString("firstName")); 
			loginUser.setLastName(userInfo.getString("lastName"));
			System.out.println("Login Success!");
		} else {
			loginUser = new Customer();
			System.out.println("No Data Found!");
		}
	
		ap.closeConnection(con);
		
		} catch (Exception x) {
			System.out.println("Login fail");
			System.out.print(x);
		}
		
		response.sendRedirect(request.getContextPath() + "/index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		ApplicationDB ap = new ApplicationDB();
		
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		Customer newUser = new Customer();
		
		newUser.setFirstName(req.getParameter("ufirstName"));
		newUser.setLastName(req.getParameter("ulastName"));
		newUser.setUserName(req.getParameter("uname"));
		newUser.setPassword(req.getParameter("upassword"));
		newUser.setEmail(req.getParameter("uemail"));
		newUser.setTelephone(req.getParameter("uphone"));
		newUser.setAddress(req.getParameter("uaddress"));
		newUser.setCity(req.getParameter("ucity"));
		newUser.setState(req.getParameter("ustate"));
		newUser.setZipcode(req.getParameter("uzip"));
		
		
		
		try {
		//	Statement statement = con.createStatement();
			PreparedStatement ps = con.prepareStatement(insert);
			ps.setString(1, newUser.getFirstName());
			ps.setString(2, newUser.getLastName());
			ps.setString(3, newUser.getUserName());
			ps.setString(4, newUser.getPassword());
			ps.setString(5, newUser.getEmail());
			ps.setString(6, newUser.getTelephone());
			ps.setString(7, newUser.getAddress());
			ps.setString(8, newUser.getCity());
			ps.setString(9, newUser.getState());
			ps.setString(10, newUser.getZipcode());
			
			ps.executeUpdate();
			
			ap.closeConnection(con);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Customer getLoginUser() {
		return loginUser;
	}

}
