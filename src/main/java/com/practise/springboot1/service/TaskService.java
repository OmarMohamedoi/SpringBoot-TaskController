package com.practise.springboot1.service;


import com.practise.springboot1.dto.TaskPatchDto;
import com.practise.springboot1.entity.Task;
import com.practise.springboot1.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }

    public Task addTask(Task task){ //SHOULD TAKE DTO AND ADD NEW TASK INTO MEMORY WITHOUT CONTROLLER?


        logger.info("Attempting to add current task to database...");
        return taskRepository.save(task);
    }

    public int getCount(){
        return taskRepository.getStorage().size();
    }
    public Task findById(Long id){
        return taskRepository.getTaskById(id).orElseThrow(()->{throw new RuntimeException("No task found");});
    }

    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }

    public boolean deleteTaskById(long id){
        return taskRepository.deleteById(id);
    }

    public List<Task> filterByCompletion(boolean completion){
        List<Task> tasks = taskRepository.getAllTasks();
        return tasks.stream()
                .filter(task -> task.isCompleted()==completion)
                .toList();
    }

    public Task updateTaskById(Long id, Task task){
       return taskRepository.updateById(id, task)
               .orElseThrow(()->{
                       logger.error("No such task exists with ID: {}", id);
                       return new RuntimeException("No task found with id: " + id);
                });
    }

    public Task patchTaskById(Long id, TaskPatchDto taskPatchDto){
        return taskRepository.patchById(id, taskPatchDto)
                .orElseThrow(()-> new RuntimeException("No task found with id: "+ id));
    }

}
