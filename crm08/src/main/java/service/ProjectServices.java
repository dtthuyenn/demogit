
package service;


import java.util.List;

import entity.ProjectEntity;
import repository.ProjectRepository;

public class ProjectServices {
    private ProjectRepository projectRepository = new ProjectRepository();

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectEntity getProjectById(int id) {
        return projectRepository.findById(id);
    }

    public void addProject(ProjectEntity project) {
        projectRepository.insert(project);
    }

    public void updateProject(ProjectEntity project) {
        projectRepository.update(project);
    }

    public void deleteProject(int id) {
        projectRepository.delete(id);
    }
}