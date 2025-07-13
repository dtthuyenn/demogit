package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.MysqlConfig;
import entity.RoleEntity;

public class RoleRepository {
	
	// Nguyen tat dat ten sao cho de hinh dung den cau truy van
	//Select:find
	//WhERE: By
	//Ex: Select + from roles where r.id =1 AND r.name = "ABC"
	
	//SELECT: đặt tên hàm là file
	//WHERE: BY
	
	public List<RoleEntity> findAll(){
		List<RoleEntity> list = new ArrayList<RoleEntity>();
		 String query="SELECT * FROM roles r";
		 try {
			 /*
			  * Cách chạy
			  * - Tạo ra 1 Role entity rỗng
			  * -Chuẩn bị câu truy vấn
			  * - Trong try truy vấn csdl xong
			  * -Duyệt qua từng dòng csdl truy vấn được
			  * -Lúc này cái list ở Roleentity dính là từng dòng csdl truy vấn đươc
			  * =>Chỉ vậy thôi không xử lí cơ sở dữ liệu 
			  * 
			  * */
			 
			// Mở kết nối CSDL
			 Connection connection = MysqlConfig.getConnection(); 
			 // Truyền câu truy vấn vào CSDL để kết nối
			 PreparedStatement statement = connection.prepareStatement(query);
			 //Xuất các kết quả truy vấn của mình về mảng
			 ResultSet resultSet = statement.executeQuery();
			 
			 // Biến dữ liệu của resultSet thành cái List
			 while (resultSet.next()) {
				RoleEntity entity = new RoleEntity();
				//Lấy giá trị của từng cộ gán vào từng thuộc tính của đối tượng
				entity.setId(resultSet.getInt("id"));
				entity.setName(resultSet.getString("name"));
				entity.setDescription(resultSet.getString("description"));
				
				list.add(entity);
			}
		 }catch (Exception e) {
			 //Đặt tên hàm sau này lỗi dễ research để biết lỗi nó nằm ở đâu
			System.out.println("findAll"+ e.getLocalizedMessage());
		}
		 return list;
	}
	
	 // Thêm method để tìm role theo ID
    public RoleEntity findById(int id) {
        RoleEntity role = null;
        String query = "SELECT * FROM roles WHERE id = ?";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = new RoleEntity();
                role.setId(resultSet.getInt("id"));
                role.setName(resultSet.getString("name"));
                role.setDescription(resultSet.getString("description"));
            }
        } catch (Exception e) {
            System.out.println("findById: " + e.getMessage());
        }
        return role;
    }

    // Thêm method để cập nhật role
    public void update(RoleEntity role) {
        String query = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            statement.setInt(3, role.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("update: " + e.getMessage());
        }
    }

    // Thêm method để xóa role
    public void delete(int id) {
        String query = "DELETE FROM roles WHERE id = ?";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("delete: " + e.getMessage());
        }
    }
	
    // Thêm method để insert role
    public void insert(RoleEntity role) {
        String query = "INSERT INTO roles (name, description) VALUES (?, ?)";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("insert: " + e.getMessage());
        }
    }
	
}