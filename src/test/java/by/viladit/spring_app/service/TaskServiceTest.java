package by.viladit.spring_app.service;

import by.viladit.spring_app.dto.TaskRequestDTO;
import by.viladit.spring_app.dto.TaskResponseDTO;
import by.viladit.spring_app.model.Status;
import by.viladit.spring_app.model.Task;
import by.viladit.spring_app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ReturnsAllTasks() {
        List<Task> tasks = List.of(
                Task.builder().id(1L).title("Тестовый заголовок 1").status(Status.TODO).build(),
                Task.builder().id(2L).title("Тестовый заголовок 1").status(Status.DONE).build()
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAll();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void findByStatus_ReturnsFilteredTasks() {
        List<Task> tasks = List.of(
                Task.builder().id(1L).title("Тестовый заголовок 1").status(Status.TODO).build()
        );

        when(taskRepository.findByStatus(Status.TODO)).thenReturn(tasks);

        List<Task> result = taskService.findByStatus(Status.TODO);

        assertEquals(1, result.size());
        assertEquals(Status.TODO, result.get(0).getStatus());
    }

    @Test
    void create_SavesTask() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Тестовая задача");
        dto.setDescription("Тестовое описание");
        dto.setDueDate(LocalDate.of(2025, 8, 10));
        dto.setStatus(Status.TODO);

        Task savedTask = Task.builder()
                .id(1L)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(dto.getStatus())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO result = taskService.create(dto);

        assertNotNull(result);
        assertEquals("Тестовая задача", result.getTitle());
        assertEquals(Status.TODO, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void update_UpdatesTaskCorrectly() {
        Long taskId = 1L;

        Task existing = Task.builder()
                .id(taskId)
                .title("Старая")
                .description("Старое описание")
                .dueDate(LocalDate.of(2025, 8, 1))
                .status(Status.TODO)
                .build();

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Новая");
        dto.setDescription("Новое описание");
        dto.setDueDate(LocalDate.of(2025, 8, 15));
        dto.setStatus(Status.DONE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO result = taskService.update(taskId, dto);

        assertEquals("Новая", result.getTitle());
        assertEquals("Новое описание", result.getDescription());
        assertEquals(Status.DONE, result.getStatus());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void delete_DeletesTaskById() {
        Long id = 5L;
        taskService.delete(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    void findAllSorted_ByDueDate() {
        taskService.findAllSorted("dueDate");
        verify(taskRepository).findAll(Sort.by(Sort.Direction.ASC, "dueDate"));
    }

    @Test
    void findAllSorted_InvalidParam_ReturnsUnsorted() {
        taskService.findAllSorted("invalid");
        verify(taskRepository).findAll();
    }
}