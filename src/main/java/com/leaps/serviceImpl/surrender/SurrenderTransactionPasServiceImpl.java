package com.leaps.serviceImpl.surrender;

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

import com.leaps.dto.surrender.SurrenderTransactionPasDto;
import com.leaps.entity.surrender.SurrenderTransactionPas;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.surrender.SurrenderTransactionPasRepository;
import com.leaps.responses.surrender.SurrenderTransactionPasResponse;
import com.leaps.service.surrender.SurrenderTransactionPasService;

@Service
public class SurrenderTransactionPasServiceImpl implements SurrenderTransactionPasService {
	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private SurrenderTransactionPasRepository surrenderTransactionPasRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<SurrenderTransactionPas, SurrenderTransactionPasDto> entityToDto = entity -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		mapper.addMappings(new PropertyMap<SurrenderTransactionPas, SurrenderTransactionPasDto>() {

			@Override
			protected void configure() {
				map().setCompanyName(source.getCompany().getCompanyName());
			}
		});
		SurrenderTransactionPasDto dto = mapper.map(entity, SurrenderTransactionPasDto.class);
		return dto;
	};

	// Dto to Entity using Function

	public Function<SurrenderTransactionPasDto, SurrenderTransactionPas> dtoToEntity = dto -> {

		// using Model Mapper to convert entity to dto
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SurrenderTransactionPas entity = mapper.map(dto, SurrenderTransactionPas.class);
		return entity;
	};

	// Get all where valid flag=1
	@Override
	public List<SurrenderTransactionPasDto> getall() {
		List<SurrenderTransactionPas> listOfSurrenderTransactionPas = surrenderTransactionPasRepository.getallActive();
		List<SurrenderTransactionPasDto> listOfSurrenderTransactionPasDto = listOfSurrenderTransactionPas.stream()
				.map(surrenderTransactionPas -> entityToDto.apply(surrenderTransactionPas))
				.collect(Collectors.toList());
		return listOfSurrenderTransactionPasDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public SurrenderTransactionPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
			String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<SurrenderTransactionPas> surrenderTransactionPasPage = surrenderTransactionPasRepository
				.getallActivePagination(pageable);

		// get content
		List<SurrenderTransactionPas> listOfSurrenderTransactionPas = surrenderTransactionPasPage.getContent();

		List<SurrenderTransactionPasDto> content = listOfSurrenderTransactionPas.stream()
				.map(surrenderTransactionPas -> entityToDto.apply(surrenderTransactionPas))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		SurrenderTransactionPasResponse surrenderTransactionPasResponse = new SurrenderTransactionPasResponse();
		surrenderTransactionPasResponse.setContent(content);
		surrenderTransactionPasResponse.setPageNo(surrenderTransactionPasPage.getNumber());
		surrenderTransactionPasResponse.setPageSize(surrenderTransactionPasPage.getSize());
		surrenderTransactionPasResponse.setTotalElements(surrenderTransactionPasPage.getTotalElements());
		surrenderTransactionPasResponse.setTotalPages(surrenderTransactionPasPage.getTotalPages());
		surrenderTransactionPasResponse.setLast(surrenderTransactionPasPage.isLast());

		return surrenderTransactionPasResponse;
	}

	// Get Active By id
	@Override
	public SurrenderTransactionPasDto getbyid(Long id) {
		SurrenderTransactionPas surrenderTransactionPas = surrenderTransactionPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderTransactionPas", "id", id));
		SurrenderTransactionPasDto surrenderTransactionPasDto = entityToDto.apply(surrenderTransactionPas);
		return surrenderTransactionPasDto;
	}

	// Add
	@Override
	public String add(SurrenderTransactionPasDto dto) {
		SurrenderTransactionPas surrenderTransactionPas = dtoToEntity.apply(dto);
		surrenderTransactionPas.setValidFlag(1);
		surrenderTransactionPasRepository.save(surrenderTransactionPas);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, SurrenderTransactionPasDto dto) {
		SurrenderTransactionPas entity = dtoToEntity.apply(dto);

		SurrenderTransactionPas surrenderTransactionPas = surrenderTransactionPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderTransactionPas", "id", id));

		// Using Optional for Null check
		Optional.of(entity.getCompanyId())
				.ifPresent((DeathClaimCoverDetails) -> surrenderTransactionPas.setCompanyId(entity.getCompanyId()));

		Optional.ofNullable(entity.getPolicyNo())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setPolicyNo(entity.getPolicyNo()));
		Optional.ofNullable(entity.getTransNo())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setTransNo(entity.getTransNo()));
		Optional.ofNullable(entity.getUinNumber())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setUinNumber(entity.getUinNumber()));
		Optional.ofNullable(entity.getSvReqDate())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setSvReqDate(entity.getSvReqDate()));
		Optional.ofNullable(entity.getLogDate())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setLogDate(entity.getLogDate()));
		Optional.ofNullable(entity.getGsv())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setGsv(entity.getGsv()));
		Optional.ofNullable(entity.getSsv())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setSsv(entity.getSsv()));
		Optional.ofNullable(entity.getPolicyDeposit()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setPolicyDeposit(entity.getPolicyDeposit()));
		Optional.ofNullable(entity.getPenalInterest()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setPenalInterest(entity.getPenalInterest()));
		Optional.ofNullable(entity.getGrossPay())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setGrossPay(entity.getGrossPay()));
		Optional.ofNullable(entity.getCdaCharges())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setCdaCharges(entity.getCdaCharges()));
		Optional.ofNullable(entity.getTds())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setTds(entity.getTds()));
		Optional.ofNullable(entity.getCashValueBonus()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setCashValueBonus(entity.getCashValueBonus()));
		Optional.ofNullable(entity.getPaidUpValue()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setPaidUpValue(entity.getPaidUpValue()));
		Optional.ofNullable(entity.getReversionaryBonus())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas
						.setReversionaryBonus(entity.getReversionaryBonus()));
		Optional.ofNullable(entity.getInterimBonus()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setInterimBonus(entity.getInterimBonus()));
		Optional.ofNullable(entity.getTotalRecovery()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setTotalRecovery(entity.getTotalRecovery()));
		Optional.ofNullable(entity.getOtherRecovery()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setOtherRecovery(entity.getOtherRecovery()));
		Optional.ofNullable(entity.getNetPayable())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setNetPayable(entity.getNetPayable()));
		Optional.ofNullable(entity.getEffectiveDate()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setEffectiveDate(entity.getEffectiveDate()));
		Optional.ofNullable(entity.getApprovDate())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setApprovDate(entity.getApprovDate()));
		Optional.ofNullable(entity.getFundValue())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setFundValue(entity.getFundValue()));
		Optional.ofNullable(entity.getPolicyLoan())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setPolicyLoan(entity.getPolicyLoan()));
		Optional.ofNullable(entity.getLoanInterest()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setLoanInterest(entity.getLoanInterest()));
		Optional.ofNullable(entity.getGsvPayable())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setGsvPayable(entity.getGsvPayable()));
		Optional.ofNullable(entity.getTotalBonus())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setTotalBonus(entity.getTotalBonus()));
		Optional.ofNullable(entity.getMakerFlag())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setMakerFlag(entity.getMakerFlag()));
		Optional.ofNullable(entity.getCheckerFlag()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setCheckerFlag(entity.getCheckerFlag()));
		Optional.ofNullable(entity.getIpcaApprovalFlag()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setIpcaApprovalFlag(entity.getIpcaApprovalFlag()));
		Optional.ofNullable(entity.getIpcaApprovalRemarks())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas
						.setIpcaApprovalRemarks(entity.getIpcaApprovalRemarks()));
		Optional.ofNullable(entity.getIpcaApprovalDate()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setIpcaApprovalDate(entity.getIpcaApprovalDate()));
		Optional.ofNullable(entity.getQcUserId())
				.ifPresent((SurrenderTransactionPas) -> surrenderTransactionPas.setQcUserId(entity.getQcUserId()));
		Optional.ofNullable(entity.getInterimStatus()).ifPresent(
				(SurrenderTransactionPas) -> surrenderTransactionPas.setInterimStatus(entity.getInterimStatus()));

		surrenderTransactionPasRepository.save(surrenderTransactionPas);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		SurrenderTransactionPas surrenderTransactionPas = surrenderTransactionPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderTransactionPas", "id", id));
		surrenderTransactionPas.setValidFlag(-1);
		surrenderTransactionPasRepository.save(surrenderTransactionPas);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		SurrenderTransactionPas surrenderTransactionPas = surrenderTransactionPasRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SurrenderTransactionPas", "id", id));
		surrenderTransactionPasRepository.delete(surrenderTransactionPas);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<SurrenderTransactionPasDto> globalSearch(String key) {
		List<SurrenderTransactionPas> listOfSurrenderTransactionPas = surrenderTransactionPasRepository
				.globalSearch(key);
		List<SurrenderTransactionPasDto> listOfSurrenderTransactionPasDto = listOfSurrenderTransactionPas.stream()
				.map(surrenderTransactionPas -> entityToDto.apply(surrenderTransactionPas))
				.collect(Collectors.toList());
		return listOfSurrenderTransactionPasDto;
	}
}
