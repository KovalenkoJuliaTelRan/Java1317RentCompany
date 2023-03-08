package telran.rentcompanyserver.dto;

public enum CarsReturnCode
{
	OK, MODEL_EXISTS, CAR_EXISTS, DRIVER_EXISTS, NO_MODEL, NO_CAR, CAR_IN_USE, CAR_REMOVED, 
	NO_DRIVER, WRONG_RENT_DAYS
}
