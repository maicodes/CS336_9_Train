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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Auth
 */
@WebServlet("/Auth")
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ResultSet userInfo;
	private static Customer loginUser = new Customer();
    
	private String insertUser = "INSERT INTO User (userName, password, role)"
								+ "VALUES (?,?,?)";
	private String insertCustomer = "INSERT INTO Customers(firstName, lastName, userName, email, telephone, address, city, state, zipcode, DOB)" 
							+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
	private String select = "SELECT * FROM User WHERE userName = ?";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Auth() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletrequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String page = "/index.jsp";
		if (request.getParameter("action") != null)
		{
			if (request.getParameter("action").equals("logout"))
			{
				logout(request);
				page = "/index.jsp";
				response.sendRedirect(request.getContextPath() + page); 
			}
		}
		
		/*
		
		Prints out all request variables for debugging purposes
		
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}*/
		
		
				
		/* // Previous code
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
		*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletrequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
		ApplicationDB ap = new ApplicationDB();
		
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		if(request.getParameter("type").equals("register")) {
			Customer newUser = new Customer();
			String userName = request.getParameter("uname");
			String password = request.getParameter("upassword");
			
			newUser.setFirstName(request.getParameter("ufirstName"));
			newUser.setLastName(request.getParameter("ulastName"));
			newUser.setUserName(userName);
			newUser.setEmail(request.getParameter("uemail"));
			newUser.setTelephone(request.getParameter("uphone"));
			newUser.setAddress(request.getParameter("uaddress"));
			newUser.setCity(request.getParameter("ucity"));
			newUser.setState(request.getParameter("ustate"));
			newUser.setZipcode(request.getParameter("uzip"));
			newUser.setDOB(request.getParameter("DOB"));
			
			
			try {
				
				// Create a new user 
				PreparedStatement insertUsertatement = con.prepareStatement(insertUser);
				insertUsertatement.setString(1, userName);
				insertUsertatement.setString(2, password);
				insertUsertatement.setString(3, "Customer");
				insertUsertatement.executeUpdate();
				
				// Create a new customer with the user name
				PreparedStatement ps = con.prepareStatement(insertCustomer);
				ps.setString(1, newUser.getFirstName());
				ps.setString(2, newUser.getLastName());
				ps.setString(3, newUser.getUserName());
				ps.setString(4, newUser.getEmail());
				ps.setString(5, newUser.getTelephone());
				ps.setString(6, newUser.getAddress());
				ps.setString(7, newUser.getCity());
				ps.setString(8, newUser.getState());
				ps.setString(9, newUser.getZipcode());
				ps.setString(10, newUser.getDOB());
				ps.executeUpdate();
				
				res.sendRedirect(request.getContextPath() + "/index.jsp?creation=success");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String page = "/index.jsp";
			if (request.getParameter("action") != null)
			{
				if (request.getParameter("action").equals("logout"))
				{
					logout(request);
					page = "/index.jsp";
					res.sendRedirect(request.getContextPath() + page); 
				}
			}
			else
			{
					//Get input from user from auth.jsp login
					String userName = request.getParameter("userName");
					String password = request.getParameter("password");
					String userValidate = authenticateUser (userName, password);
					
					if( userValidate.contentEquals("Manager")) {
						
						// Create a new session
						HttpSession session = request.getSession();
						session.setAttribute("login", "T");
						session.setAttribute("Manager", userName);
						request.setAttribute("userName", userName);
						request.getRequestDispatcher("/admin.jsp").forward(request, res);
						
					} else if (userValidate.contentEquals("CusRep")) {
						
						HttpSession session = request.getSession();
						session.setAttribute("login", "T");
						session.setAttribute("CusRep", userName);
						request.setAttribute("userName", userName);
						request.getRequestDispatcher("/cusRep.jsp").forward(request, res);
						
					} else if (userValidate.contentEquals("Customer")) {
						
						HttpSession session = request.getSession();
						session.setAttribute("login", "T");
						session.setAttribute("Customer", userName);
						request.setAttribute("userName", userName);
						request.getRequestDispatcher("/").forward(request, res);
						
					} else {
						System.out.println(userValidate);
						page = "/auth.jsp?try=fail";
						res.sendRedirect(request.getContextPath() + page); 
					}
			}
			
		}
		ap.closeConnection(con);
	}
	
	public static Customer getLoginUser() {
		return loginUser;
	}
	
	public static boolean isLoggedIn(HttpServletRequest request) {
		//My condition is to just check if firstname AND lastname are set
		//We can change to more secure check later
		// if (loginUser.getFirstName() != null && loginUser.getLastName() != null)
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("login") != null)
			return true;
		else
			return false;
	}
	
	public static void logout(HttpServletRequest request)
	{
		loginUser = new Customer();
		HttpSession session = request.getSession(false);
		
		if(session != null) 
			session.invalidate();
	}
	
	private String authenticateUser(String userName, String password) throws ServletException, IOException {
		try {
			ApplicationDB ap = new ApplicationDB();	
			Connection con = ap.getConnection();	
			PreparedStatement ps = con.prepareStatement(select);
			ps.setString(1, userName);
			
			//Run the query against the database.
			userInfo = ps.executeQuery();
			
			// check whether the database return a user or not
			if(userInfo.next()) {
				// the database return a user with the same username
				String uPassword = userInfo.getString("password");
				String role = userInfo.getString("role");
				
				if (uPassword.contentEquals(password)) {
						// Set loginUser
						
						String SQL = "";
						
						if (role.equals("Customer")) {
							SQL = "SELECT * FROM Customers WHERE userName = ?";
						} else {
							SQL = "SELECT * FROM Employees WHERE userName = ?";
						}
						
						PreparedStatement s = con.prepareStatement(SQL);
						s.setString(1, userName);
						ResultSet rs = s.executeQuery();
						
						if(rs.next()) {
							loginUser.setUserName(userName);
							loginUser.setFirstName(rs.getString("firstName")); 
							loginUser.setLastName(rs.getString("lastName"));
						}
					
					
					return role;
				}
			} 
			
			ap.closeConnection(con); 
			return "Incorrect password.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Cannot find user name. Login Failed.";
		}
	}
	
}