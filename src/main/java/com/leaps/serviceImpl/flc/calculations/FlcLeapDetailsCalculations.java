package com.leaps.serviceImpl.flc.calculations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaps.entity.flc.FlcCoverDetails;
import com.leaps.entity.flc.FlcFundDetailsPas;
import com.leaps.entity.flc.FlcLeapDetails;
import com.leaps.entity.flc.FlcLeapFundDetails;
import com.leaps.entity.flc.FlcPolicyDetails;
import com.leaps.entity.flc.FlcTransactionDetails;
import com.leaps.entity.master.LeapNav;
import com.leaps.entity.master.MortalityFlagMaster;
import com.leaps.entity.master.StamDutyMaster;
import com.leaps.repository.flc.FlcCoverDetailsRepository;
import com.leaps.repository.flc.FlcFundDetailsPasRepository;
import com.leaps.repository.flc.FlcLeapFundDetailsRepository;
import com.leaps.repository.flc.FlcPolicyDetailsRepository;
import com.leaps.repository.flc.FlcTransactionDetailsRepository;
import com.leaps.repository.master.LeapNavRepository;
import com.leaps.repository.master.MedicalDetailsRepository;
import com.leaps.repository.master.MortalityFlagMasterRepository;
import com.leaps.repository.master.MortalityRatesRepository;
import com.leaps.repository.master.StamDutyMasterRepository;

@Service
public abstract class FlcLeapDetailsCalculations {

	@Autowired
	private FlcPolicyDetailsRepository flcPolicyDetailsRepository;

	@Autowired
	private FlcCoverDetailsRepository flcCoverDetailsRepository;
	@Autowired
	private StamDutyMasterRepository stamDutyMasterRepository;
	
	@Autowired
	private FlcTransactionDetailsRepository flcTransactionDetailsRepository;
	@Autowired
	private MortalityFlagMasterRepository mortalityFlagMasterRepository;

	@Autowired
	private MortalityRatesRepository mortalityRatesRepository;
	@Autowired
	private FlcFundDetailsPasRepository flcFundDetailsPasRepository;

	@Autowired
	private FlcLeapFundDetailsRepository flcLeapFundDetailsRepository;

	@Autowired
	private LeapNavRepository leapNavRepository;

	@Autowired
	private MedicalDetailsRepository medicalDetailsRepository;

	// Gets Month
	public String getMonth(String req) throws ParseException {

		SimpleDateFormat input = new SimpleDateFormat("yyyyMMdd");

		Date reqDate = input.parse(req);

		SimpleDateFormat month = new SimpleDateFormat("MMMM");

		String reqMon = month.format(reqDate);

		System.out.println("Month: " + reqMon);

		return reqMon;
	}

	// Calculate Total Premium
	public Double calculateTotalPremium(String premToDate, String docDate, Double installmentPremium, Integer billFreq)
			throws ParseException {

		Long months = getDiffInMonth(premToDate, docDate);

		Double val = (months.doubleValue() / billFreq);

		Double Premium = installmentPremium * val;

		Double totalPremium = Premium;

		return totalPremium;
	}

	// Diff in Months
	public Long getDiffInMonth(String startDate, String endDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
		Date startDate1 = inputFormat.parse(startDate);
		Date endDate1 = inputFormat.parse(endDate);
		String StartDate = sdf.format(startDate1);
		String EndDate = sdf.format(endDate1);

		Long month = ChronoUnit.MONTHS.between(YearMonth.from(LocalDate.parse(StartDate)),
				YearMonth.from(LocalDate.parse(EndDate)));

		return month;
	}

