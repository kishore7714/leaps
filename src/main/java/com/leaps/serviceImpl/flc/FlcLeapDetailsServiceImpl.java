package com.leaps.serviceImpl.flc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.flc.FlcLeapDetailsDto;
import com.leaps.entity.flc.FlcClientDetails;
import com.leaps.entity.flc.FlcCoverDetails;
import com.leaps.entity.flc.FlcLeapDetails;
import com.leaps.entity.flc.FlcPolicyDetails;
import com.leaps.entity.flc.FlcTransactionDetails;
import com.leaps.entity.master.UinMaster;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcClientDetailsRepository;
import com.leaps.repository.flc.FlcCoverDetailsRepository;
import com.leaps.repository.flc.FlcLeapDetailsRepository;
import com.leaps.repository.flc.FlcPolicyDetailsRepository;
import com.leaps.repository.flc.FlcTransactionDetailsRepository;
import com.leaps.repository.master.UinMasterRepository;
import com.leaps.responses.flc.FlcLeapDetailsResponse;
import com.leaps.service.flc.FlcLeapDetailsService;
import com.leaps.serviceImpl.flc.calculations.FlcLeapDetailsCalculations;

@Service
public class FlcLeapDetailsServiceImpl implements FlcLeapDetailsService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcLeapDetailsRepository flcLeapDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	@Autowired
	private FlcLeapDetailsCalculations flcCalculations;

	@Autowired
	private FlcPolicyDetailsRepository flcPolicyDetailsRepository;

	@Autowired
	private FlcTransactionDetailsRepository flcTransactionDetailsRepository;

	@Autowired
	private FlcCoverDetailsRepository flcCoverDetailsRepository;

	@Autowired
	private FlcClientDetailsRepository flcClientDetailsRepository;
	
	@Autowired
	private UinMasterRepository uinMasterRepository;

	// Entity to Dto using Function

	public Function<FlcLeapDetails, FlcLeapDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcLeapDetails, FlcLeapDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcLeapDetailsDto dto = mapper.map(entity, FlcLeapDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcLeapDetailsDto, FlcLeapDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcLeapDetails entity = mapper.map(dto, FlcLeapDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcLeapDetailsDto> getall() {
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsRepository.getallActive();
		List<FlcLeapDetailsDto> listOfFlcLeapDetailsDto = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());
		return listOfFlcLeapDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcLeapDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcLeapDetails> flcLeapDetailsPage = flcLeapDetailsRepository.getallActivePagination(pageable);

		// get content
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsPage.getContent();

		List<FlcLeapDetailsDto> content = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcLeapDetailsResponse flcLeapDetailsResponse = new FlcLeapDetailsResponse();
		flcLeapDetailsResponse.setContent(content);
		flcLeapDetailsResponse.setPageNo(flcLeapDetailsPage.getNumber());
		flcLeapDetailsResponse.setPageSize(flcLeapDetailsPage.getSize());
		flcLeapDetailsResponse.setTotalElements(flcLeapDetailsPage.getTotalElements());
		flcLeapDetailsResponse.setTotalPages(flcLeapDetailsPage.getTotalPages());
		flcLeapDetailsResponse.setLast(flcLeapDetailsPage.isLast());

		return flcLeapDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcLeapDetailsDto getbyid(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		FlcLeapDetailsDto flcLeapDetailsDto = entityToDto.apply(flcLeapDetails);
		return flcLeapDetailsDto;
	}

	// Add
	@Override
	public String add(FlcLeapDetailsDto dto) {
		FlcLeapDetails flcLeapDetails = dtoToEntity.apply(dto);
		flcLeapDetails.setValidFlag(1);
		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcLeapDetailsDto dto) {
		FlcLeapDetails entity = dtoToEntity.apply(dto);

		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcLeapDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getFlcPolicyNo())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setFlcPolicyNo(entity.getFlcPolicyNo()));
		Optional.ofNullable(entity.getTranDate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTranDate(entity.getTranDate()));
		Optional.ofNullable(entity.getTranNo())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTranNo(entity.getTranNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getTotalPremium())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setTotalPremium(entity.getTotalPremium()));
		Optional.ofNullable(entity.getAvalSuspense())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setAvalSuspense(entity.getAvalSuspense()));
		Optional.ofNullable(entity.getPenalInterest())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPenalInterest(entity.getPenalInterest()));
		Optional.ofNullable(entity.getMedicalFee())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setMedicalFee(entity.getMedicalFee()));
		Optional.ofNullable(entity.getStampDuty())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setStampDuty(entity.getStampDuty()));
		Optional.ofNullable(entity.getMortCharge())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setMortCharge(entity.getMortCharge()));
		Optional.ofNullable(entity.getGrossPayable())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setGrossPayable(entity.getGrossPayable()));
		Optional.ofNullable(entity.getRecoveries())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setRecoveries(entity.getRecoveries()));
		Optional.ofNullable(entity.getNetPayable())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setNetPayable(entity.getNetPayable()));
		Optional.ofNullable(entity.getEffDate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setEffDate(entity.getEffDate()));
		Optional.ofNullable(entity.getFundValue())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setFundValue(entity.getFundValue()));
		Optional.ofNullable(entity.getPfFlag())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfFlag(entity.getPfFlag()));
		Optional.ofNullable(entity.getPfRemarks())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfRemarks(entity.getPfRemarks()));
		Optional.ofNullable(entity.getApprovFlag())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setApprovFlag(entity.getApprovFlag()));
		Optional.ofNullable(entity.getApprovRemarks())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setApprovRemarks(entity.getApprovRemarks()));
		Optional.ofNullable(entity.getPfFlagUpdate())
				.ifPresent((FlcLeapDetails) -> flcLeapDetails.setPfFlagUpdate(entity.getPfFlagUpdate()));

		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		flcLeapDetails.setValidFlag(-1);
		flcLeapDetailsRepository.save(flcLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcLeapDetails flcLeapDetails = flcLeapDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcLeapDetails", "id", id));
		flcLeapDetailsRepository.delete(flcLeapDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcLeapDetailsDto> globalSearch(String key) {
		List<FlcLeapDetails> listOfFlcLeapDetails = flcLeapDetailsRepository.globalSearch(key);
		List<FlcLeapDetailsDto> listOfFlcLeapDetailsDto = listOfFlcLeapDetails.stream()
				.map(flcLeapDetails -> entityToDto.apply(flcLeapDetails)).collect(Collectors.toList());
		return listOfFlcLeapDetailsDto;
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

	// Calculateion of Non Ulip Policy
	public String calculateNonUlip(Long policyNo, Long userId) throws ParseException {

		Double val = 0.0;
		String msg = "";
		String flag = "";

		FlcPolicyDetails policyPas = flcPolicyDetailsRepository.getActiveByPolicyNo(policyNo);
		FlcTransactionDetails trans = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNo);
		System.out.println("********** trans No ********** " + trans.getFlcTransNo());
		Double tp = flcCalculations.calculateTotalPremium(policyPas.getPremToDate(), policyPas.getDocDate(),
				policyPas.getInstallmentPremium(), Integer.parseInt(policyPas.getBillFreq()));

		Double stamDuty = flcCalculations.calculateStampDuty(policyNo);
		Double mortRate = flcCalculations.calculateNonUlipMortalityRates(policyNo);

		Double mfRate = flcCalculations.calculateMFRate(policyNo);

		if (mortRate != -1 && mfRate != -1) {

			FlcLeapDetails leapDetails = new FlcLeapDetails();

			leapDetails.setValidFlag(1);
			leapDetails.setCompanyId(policyPas.getCompanyId());
			leapDetails.setFlcPolicyNo(policyNo);
			leapDetails.setTranDate(trans.getFlcLogDate());
			leapDetails.setTranNo(trans.getFlcTransNo());
			leapDetails.setUinNumber(trans.getUinNumber());
			leapDetails.setTotalPremium(tp);
			leapDetails.setAvalSuspense(trans.getFlcPolicyDop());
			leapDetails.setPenalInterest(val);
			leapDetails.setMedicalFee(mfRate);
			leapDetails.setStampDuty(stamDuty.doubleValue());
			leapDetails.setMortCharge(mortRate);

			Double grossPay = tp + trans.getFlcPolicyDop().doubleValue() + val;
			leapDetails.setGrossPayable(grossPay);

			Double recoveries = val + mortRate + stamDuty;
			leapDetails.setRecoveries(recoveries);

			Double netPay = leapDetails.getGrossPayable() - leapDetails.getRecoveries();
			leapDetails.setNetPayable(netPay);

			leapDetails.setCreatedBy(userId);
			leapDetails.setModifiedBy(userId);
			flcLeapDetailsRepository.save(leapDetails);

			trans.setInterimStatus("Processed");
			flcTransactionDetailsRepository.save(trans);

			Double penalInt = calculatePenalInterest(policyNo, leapDetails);

			if (policyPas.getMedicalFlag().equalsIgnoreCase("yes")) {
				leapDetails.setMedicalFee(mfRate);
			} else {
				leapDetails.setMedicalFee(val);
			}

			leapDetails.setPenalInterest(penalInt);

			Double grossPay1 = tp + trans.getFlcPolicyDop().doubleValue() + penalInt;

			BigDecimal bd2 = new BigDecimal(grossPay1).setScale(2, RoundingMode.HALF_UP);

			leapDetails.setGrossPayable(bd2.doubleValue());

			Double recoveries1 = mfRate + mortRate + stamDuty;

			BigDecimal bd3 = new BigDecimal(recoveries1).setScale(2, RoundingMode.HALF_UP);
			leapDetails.setRecoveries(bd3.doubleValue());

			Double netPay1 = leapDetails.getGrossPayable() - leapDetails.getRecoveries();
			BigDecimal bd1 = new BigDecimal(netPay1).setScale(2, RoundingMode.HALF_UP);
			leapDetails.setNetPayable(bd1.doubleValue());

			flcLeapDetailsRepository.save(leapDetails);

			if (!trans.getFlcPremRefund().equals(leapDetails.getTotalPremium())) {
				flag = "Fail";
				String msg1 = "Premium Refund";
				msg = msg1;
			}

			if (!trans.getFlcPolicyDop().equals(leapDetails.getAvalSuspense())) {
				flag = "Fail";
				String msg1 = "Aval Suspense";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (!trans.getPenalIntrest().equals(leapDetails.getPenalInterest())) {
				flag = "Fail";
				String msg1 = "Penal Intrest";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getMedicalFee().equals(leapDetails.getMedicalFee())) {
				flag = "Fail";
				String msg1 = "Medical Fee";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (!trans.getStamDuty().equals(leapDetails.getStampDuty())) {
				flag = "Fail";
				String msg1 = "Stam Duty";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getRiskPremRecov().equals(leapDetails.getMortCharge())) {
				flag = "Fail";
				String msg1 = "Mortality Charge";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getGrossFlcPay().equals(leapDetails.getGrossPayable())) {
				flag = "Fail";
				String msg1 = "Gross Payable";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getTotalRecov().equals(leapDetails.getRecoveries())) {
				flag = "Fail";
				String msg1 = "Recoveries";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getNetFlcPay().equals(leapDetails.getNetPayable())) {
				flag = "Fail";
				String msg1 = "Net Payable";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (flag != "Fail") {
				flag = "Pass";
				msg = "Calculation Matching";
			}
			leapDetails.setPfFlag(flag);
			leapDetails.setPfRemarks(msg);

			flcLeapDetailsRepository.save(leapDetails);

			return errorService.getErrorById("ER001");

		} else {

			FlcLeapDetails leapDetails = new FlcLeapDetails();

			leapDetails.setValidFlag(1);
			leapDetails.setCompanyId(policyPas.getCompanyId());
			leapDetails.setFlcPolicyNo(policyNo);
			leapDetails.setTranDate(trans.getFlcLogDate());
			leapDetails.setTranNo(trans.getFlcTransNo());
			leapDetails.setUinNumber(trans.getUinNumber());
			leapDetails.setTotalPremium(val);
			leapDetails.setAvalSuspense(val);
			leapDetails.setPenalInterest(val);
			leapDetails.setMedicalFee(val);
			leapDetails.setStampDuty(val);
			leapDetails.setMortCharge(val);
			leapDetails.setGrossPayable(val);
			leapDetails.setRecoveries(val);
			leapDetails.setNetPayable(val);
			leapDetails.setPfFlag("Fail");

			if (mortRate == -1 && mfRate == -1) {
				leapDetails.setPfRemarks("Values are missing in mortality table and medical table ");

			} else if (mortRate == -1 && mfRate != -1) {
				leapDetails.setPfRemarks("Values are missing in mortality table ");

			} else if (mortRate != -1 && mfRate == -1) {
				leapDetails.setPfRemarks("Values are missing in medical table ");

			}

			flcLeapDetailsRepository.save(leapDetails);

			trans.setInterimStatus("Processed");
			flcTransactionDetailsRepository.save(trans);
			return errorService.getErrorById("ER001");
		}

	}

	// Calculation for Ulip Policy
	public String calculateUlip(Long policyNo, Long userId) throws ParseException {

		Double val = 0.0;
		String msg = "";
		String flag = "";

		FlcPolicyDetails policyPas = flcPolicyDetailsRepository.getActiveByPolicyNo(policyNo);
		FlcTransactionDetails trans = flcTransactionDetailsRepository.findInitiatedByPolicyNo(policyNo);
		System.out.println("********** trans No ********** " + trans.getFlcTransNo());
		Double tp = flcCalculations.calculateTotalPremium(policyPas.getPremToDate(), policyPas.getDocDate(),
				policyPas.getInstallmentPremium(), Integer.parseInt(policyPas.getBillFreq()));

		Double stamDuty = flcCalculations.calculateStampDuty(policyNo);
		Double mortRate = flcCalculations.calculateUlipMortalityRates(policyNo);

		Double mfRate = flcCalculations.calculateMFRate(policyNo);
		Double fundValue = flcCalculations.calculateFundValue(trans.getFlcTransNo());

		if (mortRate != -1 && mfRate != -1) {

			FlcLeapDetails leapDetails = new FlcLeapDetails();

			leapDetails.setValidFlag(1);
			leapDetails.setCompanyId(policyPas.getCompanyId());
			leapDetails.setFlcPolicyNo(policyNo);
			leapDetails.setTranDate(trans.getFlcLogDate());
			leapDetails.setTranNo(trans.getFlcTransNo());
			leapDetails.setUinNumber(trans.getUinNumber());
			leapDetails.setTotalPremium(tp);
			leapDetails.setEffDate(trans.getEffDate());
			leapDetails.setFundValue(fundValue);
			leapDetails.setAvalSuspense(trans.getFlcPolicyDop());
			leapDetails.setPenalInterest(val);

			if (policyPas.getMedicalFlag().equalsIgnoreCase("Yes")) {
				leapDetails.setMedicalFee(mfRate);
			} else {
				leapDetails.setMedicalFee(val);
			}

			leapDetails.setStampDuty(stamDuty.doubleValue());
			leapDetails.setMortCharge(mortRate);

			Double grossPay = fundValue.longValue() + trans.getFlcPolicyDop().doubleValue() + val + mortRate;
			leapDetails.setGrossPayable(grossPay);

			Double recoveries = mfRate + stamDuty;
			leapDetails.setRecoveries(recoveries);

			Double netPay = leapDetails.getGrossPayable() - leapDetails.getRecoveries();
			leapDetails.setNetPayable(netPay);

			leapDetails.setCreatedBy(userId);
			leapDetails.setModifiedBy(userId);
			flcLeapDetailsRepository.save(leapDetails);

			trans.setInterimStatus("Processed");
			flcTransactionDetailsRepository.save(trans);

			Double penalInt = calculatePenalInterest(policyNo, leapDetails);

			leapDetails.setMedicalFee(mfRate);
			leapDetails.setPenalInterest(penalInt);

			Double grossPay1 = fundValue + trans.getFlcPolicyDop() + penalInt + mortRate;

			BigDecimal bd2 = new BigDecimal(grossPay1).setScale(2, RoundingMode.HALF_UP);

			leapDetails.setGrossPayable(bd2.doubleValue());

			Double recoveries1 = mfRate + stamDuty;
			leapDetails.setRecoveries(recoveries1);

			Double netPay1 = leapDetails.getGrossPayable() - leapDetails.getRecoveries();
			BigDecimal bd1 = new BigDecimal(netPay1).setScale(2, RoundingMode.HALF_UP);
			leapDetails.setNetPayable(bd1.doubleValue());

			flcLeapDetailsRepository.save(leapDetails);

			if (!trans.getFlcTotalPrem().equals(leapDetails.getTotalPremium())) {
				flag = "Fail";
				String msg1 = "Total Premium ";
				msg = msg1;
			}

			if (!trans.getFundValue().equals(leapDetails.getFundValue())) {
				flag = "Fail";
				String msg1 = "Fund Value";
				msg = msg1;
			}

			if (!trans.getFlcPolicyDop().equals(leapDetails.getAvalSuspense())) {
				flag = "Fail";
				String msg1 = "Aval Suspense";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (!trans.getPenalIntrest().equals(leapDetails.getPenalInterest())) {
				flag = "Fail";
				String msg1 = "Penal Intrest";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getMedicalFee().equals(leapDetails.getMedicalFee())) {
				flag = "Fail";
				String msg1 = "Medical Fee";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (!trans.getStamDuty().equals(leapDetails.getStampDuty())) {
				flag = "Fail";
				String msg1 = "Stam Duty";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getMortChargeRefund().equals(leapDetails.getMortCharge())) {
				flag = "Fail";
				String msg1 = "Mortality Charge";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getGrossFlcPay().equals(leapDetails.getGrossPayable())) {
				flag = "Fail";
				String msg1 = "Gross Payable";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getTotalRecov().equals(leapDetails.getRecoveries())) {
				flag = "Fail";
				String msg1 = "Recoveries";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}
			if (!trans.getNetFlcPay().equals(leapDetails.getNetPayable())) {
				flag = "Fail";
				String msg1 = "Net Payable";
				if (msg.equals("")) {
					msg = msg1;
				} else {
					msg = msg + "," + msg1;
				}
			}

			if (flag != "Fail") {
				flag = "Pass";
				msg = "Calculation Matching";
			}
			leapDetails.setPfFlag(flag);
			leapDetails.setPfRemarks(msg);

			flcLeapDetailsRepository.save(leapDetails);

			return errorService.getErrorById("ER001");

		} else {

			FlcLeapDetails leapDetails = new FlcLeapDetails();

			leapDetails.setValidFlag(1);
			leapDetails.setCompanyId(policyPas.getCompanyId());
			leapDetails.setFlcPolicyNo(policyNo);
			leapDetails.setTranDate(trans.getFlcLogDate());
			leapDetails.setTranNo(trans.getFlcTransNo());
			leapDetails.setUinNumber(trans.getUinNumber());
			leapDetails.setTotalPremium(val);
			leapDetails.setAvalSuspense(val);
			leapDetails.setPenalInterest(val);
			leapDetails.setMedicalFee(val);
			leapDetails.setStampDuty(val);
			leapDetails.setMortCharge(val);
			leapDetails.setGrossPayable(val);
			leapDetails.setRecoveries(val);
			leapDetails.setNetPayable(val);
			leapDetails.setPfFlag("Fail");

			if (mortRate == -1 && mfRate == -1) {
				leapDetails.setPfRemarks("Values are missing in mortality table and medical table ");

			} else if (mortRate == -1 && mfRate != -1) {
				leapDetails.setPfRemarks("Values are missing in mortality table ");

			} else if (mortRate != -1 && mfRate == -1) {
				leapDetails.setPfRemarks("Values are missing in medical table ");

			}

			flcLeapDetailsRepository.save(leapDetails);

			trans.setInterimStatus("Processed");
			flcTransactionDetailsRepository.save(trans);
			return errorService.getErrorById("ER001");
		}

	}

	public String qcUpdate(FlcLeapDetails purpleDetails, Long policyNo, Long userId) {

		FlcTransactionDetails transactions = flcTransactionDetailsRepository.findProcessedByPolicyNo(policyNo);
		FlcLeapDetails pd = flcLeapDetailsRepository.getByTransNo(transactions.getFlcTransNo());

		pd.setApprovRemarks(purpleDetails.getApprovRemarks());
		pd.setApprovFlag(purpleDetails.getApprovFlag());
		flcLeapDetailsRepository.save(pd);
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String approvalDate = dateFormat.format(date);

		pd.setApprovalDate(approvalDate);

		flcLeapDetailsRepository.save(pd);
		System.out.println("*********** Transaction Approv Flag ***********" + purpleDetails.getApprovFlag());
		transactions.setLeapApprovalFlag(pd.getApprovFlag());
		transactions.setLeapApprovalRemark(pd.getApprovRemarks());
		transactions.setLeapApprovalDate(pd.getApprovalDate());
		transactions.setApprovalQcUserId(userId);
		transactions.setInterimStatus("QC Completed");
		flcTransactionDetailsRepository.save(transactions);
		if (transactions.getLeapApprovalFlag().equals(pd.getApprovFlag())) {
			pd.setPfFlagUpdate("Transaction Updated");
		}
		flcLeapDetailsRepository.save(pd);

		if (transactions.getLeapApprovalFlag().equals("Fail")) {

			System.out.println("Inside If Statement ");

			FlcPolicyDetails policy = flcPolicyDetailsRepository.getActiveByPolicyNo(transactions.getFlcPolicyNo());
			List<FlcCoverDetails> covers = flcCoverDetailsRepository.getAllByPolicyNo(policy.getPolicyNo());

			System.out.println("Client Number " + policy.getClntNum());
			FlcClientDetails client = flcClientDetailsRepository.getActiveByClientNo(policy.getClntNum());

			System.out.println("Client Number " + client.getClntNum());

			policy.setValidFlag(-1);
			flcPolicyDetailsRepository.save(policy);

			for (FlcCoverDetails cover : covers) {
				cover.setValidFlag(-1);
				flcCoverDetailsRepository.save(cover);
			}
			client.setValidFlag(-1);
			flcClientDetailsRepository.save(client);

		}
		return errorService.getErrorById("ER003");
	}
	
	public String assignMultipleTrans(List<Long> policyNums, Long userId) {

		policyNums.forEach((policyNo) -> {
			try {
				FlcPolicyDetails policy = flcPolicyDetailsRepository.getActiveByPolicyNo(policyNo);
				
				UinMaster uinMaster = uinMasterRepository.getActiveByUIN(policy.getUinNumber());

				if(uinMaster.getFlcEligibility().equalsIgnoreCase("Yes")) {
					if (uinMaster.getProductType().contains("N")) {
						calculateNonUlip(policyNo, userId);
					} else if (uinMaster.getProductType().contains("L")) {
						calculateUlip(policyNo, userId);
					}
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		});

		return errorService.getErrorById("ER001");
	}

}
