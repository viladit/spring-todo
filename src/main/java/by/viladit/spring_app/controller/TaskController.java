package by.viladit.spring_app.controller;

import by.viladit.spring_app.dto.TaskRequestDTO;
import by.viladit.spring_app.dto.TaskResponseDTO;
import by.viladit.spring_app.model.Status;
import by.viladit.spring_app.model.Task;
import by.viladit.spring_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskRequestDTO dto) {
        return taskService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        return taskService.update(id, dto);
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String sortBy
    ) {
        List<Task> tasks;

        if (status != null) {
            tasks = taskService.findByStatus(status);
        } else if (sortBy != null) {
            tasks = taskService.findAllSorted(sortBy);
        } else {
            tasks = taskService.findAll();
        }

        return tasks.stream().map(taskService::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
