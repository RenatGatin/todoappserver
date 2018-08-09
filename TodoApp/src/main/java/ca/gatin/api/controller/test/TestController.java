package ca.gatin.api.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.controller.BaseController;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.EmailService;

/**
 * Testing public API Controller
 * 
 * @author RGatin
 * @since 17-Apr-2016
 */
@RestController
@RequestMapping(value= "/api/test")
public class TestController extends BaseController {
	
	@Autowired
	EmailService emailService;
	
	@RequestMapping(value= "/simple", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testManual() {
		logger.info("> Test");
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		serviceResponse.setEntity("successfull tested API");
		return serviceResponse;
	}
	
	@RequestMapping(value= "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> testMailSender() {
		ServiceResponse<?> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_INTERNAL_ERROR);
		
		try {
			emailService.send(null);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} catch (Exception e) {
			logger.error("Error sending email", e);
		}
		
		return serviceResponse;
	}
	
}
