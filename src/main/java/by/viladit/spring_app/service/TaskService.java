package by.viladit.spring_app.service;

import by.viladit.spring_app.dto.TaskRequestDTO;
import by.viladit.spring_app.dto.TaskResponseDTO;
import by.viladit.spring_app.model.Status;
import by.viladit.spring_app.model.Task;
import by.viladit.spring_app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllSorted(String sortBy) {
        if ("dueDate".equalsIgnoreCase(sortBy)) {
            return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "dueDate"));
        } else if ("status".equalsIgnoreCase(sortBy)) {
            return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));
        } else {
            return taskRepository.findAll();
        }
    }


    public List<Task> findByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = fromDto(dto);
        return toDto(taskRepository.save(task));
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        return toDto(taskRepository.save(task));
    }


    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskResponseDTO toDto(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    private Task fromDto(TaskRequestDTO dto) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();
    }

}
