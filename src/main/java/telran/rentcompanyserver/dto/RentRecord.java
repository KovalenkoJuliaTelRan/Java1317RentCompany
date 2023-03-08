package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@SuppressWarnings("serial")
public class RentRecord implements Serializable
{
	private String regNumber;
	private long licenseId;
	private LocalDate rentDate;
	private int rentDays;
	
	private LocalDate returnDate;
	private int damages;
	private int tankPercent;
	private double cost;
	
	
	public RentRecord()
	{
		// TODO Auto-generated constructor stub
	}

	public RentRecord(String regNumber, long licenseId, LocalDate rentDate, int rentDays)
	{
		super();
		this.regNumber = regNumber;
		this.licenseId = licenseId;
		this.rentDate = rentDate;
		this.rentDays = rentDays;
	}

	public LocalDate getReturnDate()
	{
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate)
	{
		this.returnDate = returnDate;
	}

	public int getDamages()
	{
		return damages;
	}

	public void setDamages(int damages)
	{
		this.damages = damages;
	}

	public int getTankPercent()
	{
		return tankPercent;
	}

	public void setTankPercent(int tankPercent)
	{
		this.tankPercent = tankPercent;
	}

	public double getCost()
	{
		return cost;
	}

	public void setCost(double cost)
	{
		this.cost = cost;
	}

	public String getRegNumber()
	{
		return regNumber;
	}

	public long getLicenseId()
	{
		return licenseId;
	}

	public LocalDate getRentDate()
	{
		return rentDate;
	}

	public int getRentDays()
	{
		return rentDays;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(cost, damages, licenseId, regNumber, rentDate, rentDays, returnDate, tankPercent);
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
		RentRecord other = (RentRecord) obj;
		return Double.doubleToLongBits(cost) == Double.doubleToLongBits(other.cost) && damages == other.damages
				&& licenseId == other.licenseId && Objects.equals(regNumber, other.regNumber)
				&& Objects.equals(rentDate, other.rentDate) && rentDays == other.rentDays
				&& Objects.equals(returnDate, other.returnDate) && tankPercent == other.tankPercent;
	}

	@Override
	public String toString()
	{
		return "RentRecord [regNumber=" + regNumber + ", licenseId=" + licenseId + ", rentDate=" + rentDate
				+ ", returnDate=" + returnDate + ", rentDays=" + rentDays + ", damages=" + damages + ", tankPercent="
				+ tankPercent + ", cost=" + cost + "]";
	}
	
	
}
