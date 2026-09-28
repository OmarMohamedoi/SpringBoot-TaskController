package com.practise.springboot1.controller;

import com.practise.springboot1.dto.TaskDTO;
import com.practise.springboot1.dto.TaskPatchDto;
import com.practise.springboot1.entity.Task;
import com.practise.springboot1.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController()
@RequestMapping("/api/tasks")
public class TaskController {

    @Value("${tasktracker.max-tasks}")
    int maxTasks;

    @Value("${tasktracker.default-page-size}")
    int pageSize;

    TaskService taskService;
    public TaskController(TaskService taskService){
        this.taskService=taskService;
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO){
        if(taskService.getCount()>=maxTasks){
           return ResponseEntity.badRequest().build();
        }
        Task newTask = new Task(
                null,
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.completed(),
                taskDTO.dueDate()
        );
        Task savedTask = taskService.addTask(newTask);

        //build URI e.g.(/api/tasks/1)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();
        //return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        return ResponseEntity.created(location).body(newTask);



    }
//    @GetMapping
//    public ResponseEntity<List<Task>> getAllTasks(){
//        List<Task> tasks =taskService.getAllTasks();
//        return ResponseEntity.ok(tasks);
//    }

    @GetMapping
    public ResponseEntity<List<Task>> getFilteredByCompletionTasks(@RequestParam(required = false) Boolean completed){

        if(completed!=null) {

            List<Task> tasks = taskService.filterByCompletion(completed);
            return ResponseEntity.ok(tasks);
        }
        List<Task> tasks = taskService.getAllTasks();
        int endIndex = Math.min(pageSize, tasks.size());

        // Create a sublist for pagination
        List<Task> finalTasks = tasks.subList(0, endIndex);
        return ResponseEntity.ok().body(finalTasks);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long Id){
        try{

            return ResponseEntity.ok(taskService.findById(Id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }


    }

    @PutMapping("/{Id}")
    public ResponseEntity<Task> updateTaskById(@PathVariable Long Id, @Valid @RequestBody TaskDTO taskDTO){
        try{
            Task taskdetails = new Task(
                    Id,
                    taskDTO.title(),
                    taskDTO.description(),
                    taskDTO.completed(),
                    taskDTO.dueDate()
            );
            Task updatedTask = taskService.updateTaskById(Id, taskdetails);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{Id}")
    public ResponseEntity<Task> patchById(@PathVariable Long Id, @RequestBody TaskPatchDto taskPatchDto){
        try {
            Task task = taskService.patchTaskById(Id, taskPatchDto);
           return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deleteById(@PathVariable Long Id){
        if(taskService.deleteTaskById(Id)){
            return ResponseEntity.ok("Successfully removed.");
        }
        else{
            return ResponseEntity.badRequest().body("No such task existing");
        }
    }
}
