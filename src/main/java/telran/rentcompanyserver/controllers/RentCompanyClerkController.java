package telran.rentcompanyserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import telran.rentcompanyserver.dto.*;
import telran.rentcompanyserver.service.IRentCompany;
import static telran.rentcompanyserver.api.ApiConstants.*;

import java.util.List;

//String ADD_DRIVER = "/driver/add";//post
//String GET_MODEL = "/model";
//String GET_DRIVER_CARS = "/driver/cars";
//String GET_CAR_MODELS = "/models";
//String GET_MODEL_CARS = "/model/cars";
//String RENT_CAR = "/car/rent";//post
//String RETURN_CAR = "/car/return";//post
//String GET_CAR_DRIVERS = "/drivers/car";
//String GET_CAR = "/car";
//String GET_DRIVER = "/driver";

@RestController
public class RentCompanyClerkController
{
	@Autowired
	IRentCompany company;
	
	@PostMapping(value = ADD_DRIVER)
	CarsReturnCode addDriver(@Valid @RequestBody Driver driver)
	{
		return company.addDriver(driver);
	}

	@GetMapping(value = GET_MODEL)
	CarModel getModel(@RequestParam(name = MODEL_NAME) String modelName)
	{
		return company.getCarModel(modelName);
	}

	@GetMapping(value = GET_CAR)
	Car getCar(@RequestParam(name = CAR_NUMBER) String regNumber)
	{
		return company.getCar(regNumber);
	}

	@GetMapping(value = GET_CAR_DRIVERS)
	List<Driver> getCarDrivers(@RequestParam(name = CAR_NUMBER) String regNumber)
	{
		return company.getDriversByCar(regNumber);
	}

	@GetMapping(value = GET_DRIVER_CARS)
	List<Car> getDriverCars(@Min(0) @RequestParam(name = LICENSE_ID) long licenseId)
	{
		return company.getCarsByDriver(licenseId);
	}

	@GetMapping(value = GET_MODEL_CARS)
	List<Car> getModelCars(@RequestParam(name = MODEL_NAME) String modelName)
	{
		return company.getCarsByModel(modelName);
	}

	@GetMapping(value = GET_CAR_MODELS)
	List<String> getModelNames()
	{
		return company.getModelNames();
	}

	@PostMapping(value = RETURN_CAR)
	RemovedCarData returnCar(@RequestBody ReturnCarData rcd)
	{
		return company.returnCar(rcd.getRegNumber(), rcd.getLicenseId(), 
				rcd.getReturnDate(), rcd.getDamage(), rcd.getTankPercent());
	}

	@PostMapping(value = RENT_CAR)
	CarsReturnCode rentCar(@RequestBody RentCarData rcd)
	{
		return company.rentCar(rcd.getRegNumber(), rcd.getLicenseId(), 
				rcd.getRentDate(), rcd.getRentDays());
	}

	@GetMapping(value = GET_DRIVER)
	Driver getCar(@RequestParam(name = LICENSE_ID) long licenseId)
	{			
		return company.getDriver(licenseId);
	}
}