	// Calculate StamDuty
	public Double calculateStampDuty(Long policyNo) {
		double stamDuty = 0;
		List<FlcCoverDetails> covers = flcCoverDetailsRepository.getAllByPolicyNo(policyNo);
		for (FlcCoverDetails cover : covers) {

			StamDutyMaster stamDutyMaster = stamDutyMasterRepository.getByUniqueNo(cover.getUinNumber());
			Double sum = cover.getSumAssured();
			double rate = stamDutyMaster.getSdRate();
			double stamp = sum * (rate / 1000);
			double stampWithoutGst = stamp * (stamDutyMaster.getGstRate() / 100);

			double dutyWithGst = (stamp + stampWithoutGst);

			System.out.println("***** SD rate ***** " + rate);
			System.out.println("***** Stamp ***** " + stamp);
			System.out.println("***** Stamp without gst ***** " + stampWithoutGst);
			System.out.println("***** Stamp with gst ***** " + dutyWithGst);

			stamDuty = stamDuty + dutyWithGst;
		}

		BigDecimal bd1 = new BigDecimal(stamDuty).setScale(2, RoundingMode.HALF_UP);
		return bd1.doubleValue();
	}

	// Calculate Penal Interest
	public Double calculatePenalInterest(Long policyNo, FlcLeapDetails flcLeapDetails) throws ParseException {
		FlcTransactionDetails flcTransactionDetails = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNo);
		String approv = flcTransactionDetails.getFlcApprovalDate();
		String req = flcTransactionDetails.getFlcReqDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date approvDate = sdf.parse(approv);
		Date reqDate = sdf.parse(req);
		long timeDiff = Math.abs(approvDate.getTime() - reqDate.getTime());
		long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

		Double penalInt = 0.0;

