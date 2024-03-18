package com.example.mallapi.repository;

import com.example.mallapi.domain.Todo;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void toDoInsert() {
        for (int i = 1; i <= 100; i++) {

            Todo todo = Todo.builder()
                    .title("Title..." + i)
                    .dueDate(LocalDate.of(2024, 3, 13))
                    .writer("user00")
                    .build();

            todoRepository.save(todo);
        }
    }

    @Test
    public void toDoRead() {

        Long tno = 33L;

        Optional<Todo> result = todoRepository.findById(tno);

        Assertions.assertThat(tno).isEqualTo(result.get().getTno());
    }

    @Test
    public void testModify() {

        Long tno = 33L;

        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        todo.changeTitle("change title");
        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.of(2024, 3, 15));

        todoRepository.save(todo);

        Optional<Todo> modifyResult = todoRepository.findById(tno);
        Todo modifyTodo = result.orElseThrow();

        Assertions.assertThat(modifyTodo.getDueDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        Assertions.assertThat(modifyTodo.getTitle()).isEqualTo("change title");
        Assertions.assertThat(modifyTodo.isComplete()).isEqualTo(true);
    }

    @Test
    public void testDelete() {
        List<Todo> todoList = todoRepository.findAll();
        Assertions.assertThat(todoList.size()).isEqualTo(100);

        todoRepository.deleteById(100L);

        List<Todo> afterDeleteList = todoRepository.findAll();
        Assertions.assertThat(afterDeleteList.size()).isEqualTo(99);
    }

    @Test
    public void testPaging() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalElements());

        result.getContent().stream().forEach(todo -> log.info(todo));
    }
}
