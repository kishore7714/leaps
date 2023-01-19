package com.leaps.serviceImpl.flc;

import java.util.List;
import java.util.Optional;
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

import com.leaps.dto.flc.FlcTransactionDetailsDto;
import com.leaps.entity.flc.FlcTransactionDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.flc.FlcTransactionDetailsRepository;
import com.leaps.responses.flc.FlcTransactionDetailsResponse;
import com.leaps.service.flc.FlcTransactionDetailsService;

@Service
public class FlcTransactionDetailsServiceImpl implements FlcTransactionDetailsService {

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private FlcTransactionDetailsRepository flcTransactionDetailsRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<FlcTransactionDetails, FlcTransactionDetailsDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<FlcTransactionDetails, FlcTransactionDetailsDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());

			}

		});
		FlcTransactionDetailsDto dto = mapper.map(entity, FlcTransactionDetailsDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<FlcTransactionDetailsDto, FlcTransactionDetails> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		FlcTransactionDetails entity = mapper.map(dto, FlcTransactionDetails.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<FlcTransactionDetailsDto> getall() {
		List<FlcTransactionDetails> listOfFlcTransactionDetails = flcTransactionDetailsRepository.getallActive();
		List<FlcTransactionDetailsDto> listOfFlcTransactionDetailsDto = listOfFlcTransactionDetails.stream()
				.map(flcTransactionDetails -> entityToDto.apply(flcTransactionDetails)).collect(Collectors.toList());
		return listOfFlcTransactionDetailsDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public FlcTransactionDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<FlcTransactionDetails> flcTransactionDetailsPage = flcTransactionDetailsRepository
				.getallActivePagination(pageable);

		// get content
		List<FlcTransactionDetails> listOfFlcTransactionDetails = flcTransactionDetailsPage.getContent();

		List<FlcTransactionDetailsDto> content = listOfFlcTransactionDetails.stream()
				.map(flcTransactionDetails -> entityToDto.apply(flcTransactionDetails)).collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		FlcTransactionDetailsResponse flcTransactionDetailsResponse = new FlcTransactionDetailsResponse();
		flcTransactionDetailsResponse.setContent(content);
		flcTransactionDetailsResponse.setPageNo(flcTransactionDetailsPage.getNumber());
		flcTransactionDetailsResponse.setPageSize(flcTransactionDetailsPage.getSize());
		flcTransactionDetailsResponse.setTotalElements(flcTransactionDetailsPage.getTotalElements());
		flcTransactionDetailsResponse.setTotalPages(flcTransactionDetailsPage.getTotalPages());
		flcTransactionDetailsResponse.setLast(flcTransactionDetailsPage.isLast());

		return flcTransactionDetailsResponse;
	}

	// Get Active By id
	@Override
	public FlcTransactionDetailsDto getbyid(Long id) {
		FlcTransactionDetails flcTransactionDetails = flcTransactionDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcTransactionDetails", "id", id));
		FlcTransactionDetailsDto flcTransactionDetailsDto = entityToDto.apply(flcTransactionDetails);
		return flcTransactionDetailsDto;
	}

	// Add
	@Override
	public String add(FlcTransactionDetailsDto dto) {
		FlcTransactionDetails flcTransactionDetails = dtoToEntity.apply(dto);
		flcTransactionDetails.setValidFlag(1);
		flcTransactionDetailsRepository.save(flcTransactionDetails);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, FlcTransactionDetailsDto dto) {
		FlcTransactionDetails entity = dtoToEntity.apply(dto);

		FlcTransactionDetails flcTransactionDetails = flcTransactionDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcTransactionDetails", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> flcTransactionDetails.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getFlcPolicyNo())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcPolicyNo(entity.getFlcPolicyNo()));
		Optional.ofNullable(entity.getFlcTransNo())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcTransNo(entity.getFlcTransNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getFlcReqDate())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcReqDate(entity.getFlcReqDate()));
		Optional.ofNullable(entity.getFlcLogDate())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcLogDate(entity.getFlcLogDate()));
		Optional.ofNullable(entity.getFlcPremRefund()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setFlcPremRefund(entity.getFlcPremRefund()));
		Optional.ofNullable(entity.getFlcTotalPrem())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcTotalPrem(entity.getFlcTotalPrem()));
		Optional.ofNullable(entity.getFlcPolicyDop())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFlcPolicyDop(entity.getFlcPolicyDop()));
		Optional.ofNullable(entity.getPenalIntrest())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setPenalIntrest(entity.getPenalIntrest()));
		Optional.ofNullable(entity.getGrossFlcPay())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setGrossFlcPay(entity.getGrossFlcPay()));
		Optional.ofNullable(entity.getMedicalFee())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setMedicalFee(entity.getMedicalFee()));
		Optional.ofNullable(entity.getStamDuty())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setStamDuty(entity.getStamDuty()));
		Optional.ofNullable(entity.getRiskPremRecov()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setRiskPremRecov(entity.getRiskPremRecov()));
		Optional.ofNullable(entity.getMortChargeRefund()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setMortChargeRefund(entity.getMortChargeRefund()));
		Optional.ofNullable(entity.getTotalRecov())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setTotalRecov(entity.getTotalRecov()));
		Optional.ofNullable(entity.getNetFlcPay())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setNetFlcPay(entity.getNetFlcPay()));
		Optional.ofNullable(entity.getEffDate())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setEffDate(entity.getEffDate()));
		Optional.ofNullable(entity.getFundValue())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setFundValue(entity.getFundValue()));
		Optional.ofNullable(entity.getFlcApprovalDate()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setFlcApprovalDate(entity.getFlcApprovalDate()));
		Optional.ofNullable(entity.getMedicalCategory()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setMedicalCategory(entity.getMedicalCategory()));
		Optional.ofNullable(entity.getMedicalCategory()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setMedicalCategory(entity.getMedicalCategory()));
		Optional.ofNullable(entity.getMedicatTpaCode()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setMedicatTpaCode(entity.getMedicatTpaCode()));
		Optional.ofNullable(entity.getMakerFlag())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setMakerFlag(entity.getMakerFlag()));
		Optional.ofNullable(entity.getCheckerFlag())
				.ifPresent((FlcTransactionDetails) -> flcTransactionDetails.setCheckerFlag(entity.getCheckerFlag()));
		Optional.ofNullable(entity.getLeapApprovalFlag()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setLeapApprovalFlag(entity.getLeapApprovalFlag()));
		Optional.ofNullable(entity.getLeapApprovalRemark()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setLeapApprovalRemark(entity.getLeapApprovalRemark()));
		Optional.ofNullable(entity.getLeapApprovalDate()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setLeapApprovalDate(entity.getLeapApprovalDate()));
		Optional.ofNullable(entity.getApprovalQcUserId()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setApprovalQcUserId(entity.getApprovalQcUserId()));
		Optional.ofNullable(entity.getInterimStatus()).ifPresent(
				(FlcTransactionDetails) -> flcTransactionDetails.setInterimStatus(entity.getInterimStatus()));

		flcTransactionDetailsRepository.save(flcTransactionDetails);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		FlcTransactionDetails flcTransactionDetails = flcTransactionDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcTransactionDetails", "id", id));
		flcTransactionDetails.setValidFlag(-1);
		flcTransactionDetailsRepository.save(flcTransactionDetails);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		FlcTransactionDetails flcTransactionDetails = flcTransactionDetailsRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FlcTransactionDetails", "id", id));
		flcTransactionDetailsRepository.delete(flcTransactionDetails);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<FlcTransactionDetailsDto> globalSearch(String key) {
		List<FlcTransactionDetails> listOfFlcTransactionDetails = flcTransactionDetailsRepository.globalSearch(key);
		List<FlcTransactionDetailsDto> listOfFlcTransactionDetailsDto = listOfFlcTransactionDetails.stream()
				.map(flcTransactionDetails -> entityToDto.apply(flcTransactionDetails)).collect(Collectors.toList());
		return listOfFlcTransactionDetailsDto;
	}
}
