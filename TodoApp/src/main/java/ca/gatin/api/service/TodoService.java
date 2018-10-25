package ca.gatin.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.TodoItemPersistenceService;
import ca.gatin.dao.service.TodoListPersistenceService;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.todo.TodoItem;
import ca.gatin.model.todo.TodoList;

/**
 * Service responsible for all Todo items, lists, etc..
 *
 * @author RGatin
 * @since Oct 24, 2018
 */
@Component
public class TodoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	@Autowired
	private TodoItemPersistenceService todoItemPersistenceService;
	
	@Autowired
	private TodoListPersistenceService todoListPersistenceService;
	
	/**
	 * Get list of given type of user
	 * 
	 * @param role
	 * @return
	 */
	public ServiceResponse<?> testTodos() {
		ServiceResponse<List<TodoItem>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			List<TodoItem> todoItems = todoItemPersistenceService.getAll();
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			serviceResponse.setEntity(todoItems);
			
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> testTodoList() {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		try {
			List<TodoList> todoItems = todoListPersistenceService.getAll();
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			serviceResponse.setEntity(todoItems);
			
		} catch (Exception e) {
			serviceResponse.setStatus(ResponseStatus.SYSTEM_INTERNAL_ERROR);
			e.printStackTrace();
		}
		return serviceResponse;
	}
	

}
