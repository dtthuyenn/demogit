package service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import entity.RoleEntity;
import entity.UserEntity;
import entity.TaskEntity;
import repository.RoleRepository;


public class UserServices {

	private RoleRepository roleRepository = new RoleRepository();
	private UserRespository userRepository = new UserRespository();


	public List<RoleEntity> getRole(){
		List<RoleEntity> roles = roleRepository.findAll();

		return roles;
	}

	public  boolean insertUser(String fullname, String email, String password, String phone, int roleid) {
		String passwordEncode = getMd5(password);
		return userRepository.insertUser(fullname, email, passwordEncode, phone, roleid)>0;
	}

	public static String getMd5(String input)
    {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	public List<UserEntity> getAllUsers() {
	    return userRepository.findAll();
	}

	public boolean deleteUser(int id) {
		return userRepository.deleteUser(id) > 0;
	}

	public UserEntity getUserById(int id) {
		return userRepository.findById(id);
	}

	public boolean updateUser(int id, String fullname, String email, String phone, int roleId) {
		return userRepository.updateUser(id, fullname, email, phone, roleId) > 0;
	}

    public List<TaskEntity> getTasksByUserId(int userId) {
        return userRepository.findTasksByUserId(userId);
    }
    
    public int getTaskCountByStatusAndUserId(int userId, int statusId) {
        return userRepository.findTaskCountByStatusAndUserId(userId, statusId);
    }

    public int getTotalTaskCountByUserId(int userId) {
        return userRepository.findTotalTaskCountByUserId(userId);
    }

}