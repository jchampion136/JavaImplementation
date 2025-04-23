package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/*
 * This file is where you will implement the methods needed to support this application.
 * You will write the code to retrieve and save information to the database and use that
 * information to build the various objects required by the applicaiton.
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. 
 * DO NOT change these constant values.
 * 
 * You can add any helper methods you need, but you must implement all the methods
 * in this class and use them to complete the project.  The autograder will rely on
 * these methods being implemented, so do not delete them or alter their method
 * signatures.
 * 
 * Make sure you properly open and close your DB connections in any method that
 * requires access to the DB.
 * Use the connect_to_db below to open your connection in DBConnector.
 * What is opened must be closed!
 */

/*
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// DO NOT change these variables!
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";

	public enum order_state {
		PREPARED,
		DELIVERED,
		PICKEDUP
	}


	private static boolean connect_to_db() throws SQLException, IOException 
	{

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public static void addOrder(Order o) throws SQLException, IOException 
	{
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, pickup, pizzas, toppings
		 * on pizzas, order discounts and pizza discounts.
		 * 
		 * This is a KEY method as it must store all the data in the Order object
		 * in the database and make sure all the tables are correctly linked.
		 * 
		 * Remember, if the order is for Dine In, there is no customer...
		 * so the cusomter id coming from the Order object will be -1.
		 * 
		 */
	}
	
	public static int addPizza(java.util.Date d, int orderID, Pizza p) throws SQLException, IOException
	{
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind you must also add the pizza discounts and toppings 
		 * associated with the pizza.
		 * 
		 * NOTE: there is a Date object passed into this method so that the Order
		 * and ALL its Pizzas can be assigned the same DTS.
		 * 
		 * This method returns the id of the pizza just added.
		 * 
		 */

		return -1;
	}
	
	public static int addCustomer(Customer c) throws SQLException, IOException
	 {
		 connect_to_db();
		 String query = "INSERT INTO customer (customer_FName, customer_LName, customer_PhoneNum) VALUES (?, ?, ?)";
		 PreparedStatement statement = conn.prepareStatement(query);

		 statement.setString(1,c.getFName());
		 statement.setString(2,c.getLName());
		 statement.setString(3,c.getPhone());
		 statement.executeUpdate();

		 //ResultSet result = statement.getGeneratedKeys();

		 return -1;
	}

	public static void completeOrder(int OrderID, order_state newState ) throws SQLException, IOException
	{
		connect_to_db();

		if (newState == order_state.PREPARED) {
			String order_update = "UPDATE ordertable SET ordertable_IsComplete = 1 WHERE ordertable_OrderID = ?";
			PreparedStatement stmt_update1 = conn.prepareStatement(order_update);
			stmt_update1.setInt(1,OrderID);
			stmt_update1.executeUpdate();

			String update_pizza = "UPDATE pizza SET pizza_PizzaState = 'complete' WHERE ordertable_OrderID = ?";
			PreparedStatement pizza_order = conn.prepareStatement(update_pizza);
			stmt_update1.setInt(1,OrderID);
			stmt_update1.executeUpdate();
		}

		else if(newState == order_state.DELIVERED) {
			String delivery_update = "UPDATE delivery SET delivery_IsDelivered = 1 WHERE ordertable_OrderID = ?";
			PreparedStatement stmt_update2 = conn.prepareStatement(delivery_update);
			stmt_update2.setInt(1,OrderID);
			stmt_update2.executeUpdate();
		}
		else if(newState == order_state.PICKEDUP) {
			String pickup_update = "UPDATE pickup SET pickup_IsPickedUP = 1 WHERE ordertable_OrderID = ?";
			PreparedStatement stmt_update3 = conn.prepareStatement(pickup_update);
			stmt_update3.setInt(1,OrderID);
			stmt_update3.executeUpdate();
		}

	}


	public static ArrayList<Order> getOrders(int status) throws SQLException, IOException
	 {
		 connect_to_db();
		 ArrayList<Order> orders = new ArrayList<>();

		 String query = "SELECT * FROM ordertable";

		 if(status == 1) {
			 query += " Where ordertable_isComplete = 0";
		 } else if (status == 2) {
			 query += " WHERE ordertable_isComplete = 1";

		 }
	/*
	 * Return an ArrayList of orders.
	 * 	status   == 1 => return a list of open (ie oder is not completed)
	 *           == 2 => return a list of completed orders (ie order is complete)
	 *           == 3 => return a list of all the orders
	 * Remember that in Java, we account for supertypes and subtypes
	 * which means that when we create an arrayList of orders, that really
	 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
	 *
	 * You must fully populate the Order object, this includes order discounts,
	 * and pizzas along with the toppings and discounts associated with them.
	 * 
	 * Don't forget to order the data coming from the database appropriately.
	 *
	 */
		return null;
	}
	
	public static Order getLastOrder() throws SQLException, IOException 
	{
		connect_to_db();

		String query = "SELECT * FROM orders ORDER BY order_ID DESC LIMIT 1";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		Order lastOrder = null;

		//Again, Underscores
		if (result.next()) {
			int orderID = result.getInt("order_ID");
			int custID = result.getInt("cust_ID");
			String orderType = result.getString("order_Type");
			String orderDate = result.getString("order_Date");
			double custPrice = result.getDouble("cust_Price");
			double busPrice = result.getDouble("bus_Price");
			boolean isComplete = result.getBoolean("isComplete");

			lastOrder = new Order(orderID, custID, orderType, orderDate, custPrice, busPrice, isComplete);
		}

		conn.close();

		return lastOrder;
	}


	public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException
	 {
		 connect_to_db();
		 ArrayList<Order> orderList = new ArrayList<>();

		 String query = "SELECT * FROM orders WHERE order_Date = ? ORDER BY order_ID";
		 PreparedStatement statement = conn.prepareStatement(query);
		 statement.setString(1, date);

		 ResultSet result = statement.executeQuery();

		 //May have to remove underscores
		 while (result.next()) {
			 int orderID = result.getInt("order_ID");
			 int custID = result.getInt("cust_ID");
			 String orderType = result.getString("order_Type");
			 String orderDate = result.getString("order_Date");
			 double custPrice = result.getDouble("cust_Price");
			 double busPrice = result.getDouble("bus_Price");
			 boolean isComplete = result.getBoolean("isComplete");

			 Order order = new Order(orderID, custID, orderType, orderDate, custPrice, busPrice, isComplete);
			 orderList.add(order);
		 }

		 conn.close();

		 return orderList;
	 }

		
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException 
	{
		connect_to_db();
		ArrayList<Discount> discounts = new ArrayList<>();

		String query = "SELECT * FROM discount ORDER BY discount_DiscountName";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			int id = result.getInt("discount_DiscountID");
			String name = result.getString("discount_DiscountName");
			double amount = result.getDouble("discount_Amount");
			boolean isPercent = result.getBoolean("discount_IsPercent");

			Discount discount = new Discount(id, name, amount, isPercent);
			discounts.add(discount);


		}

		conn.close();

		return discounts;
	}

	public static Discount findDiscountByName(String name) throws SQLException, IOException 
	{
		connect_to_db();
		String query = "SELECT * FROM discount WHERE discount_DiscountName = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, name);

		ResultSet result = statement.executeQuery();

		Discount discount = null;

		if(result.next()) {
			int id = result.getInt("discount_DiscountID");
			String dName = result.getString("discount_DiscountName");
			double amount = result.getDouble("discount_Amount");
			boolean isPercent = result.getBoolean("discount_IsPercent");

			discount = new Discount(id, dName, amount, isPercent);
		}

		conn.close();

		return discount;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		connect_to_db();
		ArrayList<Customer> customers = new ArrayList<>();

		String query = "SELECT * FROM customer ORDER BY customer_LName, customer_FName, customer_Phone";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			int custID = result.getInt("customer_CustID");
			String fName = result.getString("customer_FName");
			String lName = result.getString("customer_LName");
			String phone = result.getString("customer_Phone");

			Customer c = new Customer(custID,fName,lName,phone);
			customers.add(c);
		}

		conn.close();

		return customers;
	}

	public static Customer findCustomerByPhone(String phoneNumber)  throws SQLException, IOException 
	{
		connect_to_db();
		Customer customer = null;

		String query = "SELECT * FROM customer WHERE customer_PhoneNum = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, phoneNumber);

		ResultSet result = statement.executeQuery();

		if(result.next()) {
			int ID = result.getInt("customer_CustID");
			String fName = result.getString("customer_FName");
			String lName = result.getString("customer_LName");
			String phoneNum = result.getString("customer_PhoneNum");

			customer = new Customer(ID, fName, lName, phoneNum);
		}

		conn.close();

		return customer;

	}


	public static String getCustomerName(int CustID) throws SQLException, IOException 
	{
		/*
		 * COMPLETED...WORKING Example!
		 * 
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with
		 * your database from Java.  
		 * 
		 * Notice how the connection to the DB made at the start of the 
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		String cname1 = "";
		String cname2 = "";
		String query = "Select customer_FName, customer_LName From customer WHERE customer_CustID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}

		/* 
		* an BETTER example of the same query using a prepared statement...
		* with exception handling
		* 
		*/
		try {
			PreparedStatement os;
			ResultSet rset2;
			String query2;
			query2 = "Select customer_FName, customer_LName From customer WHERE customer_CustID=?;";
			os = conn.prepareStatement(query2);
			os.setInt(1, CustID);
			rset2 = os.executeQuery();
			while(rset2.next())
			{
				cname2 = rset2.getString("customer_FName") + " " + rset2.getString("customer_LName"); // note the use of field names in the getSting methods
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return cname1;
		// OR
		// return cname2;

	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException
	{
		connect_to_db();
		ArrayList<Topping> toppings = new ArrayList<>();

		String query = "SELECT * FROM topping ORDER BY topping_TopName";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		while (result.next()) {
					int topID = result.getInt("topping_TopID");
					String topName = result.getString("topping_TopName");
					double smallAMT = result.getDouble("topping_SmallAMT");
					double medAMT = result.getDouble("topping_MedAMT");
					double lgMAT = result.getDouble("topping_LgAMT");
					double xlAMT = result.getDouble("topping_XLAMT");
					double custPrice = result.getDouble("topping_CustPrice");
					double busPrice = result.getDouble("topping_BusPrice");
					int minINVT = result.getInt("topping_MinINVT");
					int curINVT =result.getInt("topping_CurINVT");

					Topping t = new Topping(topID,topName,smallAMT,medAMT,lgMAT,xlAMT,custPrice,busPrice,minINVT,curINVT);

			toppings.add(t);
		}

		conn.close();

		return toppings;
	}


	public static Topping findToppingByName(String name) throws SQLException, IOException
	{
		connect_to_db();

		String query = "SELECT * FROM topping WHERE topping_TopName = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, name);
		ResultSet result = statement.executeQuery();

		Topping topping = null;
		if (result.next()) {
			int topID = result.getInt("topping_TopID");
			String topName = result.getString("topping_TopName");
			double smallAMT =result.getDouble("topping_SmallAMT");
			double medAMT = result.getDouble("topping_MedAMT");
			double lgAMT =result.getDouble("topping_LgAMT");
			double xlAMT =result.getDouble("topping_XLAMT");
			double custPrice = result.getDouble("topping_CustPrice");
			double busPrice = result.getDouble("topping_BusPrice");
			int minINVT =result.getInt("topping_MinINVT");
			int curINVT =result.getInt("topping_CurINVT");

			topping = new Topping(topID,topName,smallAMT,medAMT,lgAMT,xlAMT,custPrice,busPrice,minINVT,curINVT);
		}

		conn.close();
		return topping;
	}


	public static ArrayList<Topping> getToppingsOnPizza(Pizza p) throws SQLException, IOException 
	{
		connect_to_db();
		ArrayList<Topping> topping = new ArrayList<>();

		String query = "SELECT t.*, pt.pizza_topping_IsDouble " +
				"FROM topping t " +
				"JOIN pizza_topping pt ON t.topping_TopID = pt.topping_TopID " +
				"WHERE pt.pizza_PizzaID = ?";

		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1,p.getPizzaID());
		ResultSet result = statement.executeQuery();

		while (result.next()) {
					int topID = result.getInt("topping_TopID");
					String topName = result.getString("topping_TopName");
					double smallAMT = result.getDouble("topping_SmallAMT");
					double medAMT = result.getDouble("topping_MedAMT");
					double lgAMT = result.getDouble("topping_LgAMT");
					double xlAMT = result.getDouble("topping_XLAMT");
					double custPrice = result.getDouble("topping_CustPrice");
					double busPrice = result.getDouble("topping_BusPrice");
					int minINVT = result.getInt("topping_MinINVT");
					int curINVT = result.getInt("topping_CurINVT");

					Topping t = new Topping(topID,topName,smallAMT,medAMT,lgAMT,xlAMT,custPrice,busPrice,minINVT,curINVT);

					//topping.IsDouble(result.getBoolean("pizza_topping_IsDouble"));
					topping.add(t);
		}

		return topping;
	}

	public static void addToInventory(int toppingID, double quantity) throws SQLException, IOException 
	{
		connect_to_db();

		String query = "UPDATE topping SET topping_CurINVT = topping_CurINVT + ? WHERE topping_TopID = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setDouble(1,quantity);
		statement.setInt(2,toppingID);
		statement.executeUpdate();
	}
	public static ArrayList<Pizza> getPizzas(Order o) throws SQLException, IOException {
		connect_to_db();
		ArrayList<Pizza> pizzaList = new ArrayList<>();

		String query = "SELECT * FROM pizza WHERE ordertable_OrderID = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, o.getOrderID()); // Set the order ID for the query

		ResultSet result = statement.executeQuery();
		while (result.next()) {
			int pizzaID = result.getInt("pizza_PizzaID");
			String size = result.getString("pizza_Size");
			String crustType = result.getString("pizza_CrustType");
			String pizzaState = result.getString("pizza_PizzaState");
			String date = result.getString("pizza_PizzaDate"); // Or getTimestamp if needed
			double custPrice = result.getDouble("pizza_CustPrice");
			double busPrice = result.getDouble("pizza_BusPrice");

			Pizza p = new Pizza(pizzaID, size, crustType, o.getOrderID(), pizzaState, date, custPrice, busPrice);
			pizzaList.add(p);
		}

		conn.close();

		return pizzaList;
	}

	public static ArrayList<Discount> getDiscounts(Order o) throws SQLException, IOException 
	{
		connect_to_db();
		ArrayList<Discount> discount = new ArrayList<>();

		//Query?
		String query = "SELECT * FROM order_discount WHERE ordertable_OrderID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, o.getOrderID());


		Statement statement = conn.createStatement();

		ResultSet result = statement.executeQuery(query);

		while (result.next()) {
					int discountID = result.getInt("discount_DiscountID");
					String discountName = result.getString("discount_DiscountName");
					double amount = result.getDouble("discount_Amount");
					boolean isPercent = result.getBoolean("discount_IsPercent");

					Discount d = new Discount(discountID,discountName,amount,isPercent);

			discount.add(d);
		}



		return discount;
	}


	public static ArrayList<Discount> getDiscounts(Pizza p) throws SQLException, IOException 
	{
		connect_to_db();
		ArrayList<Discount> discount = new ArrayList<>();

		//Might need to fix Query
		String query = "SELECT * FROM pizza_discount WHERE ordertable_OrderID = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, p.getOrderID());

		ResultSet result = statement.executeQuery(query);

		while (result.next()) {
					int id = result.getInt("discount_DiscountID");
					String name = result.getString("discount_DiscountName");
					double amount = result.getDouble("discount_Amount");
					boolean isPercent = result.getBoolean("discount_IsPercent");

					Discount d = new Discount(id,name,amount,isPercent);
			discount.add(d);
		}



		return discount;
	}

	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();

		String query = "SELECT baseprice_CustPrice FROM baseprice WHERE baseprice_Size = ? AND baseprice_CrustType = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, size);
		stmt.setString(2, crust);

		ResultSet result = stmt.executeQuery();
		double price = 0.0;

		if (result.next()) {
			price = result.getDouble("baseprice_CustPrice");
		}

		return price;
	}


	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();

		String query = "SELECT baseprice_BusPrice FROM baseprice WHERE baseprice_Size = ? AND baseprice_CrustType = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setString(1, size);
		statement.setString(2, crust);

		ResultSet result = statement.executeQuery();
		double price = 0.0;

		if (result.next()) {
			price = result.getDouble("baseprice_BusPrice");
		}

		conn.close();

		return price;
	}
	public static void printToppingReport() throws SQLException, IOException
	{
		connect_to_db();

		String query = "SELECT * FROM ToppingPopularity";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		System.out.printf("%-15s %-15s\n", "Topping", "Topping Count");
		System.out.printf("%-15s %-15s\n", "-------", "-------------");

		while (result.next()) {
			String name = result.getString("Topping");
			double count = result.getDouble("ToppingCount");

			System.out.printf("%-15s %-15.0f\n", name, count);
		}

		conn.close();
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException 
	{
		connect_to_db();

		String query = "SELECT * FROM ProfitByPizza";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		System.out.printf("%-12s %-14s %-8s %-15s\n", "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
		System.out.printf("%-12s %-14s %-8s %-15s\n", "----------", "-----------", "------", "---------------");

		while (result.next()) {
			String size = result.getString("Size");
			String crust = result.getString("Crust");
			double profit = result.getDouble("Profit");
			String month = result.getString("OrderMonth");

			System.out.printf("%-12s %-14s %-8.2f %-15s\n", size, crust, profit, month);
		}

		conn.close();
	}
	
	public static void printProfitByOrderTypeReport() throws SQLException, IOException
	{
		connect_to_db();
		String query = "SELECT * FROM ProfitByOrderType";
		PreparedStatement statement = conn.prepareStatement(query);
		ResultSet result = statement.executeQuery();

		System.out.printf("%-13s %-13s %-20s %-18s %-10s\n", "Customer Type", "Order Month", "Total Order Price", "Total Order Cost", "Profit");
		System.out.printf("%-15s %-13s %-20s %-18s %-10s\n", "-------------", "-------------", "-----------------", "----------------", "------");

		while(result.next()) {
			String type = result.getString("customerType");
			String month = result.getString("OrderMonth");
			double totalPrice = result.getDouble("TotalOrderPrice");
			double totalCost = result.getDouble("TotalOrderCost");
			double profit = result.getDouble("Profit");

			String displayType;
			if (type == null) {
				displayType = "";
			} else {
				displayType = type;
			}

			System.out.printf("%-15s %-13s %-20.2f %-18.2f %-10.2f\n",
					displayType, month, totalPrice, totalCost, profit);
		}

		conn.close();

	}
	
	
	
	/*
	 * These private methods help get the individual components of an SQL datetime object. 
	 * You're welcome to keep them or remove them....but they are usefull!
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}