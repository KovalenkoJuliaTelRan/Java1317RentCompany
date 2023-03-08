package telran.rentcompanyserver.service;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RentCompanyLocks
{
	static final ReadWriteLock carsLock = new ReentrantReadWriteLock();
	static final ReadWriteLock driversLock = new ReentrantReadWriteLock();
	static final ReadWriteLock modelsLock = new ReentrantReadWriteLock();
	static final ReadWriteLock recordsLock = new ReentrantReadWriteLock();
	
	static Lock[][] locks = new Lock[2][4];
	
	static final int WRITE_INDEX = 0;
	static final int READ_INDEX = 1;
	
	static final int CARS_INDEX = 0;
	static final int DRIVERS_INDEX = 1;
	static final int MODELS_INDEX = 2;
	static final int RECORDS_INDEX = 3;
	
	static final boolean flagLock = true;
	
	static
	{
		ReadWriteLock[] temp = {carsLock, driversLock, modelsLock, recordsLock};
		for(int i=0; i<temp.length; i++)
		{
			locks[WRITE_INDEX][i] = temp[i].writeLock();
			locks[READ_INDEX][i] = temp[i].readLock();
			
		}
	}
	
	private static void lockUnlock(boolean flagLock, int typeLock, int ...indexes)
	{
		if(flagLock)
			lock(typeLock, indexes);
		else
			unlock(typeLock, indexes);
	}

	private static void unlock(int typeLock, int[] indexes)
	{
		Arrays.sort(indexes);
		for(int index: indexes)
		{
			locks[typeLock][index].unlock();
		}
	}

	private static void lock(int typeLock, int[] indexes)
	{
		Arrays.sort(indexes);
		for(int index: indexes)
		{
			locks[typeLock][index].lock();
		}
	}
	
	public static void lockUnlock_addModel(boolean flagLock)
	{
		lockUnlock(flagLock, WRITE_INDEX, MODELS_INDEX);
	}
	
	public static void lockUnlock_save(boolean flagLock)
	{
		lockUnlock(flagLock, READ_INDEX, 3,2,1,0);
	}
	
	public static void lockUnlock_rentCar(boolean flagLock)
	{
		lockUnlock(flagLock, READ_INDEX, DRIVERS_INDEX);
		lockUnlock(flagLock, WRITE_INDEX, CARS_INDEX, RECORDS_INDEX);
	}
	
	public static void lockUnlock_getModel(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, MODELS_INDEX);
	}

	public static void lockUnlock_addCar(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, MODELS_INDEX);
		lockUnlock(flock, WRITE_INDEX, CARS_INDEX);
	}

	public static void lockUnloack_addModelCars(boolean flock)
	{
		lockUnlock(flock, WRITE_INDEX, MODELS_INDEX);
	}

	public static void lockUnlock_addDriver(boolean flock)
	{
		lockUnlock(flock, WRITE_INDEX, DRIVERS_INDEX);
	}

	public static void lockUnlock_getDriver(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, DRIVERS_INDEX);
	}

	public static void lockUnlock_getCarsDriver(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_getCar(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, CARS_INDEX);
	}

	public static void lockUnlock_getDriversCar(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_getCarsModel(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, CARS_INDEX, MODELS_INDEX);
	}

	public static void lockUnlock_getRentRecords(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_removeCar(boolean flock)
	{
		lockUnlock(flock, WRITE_INDEX, CARS_INDEX, MODELS_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_removeModel(boolean flock)
	{
		lockUnlock(flock, WRITE_INDEX, CARS_INDEX, MODELS_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_returnCar(boolean flock)
	{
		lockUnlock(flock, WRITE_INDEX, CARS_INDEX, MODELS_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_activeDrivers(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, DRIVERS_INDEX, RECORDS_INDEX);
	}

	public static void lockUnlock_getModelNames(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, CARS_INDEX, MODELS_INDEX);
	}

	public static void lockUnlock_popularCars(boolean flock)
	{
		lockUnlock(flock, READ_INDEX, CARS_INDEX, MODELS_INDEX, RECORDS_INDEX);
	}
}