		if (daysDiff > 15) {

			double val1 = (0.09 / 365);
			double val2 = val1 * daysDiff;

			System.out.println("************ Net Payble *************  " + flcLeapDetails.getNetPayable());

			Double penal = (flcLeapDetails.getNetPayable() * val2);

			penalInt = penal;

		} else if (daysDiff <= 15) {
			Double penal = 0.0;
			penalInt = penal;
		}
		return penalInt;
	}

	// Calculate Mortality Rates Non Ulip
	public Double calculateNonUlipMortalityRates(Long policyNum) throws ParseException {
		FlcTransactionDetails trans = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNum);

		Integer age = flcPolicyDetailsRepository.getByAge(policyNum);

		List<FlcCoverDetails> covers = flcCoverDetailsRepository.getAllByPolicyNo(policyNum);

		double val = 0;

		for (FlcCoverDetails coverPas : covers) {

			MortalityFlagMaster mortFlagMaster = mortalityFlagMasterRepository.getByUniqueNo(coverPas.getUinNumber());

			if (mortFlagMaster.getMortFlag().equalsIgnoreCase("Yes")) {

				Float mortalityRates = mortalityRatesRepository.findByCCCT(coverPas.getUinNumber(), age,
						coverPas.getPremCessTerm());

				if (mortalityRates != null) {

					String flcReqDate = trans.getFlcReqDate();

					String coverStartDate = coverPas.getRiskComDate();

					System.out.println("************** Req Date *************** " + flcReqDate);
					System.out.println("************** Risk Com date *************** " + coverStartDate);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

					Date d1 = sdf.parse(flcReqDate);

					Date d2 = sdf.parse(coverStartDate);

					long diffInTime = d1.getTime() - d2.getTime();

					long difference_In_Days = (diffInTime / (1000 * 60 * 60 * 24)) % 365;

					double difference;

					if (difference_In_Days < 0) {

						difference = (float) (difference_In_Days * -1);

					} else {
						difference = (float) (difference_In_Days);
					}

					double rates = ((mortalityRates * coverPas.getSumAssured()) / 1000f);
					System.out.println("************** Rates *************** " + rates);
					System.out.println("************** Difference in days *************** " + difference);

					double diffInDaysplusOne = difference + 1;
					double dateDiff = diffInDaysplusOne / 365;

					System.out.println(
							"** Difference in days +1 ** " + diffInDaysplusOne + "   *** /365 ***  " + dateDiff);

					double mortalityCharge = rates * dateDiff;
//					Long mortalityCharges = (long) mortalityCharge;

					System.out.println("************** MC without gst *************** " + mortalityCharge);

					double gstCharge = (mortalityCharge * (mortFlagMaster.getGstRate() / 100));

					System.out.println("************** Gst Charge *************** " + gstCharge);

					double mortChargeWithGst = (mortalityCharge + gstCharge);

					System.out.println("************** MC with gst *************** " + mortChargeWithGst);
					val = val + mortChargeWithGst;

				} else {
					val = -1;
					break;
				}

			}

		}

		BigDecimal bd1 = new BigDecimal(val).setScale(2, RoundingMode.HALF_UP);

		return bd1.doubleValue();

	}

	// Calculate Mortality Rates Ulip
	public Double calculateUlipMortalityRates(Long policyNum) throws ParseException {
		FlcTransactionDetails trans = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNum);

		Integer age = flcPolicyDetailsRepository.getByAge(policyNum);

		List<FlcCoverDetails> covers = flcCoverDetailsRepository.getAllByPolicyNo(policyNum);

		double val = 0;
		double monthMortAmount = 0.0;

		for (FlcCoverDetails coverPas : covers) {

			MortalityFlagMaster mortFlagMaster = mortalityFlagMasterRepository.getByUniqueNo(coverPas.getUinNumber());

			if (mortFlagMaster.getMortFlag().equalsIgnoreCase("Yes")) {

				Float mortalityRates = mortalityRatesRepository.findByCCCT(coverPas.getUinNumber(), age,
						coverPas.getPremCessTerm());

				System.out.println("***************** Mortality rate************** " + mortalityRates);

				if (mortalityRates != null) {

					String flcReqDate = trans.getFlcReqDate();

					System.out.println("****** ulip req date **********" + flcReqDate);

					String coverStartDate = coverPas.getRiskComDate();
					System.out.println("****** ulip Risk date **********" + coverStartDate);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

					Date d1 = sdf.parse(flcReqDate);

					Date d2 = sdf.parse(coverStartDate);

					long diffInTime = d1.getTime() - d2.getTime();

					long difference_In_Days = (diffInTime / (1000 * 60 * 60 * 24)) % 365;

					double differenceInDays;

					if (difference_In_Days < 0) {

						differenceInDays = (float) (difference_In_Days * -1);

					} else {
						differenceInDays = (float) (difference_In_Days);
					}

					double ulipRates = ((mortalityRates * coverPas.getSumAssured()) / 1000f);
					double diffInDaysplusOne = differenceInDays + 1;

					double ulipMortalityChargesRecoverd = (ulipRates / 365) * diffInDaysplusOne;

					System.out.println("********* Mortality Rates ********* " + mortalityRates);
					System.out.println("********* Sum Assured ********* " + coverPas.getSumAssured());
					System.out.println("********* diff In Days ********* " + diffInDaysplusOne);
					System.out.println("********* Ulip Rates ********* " + ulipRates);
					System.out
							.println("********* Mortality Charge Recovered ********* " + ulipMortalityChargesRecoverd);

					double MortAmountPerDay = ((mortalityRates * coverPas.getSumAssured()) / (365 * 1000f));

					String monthForFlcReqdate = getMonth(trans.getFlcReqDate());

					System.out.println("********* Flc Req Month ********* " + monthForFlcReqdate);

					if (monthForFlcReqdate.equalsIgnoreCase("january") || monthForFlcReqdate.equalsIgnoreCase("march")
							|| monthForFlcReqdate.equalsIgnoreCase("may") || monthForFlcReqdate.equalsIgnoreCase("july")
							|| monthForFlcReqdate.equalsIgnoreCase("august")
							|| monthForFlcReqdate.equalsIgnoreCase("october")
							|| monthForFlcReqdate.equalsIgnoreCase("december")) {
						System.out.println("******* inside month if ***********");
						monthMortAmount = MortAmountPerDay * 31;
					}

					if (monthForFlcReqdate.equalsIgnoreCase("april") || monthForFlcReqdate.equalsIgnoreCase("june")
							|| monthForFlcReqdate.equalsIgnoreCase("september")
							|| monthForFlcReqdate.equalsIgnoreCase("november")) {
						monthMortAmount = MortAmountPerDay * 30;
					}

					if (monthForFlcReqdate.equalsIgnoreCase("february")) {
						monthMortAmount = MortAmountPerDay * 28;
					}

					double mortAmountRefund = monthMortAmount - ulipMortalityChargesRecoverd;

					System.out.println("********* Per Day Mort Charges ********* " + MortAmountPerDay);
					System.out.println("********* Month Mort Charges ********* " + monthMortAmount);
					System.out.println("********* Amount Refund ********* " + mortAmountRefund);

					double gstCharge = (mortAmountRefund * (mortFlagMaster.getGstRate() / 100));

					System.out.println("********* Gst Charge ********* " + gstCharge);

					double mortChargeWithGst = (mortAmountRefund + gstCharge);

					System.out.println("********* Amount Refund With Gst ********* " + mortChargeWithGst);

					val = val + mortChargeWithGst;

				} else {
					val = -1;
					break;
				}

			}

		}

		BigDecimal bd1 = new BigDecimal(val).setScale(2, RoundingMode.HALF_UP);

		return bd1.doubleValue();

	}

	// Calculate Fund Value
	public Double calculateFundValue(Long tranno) {
		FlcTransactionDetails trans = flcTransactionDetailsRepository.getInitiatedTransaction(tranno);
		List<FlcFundDetailsPas> fdss = flcFundDetailsPasRepository.getallByPolicy(trans.getFlcPolicyNo());
		double fundval = 0f;
		for (FlcFundDetailsPas fds : fdss) {
			System.out.println("******** Fund Code ***********" + fds.getFundCode());
			System.out.println("******** Nav Date ***********" + fds.getNavDate());
			LeapNav nav = leapNavRepository.getByFundCode(fds.getFundCode(), fds.getNavDate());
			double rate = 0f;
			rate = fds.getUnits() * nav.getRate();

			BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
			double value = bd.doubleValue();
			System.out.println("******** Rate ***********" + nav.getRate());
			System.out.println("******** Unit ***********" + fds.getUnits());
			System.out.println("******** Value ***********" + value);
			fundval = fundval + value;
			FlcLeapFundDetails lfds = new FlcLeapFundDetails();
			lfds.setValidFlag(1);
			lfds.setCompanyId(trans.getCompanyId());
			lfds.setPolicyNo(trans.getFlcPolicyNo());
			lfds.setLeapFundCode(fds.getFundCode());
			lfds.setLeapFundName(fds.getFundName());
			lfds.setLeapNavDate(trans.getEffDate());
			lfds.setLeapUnits(fds.getUnits());
			lfds.setLeapRateApp(nav.getRate());
			lfds.setLeapFundValue(value);
			flcLeapFundDetailsRepository.save(lfds);
			fundValueValidation(fds, lfds);
		}
		BigDecimal bd1 = new BigDecimal(fundval).setScale(2, RoundingMode.HALF_UP);
		System.out.println(
				"********Overall Value ***********" + new BigDecimal(fundval).setScale(2, RoundingMode.HALF_UP));
		return bd1.doubleValue();
	}

	// Validates the Fund Value
	public void fundValueValidation(FlcFundDetailsPas fds, FlcLeapFundDetails lfds) {

		String flag = "";

		if (!fds.getNavDate().equals(lfds.getLeapNavDate())) {
			flag = "Error";

		}

		if (!fds.getRateApp().equals(lfds.getLeapRateApp())) {
			flag = "Error";

		}

		if (!fds.getValue().equals(lfds.getLeapFundValue())) {
			flag = "Error";

		}

		if (flag != "Error") {
			flag = "Clear";
		}

		lfds.setStatus(flag);
		flcLeapFundDetailsRepository.save(lfds);
	}

	// Calculate Medical Fee Rate
	public Double calculateMFRate(Long policyNo) {

		FlcPolicyDetails policyPas = flcPolicyDetailsRepository.getActiveByPolicyNo(policyNo);
		FlcTransactionDetails trans = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNo);

		Double mfRate;

		if (policyPas.getMedicalFlag().equalsIgnoreCase("Yes")) {

			System.out.println("Inside medical flag data ");
			Double medicalfeeRate = medicalDetailsRepository.getMfRate(trans.getMedicalCategory(),
					trans.getMedicalCenter(), trans.getMedicatTpaCode());

			if (medicalfeeRate != null) {
				mfRate = medicalfeeRate;
			} else {
				mfRate = -1.0;

			}

		} else {
			mfRate = 0.0;
		}
		return mfRate;
	}

}
