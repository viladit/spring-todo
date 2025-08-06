package by.viladit.spring_app.repository;

import by.viladit.spring_app.model.Status;
import by.viladit.spring_app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Status status);
}
