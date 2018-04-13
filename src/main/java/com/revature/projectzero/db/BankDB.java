package com.revature.projectzero.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Owner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.revature.projectzero.accounts.Account;
import com.revature.projectzero.accounts.AccountTypes;
import com.revature.projectzero.accounts.User;

public class BankDB implements DatabaseSingleton {
	public static final int RESULTS_PER_PAGE_ACTIVE_ACCOUNTS = 25;
	final static Logger log = Logger.getLogger(BankDB.class);
	
	private Connection con;
	private static final BankDB instance = new BankDB();
	private BankDB() {initDB();}
	
	public static void main(String[] args) {
		DatabaseSingleton db = BankDB.getInstance();
//		Account a = db.getAccountByID(1);
//		System.out.println(a);
		User u = db.getUserByID(1);
//		System.out.println(u);
//		if(u!=null) {			
//			u.setID(10);
//			db.update(u);
//		}
//		a.setBalance(2000.31415);;
//		db.update(a);
		u=db.getUserByUsernameAndPassword("Apo45ty", "Weblog1!");
//		System.out.println(u);
//		System.out.println(db.isUniqueUsername("Apo45ty"));
//		System.out.println(db.isUniqueUsername("Apo45"));
//		db.create(new User("Denisse","Perez","DP1",true,true,false,0L), "password");
//		db.create(new Account(2000, 0, u, AccountTypes.savings04092018, false));
//		System.out.println(db.getUnActivatedAccounts(0));
		System.out.println(db.getUserAccounts(u, 0));
//		System.out.println(u.getAccounts());
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		con.close();
	}
	/**
	 * 
	 * @param url
	 */
	private void initDB() {
		InputStream in=null;
		try {
			Properties props = new Properties();
			in = new FileInputStream("src/main/resources/db.properties");
			props.load(in);
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection(props.getProperty("jdbc.url"),
					props.getProperty("jdbc.username"),
					props.getProperty("jdbc.password")
			);
		} catch (ClassNotFoundException e) {
			log.error(e);
		} catch (SQLException e) {
			log.error(e);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static DatabaseSingleton getInstance() {
		return instance;
	}
	
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#getUserByID(java.lang.String)
	 */
	public User getUserByID(long id) {
		PreparedStatement getuser = null;
		try {
			getuser = con.prepareStatement("SELECT * FROM bankuser WHERE id = ? AND isDeleted=0");
			getuser.setLong(1, id);
		
			ResultSet rs = getuser.executeQuery();
			rs.next();
			if(rs.getBoolean("isDeleted"))
				return null;
			return new User(rs.getString("fname"),
							rs.getString("lname"),
							rs.getString("username"),
							rs.getBoolean("isAdmin"),
							rs.getBoolean("isDeleted"),
							rs.getBoolean("isActive"),
							rs.getLong("id")
							);
		} catch (SQLException e) {
			log.error(e); 
		} finally {
			if(getuser != null)
				try {
					getuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#getAccountByID(java.lang.String)
	 */
	public Account getAccountByID(long accountID) {
		PreparedStatement getaccount = null;
		try {
			getaccount = con.prepareStatement("SELECT * FROM account WHERE id = ? AND isDeleted=0");
			ResultSet rs = getaccount.executeQuery();
		
			rs.next();
			if(rs.getBoolean("isDeleted"))
				return null;
			return new Account(rs.getDouble("balance"),
							rs.getLong("id"),
							null,
							AccountTypes.valueOf(rs.getString("type")),
							rs.getBoolean("isDeleted")
							);
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if(getaccount != null)
				try {
					getaccount.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
			
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#update(com.revature.projectzero.accounts.User)
	 */
	public void update(User u) {
		PreparedStatement updateuser = null;
		try {
			updateuser = con.prepareStatement("UPDATE bankuser SET "
					+ "id=?, "
					+ "fname=?,"
					+ "lname=?,"
					+ "isAdmin=?,"
					+ "isActive=?,"
					+ "isDeleted=?,"
					+ "username=? "
					+ "WHERE username=?");
			updateuser.setLong(1, u.getID());
			updateuser.setString(2, u.getName());
			updateuser.setString(3, u.getLname());
			updateuser.setBoolean(4, u.isAdmin());
			updateuser.setBoolean(5, u.isActive());
			updateuser.setBoolean(6, u.isDeleted());
			updateuser.setString(7, u.getUsername());
			updateuser.setString(8, u.getUsername());
			updateuser.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(updateuser != null)
				try {
					updateuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
 	
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#update(com.revature.projectzero.accounts.Account)
	 */
	public void update(Account a) {
		Statement updateaccount = null;
		try {
			updateaccount = con.createStatement();
			String s = null;
			if(a.getOwner()==null)
				s="UPDATE account SET "
						+ "id="+a.getId()+", "
						+ "balance="+a.getBalance()+","
						+ "type=\'"+a.getType()+"\',"
						+ "isDeleted="+(a.isDeleted()?1:0)
						+ " WHERE id="+a.getId();
			else 
				s="UPDATE account SET "
						+ "id="+a.getId()+", "
						+ "owner='"+a.getOwner().getUsername()+"',"
						+ "balance="+a.getBalance()+","
						+ "type=\'"+a.getType()+"\',"
						+ "isDeleted="+(a.isDeleted()?1:0)
						+ " WHERE id="+a.getId();
			updateaccount.executeUpdate(s);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(updateaccount != null)
				try {
					updateaccount.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#create(com.revature.projectzero.accounts.User)
	 */
	public void create(User u,String password) {
		PreparedStatement updateuser = null;
		try {
			updateuser = con.prepareStatement("INSERT INTO bankuser VALUES("
					+ "?,"
					+ "?,"
					+ "?,"
					+ "?,"
					+ "?,"
					+ "?,"
					+ "?,"
					+ "?"
					+ ")");
			updateuser.setLong(1, u.getID());
			updateuser.setString(2, u.getName());
			updateuser.setString(3, password);
			updateuser.setString(4, u.getLname());
			updateuser.setBoolean(5, u.isAdmin());
			updateuser.setBoolean(6, u.isActive());
			updateuser.setBoolean(7, u.isDeleted());
			updateuser.setString(8, u.getUsername());
			updateuser.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(updateuser != null)
				try {
					updateuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
 	
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#create(com.revature.projectzero.accounts.Account)
	 */
	public void create(Account a) {
		Statement updateuser = null;
		try {
			updateuser = con.createStatement();
			String s ="INSERT INTO account (owner,balance,isDeleted,type) VALUES ("
					+ "\'"+a.getOwner().getUsername()+"\',"
					+ a.getBalance()+","
					+ (a.isDeleted()?1:0)+","
					+ "\'"+a.getType().toString()+"\')";
			updateuser.execute(s);
		} catch (SQLException e1) {
			log.error(e1);
		} finally {
			if(updateuser != null)
				try {
					updateuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
 	
	}

	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#delete(com.revature.projectzero.accounts.User)
	 */
	public void delete(User u) {
		u.setDeleted(true);
		update(u);
	}
	
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#delete(com.revature.projectzero.accounts.Account)
	 */
	public void delete(Account a) {
		a.setDeleted(true);
		update(a);
	}
	/* (non-Javadoc)
	 * @see com.revature.projectzero.db.DatabaseSingleton#getUserByUsernameAndPassword(java.lang.String, java.lang.String)
	 */
	public User getUserByUsernameAndPassword(String username,String password) {
		PreparedStatement getuser = null;
		ResultSet rs = null;
		try {
			getuser = con.prepareStatement("SELECT * FROM bankuser WHERE username = ? AND password = ? ");
			getuser.setString(1, username);
			getuser.setString(2, password);
			rs = getuser.executeQuery();
			if(rs.next())
				return new User(rs.getString("fname"),
						rs.getString("lname"),
						rs.getString("username"),
						rs.getBoolean("isAdmin"),
						rs.getBoolean("isActive"),
						rs.getBoolean("isDeleted"),
						rs.getLong("id")
						);
		} catch (SQLException e1) {
			log.error(e1);
			return null;
		}  finally {
			if(getuser != null)
				try {
					getuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
		return null;
		
	}
	/**
	 * 
	 */
	public boolean isUniqueUsername(String username) {
		PreparedStatement getuser = null;
		ResultSet rs = null;
		try {
			getuser = con.prepareStatement("SELECT * FROM bankuser WHERE username = ?");
			getuser.setString(1, username);
			rs = getuser.executeQuery();
			return !rs.next();
		} catch (NumberFormatException | SQLException e) {
			log.error(e);
		} finally {
			if(getuser != null)
				try {
					getuser.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
		return false;
	}
	/**
	 * 
	 */
	public List<User> getUnActivatedAccounts(int page) {
		int offset = page*RESULTS_PER_PAGE_ACTIVE_ACCOUNTS;
		PreparedStatement statement = null;		
		ResultSet rs = null;
		List<User> result = null;
		try {
			statement = con.prepareStatement("SELECT *  FROM   bankuser u WHERE isDeleted=0 ORDER BY u.username OFFSET "+offset+" ROWS FETCH NEXT "+RESULTS_PER_PAGE_ACTIVE_ACCOUNTS+" ROWS ONLY");
			rs = statement.executeQuery();
			result = new LinkedList<>();
			while(rs.next()) {
				if(!rs.getBoolean("isDeleted")&&!rs.getBoolean("isActive"))
					result.add(new User(
							rs.getString("fname"),
							rs.getString("lname"),
							rs.getString("username"),
							rs.getBoolean("isAdmin"),
							rs.getBoolean("isActive"),
							rs.getBoolean("isDeleted"),
							rs.getLong("id")
					));
			}
			return result;
		} catch (SQLException e) {
			log.error(e);
		} finally {
			if(statement != null)
				try {
					statement.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
		return result;
	}

	public List<Account> getUserAccounts(User u,int page) {
		int offset = page*RESULTS_PER_PAGE_ACTIVE_ACCOUNTS;
		PreparedStatement statement = null;
		List<Account> result = null;
		try {
			statement = con.prepareStatement("SELECT *  FROM  account a WHERE a.owner=? AND isDeleted=0 ORDER BY a.owner OFFSET "+offset+" ROWS FETCH NEXT "+RESULTS_PER_PAGE_ACTIVE_ACCOUNTS+" ROWS ONLY");
			statement.setString(1, u.getUsername());
			ResultSet rs = statement.executeQuery();
			result = new LinkedList<>();
			while(rs.next()) {
				result.add(new Account(rs.getDouble("balance"), rs.getLong("id"), u,AccountTypes.valueOf(rs.getString("type")),
						rs.getBoolean("isDeleted")));
			}
			u.setAccounts(result);
			return result;
		} catch (SQLException e1) {
			log.error(e1);
		} finally {
			if(statement != null)
				try {
					statement.close();
				} catch (SQLException e1) {
					log.error(e1);
				}
		}
		return result;
	}
	
}
