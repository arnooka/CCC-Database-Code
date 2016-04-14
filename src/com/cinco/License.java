package com.cinco;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class License extends Product{

		private Double fee;
		private Double annualCost;
		private Double years;

		public License(String productCode, String productName, Double fee, Double annualCost, String type){
			super(productName, productCode, type);
			this.fee = fee;
			this.annualCost = annualCost;
		}
		
		//Calculates the number of days the license has been used
		public static Double daysBetween(String date1, String date2){
			String start[] = date1.split("-");
			String Year1 = start[0].trim();
			String Month1 = start[1].trim();
			String Day1 = start[2].trim();
			int startYear = Integer.parseInt(Year1);
			int startMonth = Integer.parseInt(Month1);
			int startDay = Integer.parseInt(Day1);
	
			String end[] = date2.split("-");
			String Year2 = end[0].trim();
			String Month2 = end[1].trim();
			String Day2 = end[2].trim();
			int endYear = Integer.parseInt(Year2);
			int endMonth = Integer.parseInt(Month2);
			int endDay = Integer.parseInt(Day2);
	
			//Add to its own method in Licenses
			DateTime startDate = new DateTime(startYear, startMonth, startDay, 0, 0);
			DateTime endDate = new DateTime(endYear, endMonth, endDay, 0, 0);
			double days = Days.daysBetween(startDate, endDate).getDays();
			
			//Can do math here for removing leap year days (February)
			
			return days;
		}

		public Double getFee() {
			return fee;
		}

		public Double getAnnualCost() {
			return annualCost;
		}

		public void setFee(Double fee) {
			this.fee = fee;
		}

		public void setAnnualCost(Double annualCost) {
			this.annualCost = annualCost;
		}
		
		public Double getYears() {
			return years;
		}
		
		public void setYears(Double years) {
			this.years = years;
		}
		
}
