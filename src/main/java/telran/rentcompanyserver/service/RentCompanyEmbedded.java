package telran.rentcompanyserver.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import telran.rentcompanyserver.dto.*;
import static telran.rentcompanyserver.service.RentCompanyLocks.*;


@SuppressWarnings("serial")
public class RentCompanyEmbedded extends AbstractRentCompany implements Persistable
{
	private static final int REMOVE_THRESHOLD = 60;
	private static final int BAD_THRESHOLD = 30;
	private static final int GOOD_THRESHOLD = 10;
	
	HashMap<String, Car> cars = new HashMap<>();
	HashMap<Long, Driver> drivers = new HashMap<>();
	HashMap<String, CarModel> models = new HashMap<>();
	TreeMap<LocalDate, List<RentRecord>> records = new TreeMap<>();
	
	HashMap<String, List<Car>> modelCars = new HashMap<>();
	HashMap<Long, List<RentRecord>> driverRecords = new HashMap<>();
	HashMap<String, List<RentRecord>> carRecords =  new HashMap<>();
	
	@Value("${fileName:company.data}")
	private String fileName;
	
	@Value("${gasPrice:20}")
	private int gasPrice;
	
	@Value("${finePercent:20}")
	private int finePercent;
	
	@PostConstruct
	public void setting()
	{
		setFinePersent(finePercent);
		setGasPrice(gasPrice);
	}
	
	@PreDestroy
	public void saveToFile()
	{
		this.save(fileName);
	}

	@Override
	public CarsReturnCode addCar(Car car)
	{
		lockUnlock_addCar(true);
		try
		{
			if (!models.containsKey(car.getModelName()))
				return CarsReturnCode.NO_MODEL;
			boolean res = cars.putIfAbsent(car.getRegNumber(), car) == null;
			if (!res)
				return CarsReturnCode.CAR_EXISTS;
			addToModelCars(car);
			return CarsReturnCode.OK;
		} finally
		{
			lockUnlock_addCar(false);
		}
	}

	private void addToModelCars(Car car)
	{
		String modelName = car.getModelName();
		List<Car> list = modelCars.getOrDefault(modelName, new ArrayList<>());
		list.add(car);
		modelCars.putIfAbsent(modelName, list);
	}

	@Override
	public CarsReturnCode addModel(CarModel model)
	{
		try
		{
			lockUnlock_addModel(true);
			return models.putIfAbsent(model.getModelName(), model) == null ? CarsReturnCode.OK
					: CarsReturnCode.MODEL_EXISTS;
		} finally
		{
			lockUnlock_addModel(false);
		}
	}

	@Override
	public CarsReturnCode addDriver(Driver driver)
	{
		lockUnlock_addDriver(true);
		try
		{
			return drivers.putIfAbsent(driver.getLicenseId(), driver) == null ? CarsReturnCode.OK
					: CarsReturnCode.DRIVER_EXISTS;
		} finally
		{
			lockUnlock_addDriver(false);
		}
	}

	@Override
	public Car getCar(String regNumber)
	{
		lockUnlock_getCar(true);
		try
		{
			return cars.get(regNumber);
		} finally
		{
			lockUnlock_getCar(false);
		}
	}

	@Override
	public CarModel getCarModel(String modelName)
	{
		lockUnlock_getCarsModel(true);
		try
		{
			return models.get(modelName);
		} finally
		{
			lockUnlock_getCarsModel(false);
		}
	}

	@Override
	public Driver getDriver(long licenseId)
	{
		lockUnlock_getDriver(true);
		try
		{
			return drivers.get(licenseId);
		} finally
		{
			lockUnlock_getDriver(false);
		}
	}

