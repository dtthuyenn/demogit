package service;
import java.util.List;

import entity.RoleEntity;
import repository.RoleRepository;

public class RoleServices {

    private RoleRepository roleRepository = new RoleRepository();

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    // Thêm method để lấy role theo ID
    public RoleEntity getRoleById(int id) {
        return roleRepository.findById(id); 
    }

    // Thêm method để cập nhật role
    public void updateRole(RoleEntity role) {
        roleRepository.update(role); 
    }

    // Thêm method để xóa role
    public void deleteRole(int id) {
        roleRepository.delete(id); 
    }
    
 // Thêm method để thêm role
    public void addRole(RoleEntity role) {
        roleRepository.insert(role); 
    }
}