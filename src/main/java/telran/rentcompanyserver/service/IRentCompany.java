package telran.rentcompanyserver.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import telran.rentcompanyserver.dto.*;


public interface IRentCompany extends Serializable
{
//	Sprint1
	int getGasPrice();
	void setGasPrice(int price);
	int getFinePersent();
	void setFinePersent(int persent);
	CarsReturnCode addCar(Car car);
	CarsReturnCode addModel(CarModel model);
	CarsReturnCode addDriver(Driver driver);
	Car getCar(String regNumber);
	CarModel getCarModel(String modelName);
	Driver getDriver(long licenseId);
	
//	Sprint2
	CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays);
	List<Car> getCarsByDriver(long licenseId);
	List<Driver> getDriversByCar(String regNumber);
	List<Car> getCarsByModel(String modelName);
	List<RentRecord> getRecordsAtDates(LocalDate from, LocalDate to);
	
//	Sprint3
	RemovedCarData removeCar(String regNumber);
	List<RemovedCarData> removeModel(String modelName);
	RemovedCarData returnCar(String regNumber, long licenseId, LocalDate returnDate, int damages,
			int tankPercent);
	
//	Sprint4
	List<String> getMostPopularCarModels(LocalDate from, LocalDate to, int ageFrom, int ageTo);
	List<String> getMostProfitableCarModels(LocalDate from, LocalDate to);
	List<Driver> getMostActiveDrivers();
	
//	Sprint5
	List<String> getModelNames();
}
