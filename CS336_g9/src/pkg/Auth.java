package pkg;
import java.util.Enumeration;
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
		String page = "/index.jsp";
		
		/*
		
		Prints out all request variables for debugging purposes
		
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}*/
		
		if (request.getParameter("action") != null)
		{
			if (request.getParameter("action").equals("logout"))
			{
				logout();
				page = "/index.jsp";
			}
		}
		else
		{
		
		
		try {
		ApplicationDB ap = new ApplicationDB();	
		Connection con = ap.getConnection();		
		PreparedStatement ps = con.prepareStatement(select);
		
		//Get input from user from auth.jsp login
		String inputUserName = request.getParameter("userName");
		String inputPassword = request.getParameter("password");
		
		
		ps.setString(1, inputUserName);
		
		//Run the query against the database.
		userInfo = ps.executeQuery();
		
		// check whether the database return a user or not
		if(userInfo.next()) {
			// the database return a user with the same username
			String uPassword = userInfo.getString("password");
			
			if (uPassword.contentEquals(inputPassword)) {
				// login success
				loginUser.setUserName(inputUserName);
				loginUser.setFirstName(userInfo.getString("firstName")); 
				loginUser.setLastName(userInfo.getString("lastName"));
				loginUser.setPassword(inputPassword);
				System.out.println("Login Success!");
			} else
				throw new IOException("Your password is not correct!");

		} else {
			loginUser = new Customer();
			throw new IOException(inputUserName + " is not found!");
		}
	
		ap.closeConnection(con);
		
		} 
		catch (Exception x) {
			System.out.println("Login fail");
			System.out.print(x);
			page = "/auth.jsp?try=fail";
		}
		}
		response.sendRedirect(request.getContextPath() + page);
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
			
			res.sendRedirect(req.getContextPath() + "/index.jsp?creation=success");
			
			ap.closeConnection(con);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Customer getLoginUser() {
		return loginUser;
	}
	public static boolean isLoggedIn() {
		//My condition is to just check if firstname AND lastname are set
		//We can change to more secure check later
		if (loginUser.getFirstName() != null && loginUser.getLastName() != null)
			return true;
		else
			return false;
	}
	
	private void logout()
	{
		loginUser = new Customer();
	}
	
}
