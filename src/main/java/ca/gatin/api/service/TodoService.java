package ca.gatin.api.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import ca.gatin.api.exception.PermissionDeniedException;
import ca.gatin.api.exception.ValidationException;
import ca.gatin.api.response.FieldErrorBean;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.TodoItemPersistenceService;
import ca.gatin.dao.service.TodoListPersistenceService;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.request.CreateToDoItemBean;
import ca.gatin.model.request.CreateToDoListBean;
import ca.gatin.model.security.User;
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
	 * Get list of all todo items from all lists
	 * 
	 * @param role
	 * @return
	 */
	public ServiceResponse<?> testTodoItemAll() {
		ServiceResponse<List<TodoItem>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoItem> todoItems = todoItemPersistenceService.getAll();
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);

		return serviceResponse;
	}
	
	public ServiceResponse<?> testTodoListAll() {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoList> todoItems = todoListPersistenceService.getAll();
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);
		
		return serviceResponse;
	}
	
	public ServiceResponse<?> getTodoListAll(User user) {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoList> todoItems = todoListPersistenceService.getByCreator(user);
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);
			
		return serviceResponse;
	}
	
	public ServiceResponse<?> getTodoListById(User user, Long id) {
		ServiceResponse<TodoList> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList todoItem = todoListPersistenceService.getById(id);
		if (todoItem.getCreator().getId().equals(user.getId())) {
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			serviceResponse.setEntity(todoItem);
		} else {
			serviceResponse.setStatus(ResponseStatus.UNAUTHORIZED_TODOITEM_REQUEST);
		}
			
		return serviceResponse;
	}
	
	public ServiceResponse<?> renameList(User user, Long listId, String newName) throws NoSuchMethodException, SecurityException, MethodArgumentNotValidException {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		newName = newName.trim();
		if (StringUtils.isEmpty(newName)) {
			FieldErrorBean fieldError = new FieldErrorBean("getStringVar", "is empty");
			throw new ValidationException(fieldError);
		}
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		
		if (listItem.getCreator().getId() == user.getId()) {
			listItem.setName(newName);
			listItem.setDateLastModified(new Date());
			todoListPersistenceService.save(listItem);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} else {
			throw new PermissionDeniedException();
		}
			
		return serviceResponse;
	}
	
	public ServiceResponse<?> setTodoListHideCompleted(User user, Long listId, boolean doHide) throws NoSuchMethodException, SecurityException, MethodArgumentNotValidException {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		
		if (listItem.getCreator().getId() == user.getId()) {
			listItem.setHideCompleted(doHide);
			listItem.setDateLastModified(new Date());
			todoListPersistenceService.save(listItem);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} else {
			throw new PermissionDeniedException();
		}
			
		return serviceResponse;
	}

	public ServiceResponse<?> deleteList(User user, Long listId) {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		
		if (listItem.getCreator().getId() == user.getId()) {
			todoListPersistenceService.deleleListItem(listId);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} else {
			throw new PermissionDeniedException();
		}
			
		return serviceResponse;
	}

	public ServiceResponse<?> createTodoList(User user, CreateToDoListBean todoListBean) {
		ServiceResponse<TodoList> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		String name = todoListBean.getName().trim();
		
		TodoList listItem = todoListPersistenceService.getByName(name);
		if (listItem != null) {
			serviceResponse.setStatus(ResponseStatus.DATABASE_RECORD_DUPLICATION);
			
		} else {
			listItem = new TodoList();
			listItem.setName(name);
			listItem.setCreator(user);
			listItem.setDateCreated(new Date());
			TodoList createdList = todoListPersistenceService.save(listItem);
			
			if (createdList != null) {
				serviceResponse.setEntity(createdList);
				serviceResponse.setStatus(ResponseStatus.SUCCESS);
			}
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> createTodoItem(User user, CreateToDoItemBean bean) {
		ServiceResponse<TodoItem> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoItem todoItem = new TodoItem();
		todoItem.setTitle(bean.getTitle().trim());
		todoItem.setNotes(bean.getNotes());
		todoItem.setDateCreated(new Date());
		todoItem.setListId(bean.getListId());
		
		TodoList listItem = todoListPersistenceService.getById(todoItem.getListId());
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		this.checkListOwnership(listItem, user);
		
		try {
			TodoItem createdItem = todoItemPersistenceService.save(todoItem);
			serviceResponse.setEntity(createdItem);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			serviceResponse.setStatus(ResponseStatus.DATABASE_PERSISTANCE_ERROR);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> updateTodoItem(User user, TodoItem todoItem) {
		ServiceResponse<TodoItem> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList listItem = todoListPersistenceService.getById(todoItem.getListId());
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		this.checkListOwnership(listItem, user);
		
		try {
			todoItem.setDateLastModified(new Date());
			TodoItem updatedItem = todoItemPersistenceService.save(todoItem);
			serviceResponse.setEntity(updatedItem);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			serviceResponse.setStatus(ResponseStatus.DATABASE_PERSISTANCE_ERROR);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<?> deteleTodoItem(User user, Long listId, Long id) {
		ServiceResponse<TodoItem> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		this.checkListOwnership(listItem, user);
				
		try {
			todoItemPersistenceService.delete(id);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			serviceResponse.setStatus(ResponseStatus.DATABASE_PERSISTANCE_ERROR);
		}
		return serviceResponse;
	}
	
	private void checkListOwnership(TodoList listItem, User user) {
		if (listItem.getCreator().getId() != user.getId()) {
			throw new PermissionDeniedException();
		} 
	}
}
