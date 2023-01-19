package com.leaps.serviceImpl.deathclaim;

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

import com.leaps.dto.deathclaim.DeathClaimCoverDetailsDto;
import com.leaps.entity.deathclaim.DeathClaimCoverDetails;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.deathclaim.DeathClaimCoverDetailsRepository;
import com.leaps.responses.deathclaim.DeathClaimCoverDetailsResponse;
import com.leaps.service.deathclaim.DeathClaimCoverDetailsService;

@Service
public class DeathClaimCoverDetailsServiceImpl implements DeathClaimCoverDetailsService{

	   // Autowiring Repository to use jpa methods and custom queries
		@Autowired
		private DeathClaimCoverDetailsRepository deathClaimCoverDetailsRepository;

		// Autowiring Error Service to Display the Response Messages Stored in Error
		// Table
		@Autowired
		private ErrorService errorService;

		// Entity to Dto using Function

		public Function<DeathClaimCoverDetails, DeathClaimCoverDetailsDto> entityToDto = entity -> {

			// using Model Mapper to convert entity to dto
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			mapper.addMappings(new PropertyMap<DeathClaimCoverDetails, DeathClaimCoverDetailsDto>() {

				@Override
				protected void configure() {
					map().setCompanyName(source.getCompany().getCompanyName());

				}

			});
			DeathClaimCoverDetailsDto dto = mapper.map(entity, DeathClaimCoverDetailsDto.class);
			return dto;
		};

		// Dto to Entity using Function

		public Function<DeathClaimCoverDetailsDto, DeathClaimCoverDetails> dtoToEntity = dto -> {

			// using Model Mapper to convert entity to dto
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			DeathClaimCoverDetails entity = mapper.map(dto, DeathClaimCoverDetails.class);
			return entity;
		};

		// Get all where valid flag=1
		@Override
		public List<DeathClaimCoverDetailsDto> getall() {
			List<DeathClaimCoverDetails> listOfDeathClaimCoverDetails = deathClaimCoverDetailsRepository.getallActive();
			List<DeathClaimCoverDetailsDto> listOfDeathClaimCoverDetailsDto = listOfDeathClaimCoverDetails.stream()
					.map(deathClaimCoverDetails -> entityToDto.apply(deathClaimCoverDetails)).collect(Collectors.toList());
			return listOfDeathClaimCoverDetailsDto;
		}

		// Get All where valid flag=1 With Pagination
		@Override
		public DeathClaimCoverDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy,
				String sortDir) {
			// sort Condition
			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
					: Sort.by(sortBy).descending();

			// Create Pageable Instance
			Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

			Page<DeathClaimCoverDetails> deathClaimCoverDetailsPage = deathClaimCoverDetailsRepository
					.getallActivePagination(pageable);

			// get content
			List<DeathClaimCoverDetails> listOfDeathClaimCoverDetails = deathClaimCoverDetailsPage.getContent();

			List<DeathClaimCoverDetailsDto> content = listOfDeathClaimCoverDetails.stream()
					.map(deathClaimCoverDetails -> entityToDto.apply(deathClaimCoverDetails)).collect(Collectors.toList());

			// Setting the the values to Custom Response Created
			DeathClaimCoverDetailsResponse deathClaimCoverDetailsResponse = new DeathClaimCoverDetailsResponse();
			deathClaimCoverDetailsResponse.setContent(content);
			deathClaimCoverDetailsResponse.setPageNo(deathClaimCoverDetailsPage.getNumber());
			deathClaimCoverDetailsResponse.setPageSize(deathClaimCoverDetailsPage.getSize());
			deathClaimCoverDetailsResponse.setTotalElements(deathClaimCoverDetailsPage.getTotalElements());
			deathClaimCoverDetailsResponse.setTotalPages(deathClaimCoverDetailsPage.getTotalPages());
			deathClaimCoverDetailsResponse.setLast(deathClaimCoverDetailsPage.isLast());

			return deathClaimCoverDetailsResponse;
		}

		// Get Active By id
		@Override
		public DeathClaimCoverDetailsDto getbyid(Long id) {
			DeathClaimCoverDetails deathClaimDetails = deathClaimCoverDetailsRepository.getActiveById(id)
					.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverDetails", "id", id));
			DeathClaimCoverDetailsDto deathClaimCoverDetailsDto = entityToDto.apply(deathClaimDetails);
			return deathClaimCoverDetailsDto;
		}

		// Add
		@Override
		public String add(DeathClaimCoverDetailsDto dto) {
			DeathClaimCoverDetails deathClaimCoverDetails = dtoToEntity.apply(dto);
			deathClaimCoverDetails.setValidFlag(1);
			deathClaimCoverDetailsRepository.save(deathClaimCoverDetails);
			return errorService.getErrorById("ER001");
		}

		// Update
		@Override
		public String update(Long id, DeathClaimCoverDetailsDto dto) {
			DeathClaimCoverDetails entity = dtoToEntity.apply(dto);

			DeathClaimCoverDetails deathClaimDetails = deathClaimCoverDetailsRepository.getActiveById(id)
					.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverDetails", "id", id));

			// Using Optional for Null check
			Optional.of(entity.getCompanyId()).ifPresent((DeathClaimCoverDetails) -> deathClaimDetails.setCompanyId(entity.getCompanyId()));

			Optional.ofNullable(entity.getClntNum()).ifPresent((DeathClaimCoverDetails) -> deathClaimDetails
					.setClntNum(entity.getClntNum()));

			deathClaimCoverDetailsRepository.save(deathClaimDetails);
			return errorService.getErrorById("ER002");

		}

		// Soft Delete
		@Override
		public String softdelete(Long id) {
			DeathClaimCoverDetails deathClaimCoverDetails = deathClaimCoverDetailsRepository.getActiveById(id)
					.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverDetails", "id", id));
			deathClaimCoverDetails.setValidFlag(-1);
			deathClaimCoverDetailsRepository.save(deathClaimCoverDetails);
			return errorService.getErrorById("ER003");
		}

		// Hard Delete
		@Override
		public String harddelete(Long id) {
			DeathClaimCoverDetails deathClaimCoverDetails = deathClaimCoverDetailsRepository.getActiveById(id)
					.orElseThrow(() -> new ResourceNotFoundException("DeathClaimCoverDetails", "id", id));
			deathClaimCoverDetailsRepository.delete(deathClaimCoverDetails);
			return errorService.getErrorById("ER003");
		}

		// Global Search
		@Override
		public List<DeathClaimCoverDetailsDto> globalSearch(String key) {
			List<DeathClaimCoverDetails> listOfDeathClaimCoverDetails = deathClaimCoverDetailsRepository
					.globalSearch(key);
			List<DeathClaimCoverDetailsDto> listOfDeathClaimCoverDetailsDto = listOfDeathClaimCoverDetails.stream()
					.map(deathClaimCoverDetails -> entityToDto.apply(deathClaimCoverDetails)).collect(Collectors.toList());
			return listOfDeathClaimCoverDetailsDto;
		}
}
