package telran.rentcompanyserver.dto;

import java.io.Serializable;
import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("serial")
public class Driver implements Serializable
{
	@Min(0)
	private long licenseId;
	
	@Size(min = 3, max = 30, message = "Name of driver is too short or to long")
	@Pattern(regexp = "[a-zA-Z]+", message = "Name has contains characters only")
	private String name;
	
	@Max(2022)
	private int birthYear;
	private String phone;
	
	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public long getLicenseId()
	{
		return licenseId;
	}

	public String getName()
	{
		return name;
	}

	public int getBirthYear()
	{
		return birthYear;
	}

	public Driver()
	{
		// TODO Auto-generated constructor stub
	}

	public Driver(long licenseId, String name, int birthYear, String phone)
	{
		super();
		this.licenseId = licenseId;
		this.name = name;
		this.birthYear = birthYear;
		this.phone = phone;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(licenseId);
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
		Driver other = (Driver) obj;
		return licenseId == other.licenseId;
	}

	@Override
	public String toString()
	{
		return "Driver [licenseId=" + licenseId + ", name=" + name + ", birthYear=" + birthYear + ", phone=" + phone
				+ "]";
	}
	
	
}
