package com.acxiom.manager;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.acxiom.model.CookModel;



public class MysqlManager {
	
	private static MysqlManager singleton = null;      

	private ArrayList<CookModel> listCook = new ArrayList<CookModel>();

    //构造方法被设为私有，防止外部使用new来创建对象，破坏单例
    private MysqlManager(){
        //System.out.println("构造函数被调用");
    }  
    
    //公有的静态方法，供外部调用来获取单例对象
    public static MysqlManager getInstance(){
        if(singleton == null){    //第一次调用该方法时，创建对象。
            singleton = new MysqlManager();
        }
        return singleton;
    }
	
	public void loadSqlData() {

		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/sys";
		String user = "jtan";
		String password = "901020";
		try {
			Class.forName(driver);
			java.sql.Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			java.sql.Statement statement = conn.createStatement();

			String sql = "select * from cookmenu;";
			ResultSet rs = statement.executeQuery(sql);

			if (listCook.size() != 0) {
				listCook.clear();
			}

			while (rs.next()) {
				// 选择sname这列数据

				CookModel model = new CookModel();
				model.id = rs.getString("id");
				model.title = rs.getString("title");
				model.tags = rs.getString("tags");
				model.imtro = rs.getString("imtro");
				model.ingredients = rs.getString("ingredients");
				model.burden = rs.getString("burden");
				model.albums = rs.getString("albums");
				model.steps = rs.getString("steps");

				listCook.add(model);
			}
			rs.close();
			conn.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testSql ()
	{
		// 驱动程序名
        String driver = "com.mysql.jdbc.Driver";

        // URL指向要访问的数据库名sys
        String url = "jdbc:mysql://localhost:3306/sys";

        // MySQL配置时的用户名
        String user = "jtan"; 

        // MySQL配置时的密码
        String password = "901020";

        try { 
         // 加载驱动程序
         Class.forName(driver);

         // 连续数据库
         java.sql.Connection conn = DriverManager.getConnection(url, user, password);

         if(!conn.isClosed()) 
          System.out.println("Succeeded connecting to the Database!");

         // statement用来执行SQL语句
         java.sql.Statement statement = conn.createStatement();

         // 要执行的SQL语句
         String sql = "select * from cookmenu;";

         // 结果集
         ResultSet rs = statement.executeQuery(sql);
         
         String name = null;
         while(rs.next()) {
          // 选择sname这列数据
          name = rs.getString("tags");
          // 输出结果
          System.out.println(rs.getString("title") + "\t" + name);
         }

         rs.close();
         conn.close();

        } catch(ClassNotFoundException e) {


         System.out.println("Sorry,can`t find the Driver!"); 
         e.printStackTrace();


        } catch(SQLException e) {


         e.printStackTrace();


        } catch(Exception e) {


         e.printStackTrace();

        } 
	}


	public ArrayList<CookModel> getListCook (){
		this.loadSqlData();
		return listCook;
	}
	
}
