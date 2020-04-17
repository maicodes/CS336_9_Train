package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class admin_reservations
 */
@WebServlet("/admin_reservations")
public class admin_reservations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String transitTrainQuery = "SELECT * "
			+ "FROM Reservations "
			+ "WHERE rid IN "
			+ "(SELECT r.rid "
			+ "FROM Reservations r, Stops s, Trains t "
			+ "WHERE r.originStop_id = s.stop_id AND s.train_id = t.t_id AND t.t_id = ? AND t.transitLine_Name = ?)";
    String customerQuery = "SELECT * "
    		+ "FROM Reservations "
    		+ "WHERE rid IN "
    		+ "(SELECT b.rid "
    		+ "FROM Bookings b, Customers c "
    		+ "WHERE b.userName = c.userName AND c.firstName = ? AND c.lastName = ?)";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_reservations() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		try {
			ApplicationDB ap = new ApplicationDB();
			
			Connection con = ap.getConnection();
			
			if(con == null) {
				System.out.print("Cannot connect to database");
				return;
			}
			
			if(request.getParameter("admin-res-fname") != null) {
				// Request to get a list of reservations by customer's name.
						String firstName = request.getParameter("admin-res-fname");
						String lastName = request.getParameter("admin-res-lname");
						PreparedStatement ps = con.prepareStatement(customerQuery);
						ps.setString(1, firstName);
						ps.setString(2, lastName);
						
						ResultSet rs = ps.executeQuery();
						Common.printSQLResultTable(rs, response, "List of reservations by customer " + firstName + " " + lastName);
						
			} 
			else {
				// Request to get a list of reservations by train number and transit line
				String trainNum = request.getParameter("admin-res-train");
				String transitLine = request.getParameter("admin-res-tran");
				PreparedStatement ps = con.prepareStatement(transitTrainQuery);
				ps.setString(1, trainNum);
				ps.setString(2, transitLine);
				
				ResultSet rs = ps.executeQuery();
				
				Common.printSQLResultTable(rs, response, "List of reservations by train number " + trainNum + " and transit line " + transitLine);
			}
			
			ap.closeConnection(con);
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
		finally {
			out.close();
		}
		
	}

}