	@Override
	public void save(String fileName)
	{
		lockUnlock_save(true);
		try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(
				new FileOutputStream(fileName))))
		{
			out.writeObject(this);
		} 
		catch (IOException e)
		{
			System.err.println("Error in method save " + e.getMessage());
		}
		finally
		{
			lockUnlock_save(false);
		}
	}

	public static RentCompanyEmbedded restoreFromFile(String fileName)
	{
		try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(fileName))))
		{
			return (RentCompanyEmbedded) in.readObject();
		} 
		catch (Exception e)
		{
			return new RentCompanyEmbedded();
		}
	}

	
	@Override
	public CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays)
	{
		lockUnlock_rentCar(true);
		try
		{
			Car car = getCar(regNumber);
			if (car == null)
				return CarsReturnCode.NO_CAR;
			if (car.isInUse())
				return CarsReturnCode.CAR_IN_USE;
			if (car.isFlRemoved())
				return CarsReturnCode.CAR_REMOVED;
			if (!drivers.containsKey(licenseId))
				return CarsReturnCode.NO_DRIVER;
			if (rentDays < 1)
				return CarsReturnCode.WRONG_RENT_DAYS;
			RentRecord record = new RentRecord(regNumber, licenseId, rentDate, rentDays);
			addToCarRecords(record);
			addToDriverRecords(record);
			addToRecords(record);
			car.setInUse(true);
			return CarsReturnCode.OK;
		} finally
		{
			lockUnlock_rentCar(false);
		}
	}

	private void addToRecords(RentRecord record)
	{
		LocalDate date = record.getRentDate();
		List<RentRecord> list = records.getOrDefault(date, new ArrayList<>());
		list.add(record);
		records.putIfAbsent(date, list);
	}

	private void addToDriverRecords(RentRecord record)
	{
		long licenseId = record.getLicenseId();
		List<RentRecord> list = driverRecords.getOrDefault(licenseId, new ArrayList<>());
		list.add(record);
		driverRecords.putIfAbsent(licenseId, list);
	}

	private void addToCarRecords(RentRecord record)
	{
		String regNumber = record.getRegNumber();
		List<RentRecord> list = carRecords.getOrDefault(regNumber, new ArrayList<>());
		list.add(record);
		carRecords.putIfAbsent(regNumber, list);
	}

	@Override
	public List<Car> getCarsByDriver(long licenseId)
	{
		lockUnlock_getCarsDriver(true);
		try
		{
			List<RentRecord> list = driverRecords.getOrDefault(licenseId, new ArrayList<>());
			return list.stream().map(rr -> getCar(rr.getRegNumber())).distinct().collect(Collectors.toList());
		} finally
		{
			lockUnlock_getCarsDriver(false);
		}
	}

	@Override
	public List<Driver> getDriversByCar(String regNumber)
	{
		lockUnlock_getDriversCar(true);
		try
		{
			List<RentRecord> list = carRecords.getOrDefault(regNumber, new ArrayList<>());
			return list.stream().map(rr -> getDriver(rr.getLicenseId())).distinct().toList();
		} finally
		{
			lockUnlock_getDriversCar(false);
		}
	}

	@Override
	public List<Car> getCarsByModel(String modelName)
	{
		lockUnlock_getCarsModel(true);
		try
		{
			List<Car> list = modelCars.getOrDefault(modelName, new ArrayList<>());
			return list.stream().filter(c -> !c.isFlRemoved() && !c.isInUse()).toList();
		} finally
		{
			lockUnlock_getCarsModel(false);
		}
	}

	@Override
	public List<RentRecord> getRecordsAtDates(LocalDate from, LocalDate to)
	{
		lockUnlock_getRentRecords(true);
		try
		{
			Collection<List<RentRecord>> coll = records.subMap(from, to).values();
			return coll.stream().flatMap(l -> l.stream()).toList();
		} finally
		{
			lockUnlock_getRentRecords(false);
		}
	}

	@Override
	public RemovedCarData removeCar(String regNumber)
	{
		lockUnlock_removeCar(true);
		try
		{
			Car car = getCar(regNumber);
			if (car == null)
				return null;
			car.setFlRemoved(true);
			return car.isInUse() ? new RemovedCarData(car, null) : actualCarRemove(regNumber);
		} finally
		{
			lockUnlock_removeCar(false);
		}
	}

	private RemovedCarData actualCarRemove(String regNumber)
	{
		List<RentRecord> list = carRecords.remove(regNumber);
		
		removeFromDriverRecords(list);
		removeFromRecords(list);
		removeFromModel(regNumber);
		cars.remove(regNumber);
		return new RemovedCarData(getCar(regNumber), list);
	}

	private void removeFromModel(String regNumber)
	{
		Car car = getCar(regNumber);
		modelCars.get(car.getModelName()).remove(car);
	}

	private void removeFromRecords(List<RentRecord> list)
	{
		list.forEach(rr -> records.get(rr.getRentDate()).remove(rr));
	}

	private void removeFromDriverRecords(List<RentRecord> list)
	{
		list.forEach(rr -> driverRecords.get(rr.getLicenseId()).remove(rr));
	}

	@Override
	public List<RemovedCarData> removeModel(String modelName)
	{
		lockUnlock_removeModel(true);
		try
		{
			List<Car> list = modelCars.getOrDefault(modelName, new ArrayList<>());
			return list.stream().filter(c -> !c.isFlRemoved()).map(c -> removeCar(c.getRegNumber())).toList();
		} finally
		{
			lockUnlock_removeModel(false);
		}
	}

	@Override
	public RemovedCarData returnCar(String regNumber, long licenseId, LocalDate returnDate, 
			int damages, int tankPercent)
	{
		lockUnlock_returnCar(true);
		try
		{
			RentRecord record = driverRecords.get(licenseId).stream()
					.filter(rr -> rr.getRegNumber().equals(regNumber) && rr.getReturnDate() == null).findFirst()
					.orElse(null);
			if (record == null)
				return null;
			Car car = getCar(regNumber);
			updateRecord(record, returnDate, damages, tankPercent);
			updateCar(car, damages);
			return car.isFlRemoved() || damages > REMOVE_THRESHOLD ? actualCarRemove(regNumber)
					: new RemovedCarData(car, null);
		} finally
		{
			lockUnlock_returnCar(false);
		}
	}

	private void updateCar(Car car, int damages)
	{
		car.setInUse(false);
		if(damages >= BAD_THRESHOLD)
			car.setState(State.BAD);
		else if(damages >= GOOD_THRESHOLD)
			car.setState(State.GOOD);
	}

	private void updateRecord(RentRecord record, LocalDate returnDate, int damages, int tankPercent)
	{
		record.setDamages(damages);
		record.setReturnDate(returnDate);
		record.setTankPercent(tankPercent);
		
		double cost = computeCost(getRentPrice(record.getRegNumber()), record.getRentDays(),
				getDelay(record), tankPercent, getTankVolume(record.getRegNumber()));
		record.setCost(cost);
	}

	private int getTankVolume(String regNumber)
	{
		String modelName = cars.get(regNumber).getModelName();
		return models.get(modelName).getGasTank();
	}

	private int getDelay(RentRecord record)
	{
		long realDays = ChronoUnit.DAYS.between(record.getRentDate(), record.getReturnDate());
		int delta = (int) (realDays - record.getRentDays());
		return delta <=0 ? 0 : delta;
	}

	private int getRentPrice(String regNumber)
	{
		String modelName = cars.get(regNumber).getModelName();
		return models.get(modelName).getPriceDay();
	}

	@Override
	public List<String> getMostPopularCarModels(LocalDate from, LocalDate to, int ageFrom, int ageTo)
	{
		lockUnlock_popularCars(true);
		try
		{
			List<RentRecord> list = getRecordsAtDates(from, to);
			Map<String, Long> map = list.stream().filter(rr -> isProperAge(rr, ageFrom, ageTo)).collect(
					Collectors.groupingBy(rr -> getCar(rr.getRegNumber()).getModelName(), Collectors.counting()));
			long max = Collections.max(map.values());
			System.out.println(max);
			List<String> res = new ArrayList<>();
			map.forEach((k, v) ->
			{
				if (v == max)
					res.add(k);
			});
			return res;
		} finally
		{
			lockUnlock_popularCars(false);
		}
	}

	private boolean isProperAge(RentRecord rr, int ageFrom, int ageTo)
	{
		LocalDate rentDate = rr.getRentDate();
		Driver driver = getDriver(rr.getLicenseId());
		int driverAge = rentDate.getYear() - driver.getBirthYear();
		return driverAge >= ageFrom && driverAge < ageTo;
	}

	@Override
	public List<String> getMostProfitableCarModels(LocalDate from, LocalDate to)
	{
		lockUnlock_popularCars(false);
		try
		{
			Collection<List<RentRecord>> coll = records.subMap(from, to).values();
			if (coll == null)
				return new ArrayList<>();
			Map<String, Double> map = coll.stream().flatMap(l -> l.stream()).collect(Collectors.groupingBy(
					rr -> getCar(rr.getRegNumber()).getModelName(), Collectors.summingDouble(RentRecord::getCost)));
			double max = map.values().stream().mapToDouble(c -> c).max().getAsDouble();
			List<String> res = new ArrayList<>();
			map.forEach((k, v) ->
			{
				if (v == max)
					res.add(k);
			});
			return res;
		} finally
		{
			lockUnlock_popularCars(false);
		}
	}

	@Override
	public List<Driver> getMostActiveDrivers()
	{
		lockUnlock_activeDrivers(false);
		try
		{
			long max = 0;
			for (List<RentRecord> l : driverRecords.values())
			{
				max = l.size() > max ? l.size() : max;
			}
			long maxFinal = max;
			List<Driver> res = new ArrayList<>();
			driverRecords.forEach((k, v) ->
			{
				if (v.size() == maxFinal)
					res.add(getDriver(k));
			});
			return res;
		} finally
		{
			lockUnlock_activeDrivers(false);
		}
	}

	@Override
	public List<String> getModelNames()
	{
		lockUnlock_getModelNames(true);
		try
		{
			return new ArrayList<>(models.keySet());
		} finally
		{
			lockUnlock_getModelNames(false);
		}
	}
}
