package telran.rentcompanyserver.service;


@SuppressWarnings("serial")
public abstract class AbstractRentCompany implements IRentCompany
{
	protected int gasPrice;
	protected int finePersent;
	
	@Override
	public int getGasPrice()
	{
		return gasPrice;
	}

	@Override
	public void setGasPrice(int price)
	{
		this.gasPrice = price;
	}

	@Override
	public int getFinePersent()
	{
		return finePersent;
	}

	@Override
	public void setFinePersent(int persent)
	{
		this.finePersent = persent;
	}
	
	public double computeCost(int rentPrice, int rentDays, int delay, int tankPercent, int tankVolume)
	{
		double cost = rentPrice*rentDays;
		
		if(delay > 0)
			cost += ((1 + (double)finePersent / 100) * rentPrice) * delay;
		
		if(tankPercent < 100)
			cost += tankVolume * ((double)(100 - tankPercent) / 100) * gasPrice;
		
		return cost;
	}

	
}
