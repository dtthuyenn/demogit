package service;
import java.util.List;
import entity.StatusEntity;
import repository.StatusRepository;

public class StatusServices {

    private StatusRepository statusRepository = new StatusRepository();

    public List<StatusEntity> getAllStatuses() {
        return statusRepository.findAll();
    }

    public StatusEntity getStatusById(int id) {
        return statusRepository.findById(id);
    }

    
}