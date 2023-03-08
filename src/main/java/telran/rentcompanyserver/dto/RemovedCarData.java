package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RemovedCarData implements Serializable
{
	private Car car;
	private List<RentRecord> removedRecords;
	
	public RemovedCarData()
	{
		// TODO Auto-generated constructor stub
	}

	public RemovedCarData(Car car, List<RentRecord> removedRecords)
	{
		super();
		this.car = car;
		this.removedRecords = removedRecords;
	}

	public Car getCar()
	{
		return car;
	}

	public List<RentRecord> getRemovedRecords()
	{
		return removedRecords;
	}

	@Override
	public String toString()
	{
		return "RemovedCarData [car=" + car + ", removedRecords=" + removedRecords + "]";
	}
	
	
} 
