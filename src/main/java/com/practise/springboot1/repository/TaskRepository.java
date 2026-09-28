package com.practise.springboot1.repository;

import com.practise.springboot1.dto.TaskPatchDto;
import com.practise.springboot1.entity.Task;
import com.practise.springboot1.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TaskRepository {
    private final ArrayList<Task> storage = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    Logger logger = LoggerFactory.getLogger(TaskService.class);
    public ArrayList<Task> getStorage() {
        return storage;
    }

    public Task save(Task task){
        Long newId = idCounter.incrementAndGet();
        task.setId(newId);
        storage.add(task);
        logger.info("Successfully added to database...");
        return task; //<------------I DONT UNDERSTAND THIS
    }

    public Optional<Task> getTaskById(Long id){

         return storage.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();

    }

    public ArrayList<Task> getAllTasks(){
        return storage;
    }

    public boolean deleteById(Long id){
        boolean removed = storage.removeIf(task->task.getId().equals(id));

        return removed;
    }

    public Optional<Task> updateById(Long id, Task task){
        Optional<Task> existingTask = getTaskById(id);

        if(existingTask.isPresent()){
            Task existingTasko = existingTask.get();
            existingTasko.setCompleted(task.isCompleted());
            existingTasko.setTitle(task.getTitle());
            existingTasko.setDescription(task.getDescription());
            existingTasko.setDueDate(existingTasko.getDueDate());
            return Optional.of(existingTasko);
        }
        return Optional.empty();
    }

    public Optional<Task> patchById(Long id, TaskPatchDto taskPatchDto){
        Optional<Task> existingTask = getTaskById(id);
        Task existingTask1 = existingTask.get(); //convert optional task to task
        if(existingTask.isPresent()){
            if(taskPatchDto.title()!=null){
                existingTask1.setTitle(taskPatchDto.title());
            }
            if(taskPatchDto.description()!=null){
                existingTask1.setDescription(taskPatchDto.description());
            }
            if(taskPatchDto.completed()!=null){
                existingTask1.setCompleted(taskPatchDto.completed());
            }
            if(taskPatchDto.dueDate()!=null){
                existingTask1.setDueDate(taskPatchDto.dueDate());
            }
            return Optional.of(existingTask1);
        }
        return existingTask;
    }
}
