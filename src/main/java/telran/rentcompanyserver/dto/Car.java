package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Car implements Serializable
{
	private String regNumber;
	private String color;
	private State state = State.EXCELLENT;
	private String modelName;
	private boolean inUse;
	private boolean flRemoved;
	
	public Car()
	{
		// TODO Auto-generated constructor stub
	}

	public Car(String regNumber, String color, String modelName)
	{
		super();
		this.regNumber = regNumber;
		this.color = color;
		this.modelName = modelName;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public boolean isInUse()
	{
		return inUse;
	}

	public void setInUse(boolean inUse)
	{
		this.inUse = inUse;
	}

	public boolean isFlRemoved()
	{
		return flRemoved;
	}

	public void setFlRemoved(boolean flRemoved)
	{
		this.flRemoved = flRemoved;
	}

	public String getRegNumber()
	{
		return regNumber;
	}

	public String getColor()
	{
		return color;
	}

	public String getModelName()
	{
		return modelName;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(regNumber);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(regNumber, other.regNumber);
	}

	@Override
	public String toString()
	{
		return "Car [regNumber=" + regNumber + ", color=" + color + ", state=" + state + ", modelName=" + modelName
				+ ", inUse=" + inUse + ", flRemoved=" + flRemoved + "]";
	}
	
	
}
