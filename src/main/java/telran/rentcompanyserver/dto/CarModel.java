package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class CarModel implements Serializable
{
	private String modelName;
	private String company;
	private String country;
	private int gasTank;
	private int priceDay;
	
	public CarModel()
	{
		// TODO Auto-generated constructor stub
	}

	public CarModel(String modelName, String company, String country, int gasTank, int priceDay)
	{
		super();
		this.modelName = modelName;
		this.company = company;
		this.country = country;
		this.gasTank = gasTank;
		this.priceDay = priceDay;
	}

	public int getPriceDay()
	{
		return priceDay;
	}

	public void setPriceDay(int priceDay)
	{
		this.priceDay = priceDay;
	}

	public String getModelName()
	{
		return modelName;
	}

	public String getCompany()
	{
		return company;
	}

	public String getCountry()
	{
		return country;
	}

	public int getGasTank()
	{
		return gasTank;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(modelName);
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
		CarModel other = (CarModel) obj;
		return Objects.equals(modelName, other.modelName);
	}

	@Override
	public String toString()
	{
		return "CarModel [modelName=" + modelName + ", company=" + company + ", country=" + country + ", gasTank="
				+ gasTank + ", priceDay=" + priceDay + "]";
	}
	
}
