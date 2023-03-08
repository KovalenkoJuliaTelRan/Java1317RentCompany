package telran.rentcompanyserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.rentcompanyserver.dto.*;
import telran.rentcompanyserver.service.IRentCompany;
import static telran.rentcompanyserver.api.ApiConstants.*;

import java.util.List;

//String ADD_CAR = "/car/add";//post
//String ADD_MODEL = "/model/add";//post
//String REMOVE_CAR = "/car/remove";//delete
//String REMOVE_MODEL = "/model/remove";//delete
@RestController
public class RentCompanyAdministratorController
{
	@Autowired
	IRentCompany company;
	
	@PostMapping(value = ADD_CAR)
	CarsReturnCode addCar(@RequestBody Car car)
	{
		return company.addCar(car);
	}
	
	@DeleteMapping(value = REMOVE_CAR)
	RemovedCarData removeCar(@RequestParam(name = CAR_NUMBER) String regNumber)
	{
		return company.removeCar(regNumber);
	}
	
	@PostMapping(value = ADD_MODEL)
	CarsReturnCode addModel(@RequestBody CarModel model)
	{
		return company.addModel(model);
	}
	
	@DeleteMapping(value = REMOVE_MODEL)
	List<RemovedCarData> removeModel(@RequestParam(name = MODEL_NAME) String modelName)
	{
		return company.removeModel(modelName);
	}
}
