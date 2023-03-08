package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class ReturnCarData implements Serializable
{
	String regNumber;
	long licenseId;
	LocalDate returnDate;
	int damage;
	int tankPercent;
	
	public ReturnCarData()
	{
		// TODO Auto-generated constructor stub
	}

	public ReturnCarData(String regNumber, long licenseId, LocalDate returnDate, int damage, int tankPercent)
	{
		super();
		this.regNumber = regNumber;
		this.licenseId = licenseId;
		this.returnDate = returnDate;
		this.damage = damage;
		this.tankPercent = tankPercent;
	}

	public String getRegNumber()
	{
		return regNumber;
	}

	public long getLicenseId()
	{
		return licenseId;
	}

	public LocalDate getReturnDate()
	{
		return returnDate;
	}

	public int getDamage()
	{
		return damage;
	}

	public int getTankPercent()
	{
		return tankPercent;
	}
	
	
}
