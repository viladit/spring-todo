package by.viladit.spring_app.dto;

import by.viladit.spring_app.model.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;
}
