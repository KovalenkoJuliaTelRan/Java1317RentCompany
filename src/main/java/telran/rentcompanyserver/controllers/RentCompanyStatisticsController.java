package telran.rentcompanyserver.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import telran.rentcompanyserver.dto.Driver;
import telran.rentcompanyserver.dto.RentRecord;
import telran.rentcompanyserver.service.IRentCompany;
import static telran.rentcompanyserver.api.ApiConstants.*;

//String GET_MOST_ACTIVE_DRIVERS = "/drivers/active";
//String GET_MOST_POPULAR_MODELS = "/models/popular";
//String GET_MOST_PROFITABLE_MODELS = "/models/profitable";
//String GET_RECORDS = "/records";

@RestController
public class RentCompanyStatisticsController
{
	@Autowired
	IRentCompany company;
	
	@GetMapping(value = GET_MOST_POPULAR_MODELS)
	List<String> getMostPopularCarModels(@RequestParam(name = "date_from")
										@DateTimeFormat(iso = ISO.DATE) String from, 
										@RequestParam(name = "date_to")
										@DateTimeFormat(iso = ISO.DATE) String to, 
										@RequestParam(name = "age_from") int ageFrom, 
										@RequestParam(name = "age_to") int ageTo)
	{
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		return company.getMostPopularCarModels(fromDate, toDate, ageFrom, ageTo);
	}
	
	@DateTimeFormat(iso = ISO.DATE)
	@GetMapping(value = GET_MOST_PROFITABLE_MODELS)
	List<String> getMostProfitableModels(@RequestParam(name = DATE_FROM) String dateFrom,
			@RequestParam(name = DATE_TO) String dateTo)
	{
		LocalDate from = LocalDate.parse(dateFrom);
		LocalDate to = LocalDate.parse(dateTo);
		return company.getMostProfitableCarModels(from, to);
	}

	@GetMapping(value = GET_MOST_ACTIVE_DRIVERS)
	List<Driver> getMostActiveDrivers()
	{
		return company.getMostActiveDrivers();
	}

	@DateTimeFormat(iso = ISO.DATE)
	@GetMapping(value = GET_RECORDS)
	@ApiResponse(description = "")
	List<RentRecord> getRecords(@RequestParam(name = DATE_FROM) String dateFrom,
			@RequestParam(name = DATE_TO) String dateTo)
	{
		LocalDate from = LocalDate.parse(dateFrom);
		LocalDate to = LocalDate.parse(dateTo);
		return company.getRecordsAtDates(from, to);
	}
	
}
