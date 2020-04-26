package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class cusRep_reservations
 */
@WebServlet("/cusRep_reservations")
public class cusRep_reservations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String delete = "DELETE FROM Reservations WHERE rid = ?;";
	String update = "UPDATE Reservations "
			+ "SET bookingFee = ?, class = ?, travelTime = ?, totalFare = ?, total_discount = ?, originStop_id = ?, destinationStop_id = ?, is_round_trip = ?, isWeekly = ?, isMonthly = ? "
			+ "WHERE rid = ?";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cusRep_reservations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if (req.getParameter("type").equals("customers")) 
		{
			String SQL_query = "SELECT 'Customer' As 'Booked By', t2.userName \"User Name\", t2.firstName \"First Name\", t2.lastName \"Last Name\", t2.rid \"Reservations Number\" " + 
					"FROM (SELECT c.userName, c.firstName, c.lastName, t1.rid " + 
					"		FROM (SELECT r.rid " + 
					"				FROM Reservations r, Stops s, Trains t " + 
					"				WHERE r.originStop_id = s.stop_id AND s.train_id = ? AND t.t_id = ? AND t.transitLine_Name = ?) t1, " + 
					"		Customers c, Bookings b " + 
					"		WHERE t1.rid = b.rid AND b.userName = c.userName) t2 " + 
					"UNION " + 
					"SELECT 'Customer Representative' As 'Booked By', t3.userName \"User Name\", t3.cusFirstName \"First Name\", t3.cusLastName \"Last Name\", t3.rid \"Reservations Number\" " + 
					"FROM (SELECT o.userName, o.cusFirstName, o.cusLastName, o.rid " + 
					"		FROM (SELECT r.rid " + 
					"				FROM Reservations r, Stops s, Trains t " + 
					"				WHERE r.originStop_id = s.stop_id AND s.train_id = ? AND t.t_id = ? AND t.transitLine_Name = ?) t1, " + 
					"		Orders o " + 
					"		WHERE t1.rid = o.rid AND o.cusFirstName IS NOT NULL) t3";
			
			try {
				PreparedStatement ps = con.prepareStatement(SQL_query);
				int train = Integer.parseInt(req.getParameter("train"));
				String tran = req.getParameter("tran");
				ps.setInt(1, train);
				ps.setInt(2, train);
				ps.setString(3, tran);
				ps.setInt(4, train);
				ps.setInt(5, train);
				ps.setString(6, tran);
				
				ResultSet rs = ps.executeQuery();
				
				Common.printSQLResultTable(rs, response, "List of all customers who have seats reserved on train " + train + " and transit line " + tran + " :");
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ap.closeConnection(con);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if (req.getParameter("type").equals("addWithFirstName")) 
		{
			
			HttpSession session = req.getSession();
			session.removeAttribute("cusUserName");
			session.setAttribute("cusFirstName", req.getParameter("fname"));
			session.setAttribute("cusLastName", req.getParameter("lname"));
			req.getRequestDispatcher("/Schedule?line=all").forward(req, response);
		} 
		else if (req.getParameter("type").equals("addWithUserName")) {
			HttpSession session = req.getSession();
			session.setAttribute("cusUserName", req.getParameter("uname"));
			session.removeAttribute("cusFirstName");
			session.removeAttribute("cusLastName");
			req.getRequestDispatcher("/Schedule?line=all").forward(req, response);
		} else if (req.getParameter("type").equals("delete")) {
			try {
				PreparedStatement ps = con.prepareStatement(delete);
				ps.setString(1, req.getParameter("rid"));
				ps.executeUpdate();
				response.sendRedirect(req.getContextPath() + "/cusRep.jsp");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// edit
			try {
				PreparedStatement ps = con.prepareStatement(update);
				
				ps.setDouble(1, Double.parseDouble(req.getParameter("fee")));
				ps.setString(2, req.getParameter("class"));
				ps.setInt(3, Integer.parseInt(req.getParameter("travel-time")));
				ps.setDouble(4, Double.parseDouble(req.getParameter("total-fare")));
				ps.setDouble(5, Double.parseDouble(req.getParameter("total-discount")));
				ps.setString(6, req.getParameter("origin-stop"));
				ps.setString(7, req.getParameter("des-stop"));
				
				String tripType= req.getParameter("trip");
				
				if (tripType.equals("One-Way")) {
					ps.setInt(8, 0);
					ps.setInt(9, 0);
					ps.setInt(10, 0);
				} else if (tripType.contentEquals("Round-Trip")) {
					ps.setInt(8, 1);
					ps.setInt(9, 0);
					ps.setInt(10, 0);
				} else if (tripType.equals("Weekly")) {
					ps.setInt(8, 1);
					ps.setInt(9, 1);
					ps.setInt(10, 0);
				} else {
					ps.setInt(8, 1);
					ps.setInt(9, 0);
					ps.setInt(10, 1);
				}
				
				ps.setInt(11, Integer.parseInt(req.getParameter("rid")));
				
				ps.executeUpdate();
				response.sendRedirect(req.getContextPath() + "/cusRep.jsp");
				
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ap.closeConnection(con);
	}

}