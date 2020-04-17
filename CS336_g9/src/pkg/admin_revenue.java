package pkg;

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
 * Servlet implementation class admin_revenue
 */
@WebServlet("/admin_revenue")
public class admin_revenue extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin_revenue() {
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
		//doGet(request, response);
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		/*
		 * GET THE BEST CUSTOMER
		 * 
		 * */
		if (req.getParameter("type").equals("bestCustomer")) {
			try {
				// This is the query statement to query the best customer who generated the highest booking fee and fares combined
				String query = "SELECT x.userName \"Customer User Name\", c.firstName \"Customer First Name\", c.lastName \"Customer Last Name\", max(totalBookingFee + totalFare) Revenue " + 
						"from (" + 
						"select userName, sum(bookingFee) totalBookingFee, sum(totalFare) totalFare " + 
						"from Bookings b " + 
						"JOIN Reservations r " + 
						"ON b.rid = r.rid " + 
						"group by userName) x, Customers c " + 
						"where x.userName = c.userName;";
		
				Statement sm = con.createStatement();
				ResultSet rs = sm.executeQuery(query);
				Common.printSQLResultTable(rs, res, "Report of the best customer");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		 * GET TOP 5 ACTIVE TRANSIT LINES
		 * 
		 * */
		
		/*
		 * GET REVENUE BY TRANSIT LINE
		 * 
		 * */
		
		/*
		 * GET REVENUE BY DESTINATION CITY
		 * 
		 * */
		
		/*
		 * GET REVENUE BY CUSTOMER
		 * 
		 * */
		
		ap.closeConnection(con);
	}

}
