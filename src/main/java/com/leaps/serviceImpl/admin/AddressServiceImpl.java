package com.leaps.serviceImpl.admin;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.leaps.dto.admin.AddressDto;
import com.leaps.entity.admin.Address;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.AddressRepository;
import com.leaps.responses.admin.AddressResponse;
import com.leaps.service.admin.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	// Autowiring ModelMapper to Convert Entity to Dto and Vice Versa
	@Autowired
	private ModelMapper mapper;

	// Autowiring Repository to use jpa methods and custom queries
	@Autowired
	private AddressRepository addressRepository;

	// Autowiring Error Service to Display the Response Messages Stored in Error
	// Table
	@Autowired
	private ErrorService errorService;

	// Entity to Dto using Function

	public Function<Address, AddressDto> entityToDto = entity -> mapper.map(entity, AddressDto.class);
	
	// Dto to Entity using Function

	public Function<AddressDto, Address> dtoToEntity = dto -> mapper.map(dto, Address.class);

	// Get all where valid flag=1
	@Override
	public List<AddressDto> getall() {
		List<Address> listofAddress = addressRepository.getallActive();
		List<AddressDto> listofAddressDto = listofAddress.stream().map(address -> entityToDto.apply(address))
				.collect(Collectors.toList());
		return listofAddressDto;
	}

	// Get All where valid flag=1 With Pagination
	@Override
	public AddressResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		// sort Condition
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// Create Pageable Instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Address> addressPage = addressRepository.getallActivePagination(pageable);

		// get content
		List<Address> listOfAddress = addressPage.getContent();

		List<AddressDto> content = listOfAddress.stream().map(address -> entityToDto.apply(address))
				.collect(Collectors.toList());

		// Setting the the values to Custom Response Created
		AddressResponse addressResponse = new AddressResponse();
		addressResponse.setContent(content);
		addressResponse.setPageNo(addressPage.getNumber());
		addressResponse.setPageSize(addressPage.getSize());
		addressResponse.setTotalElements(addressPage.getTotalElements());
		addressResponse.setTotalPages(addressPage.getTotalPages());
		addressResponse.setLast(addressPage.isLast());

		return addressResponse;
	}

	// Get Active By id
	@Override
	public AddressDto getbyid(Long id) {
		Address address = addressRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
		AddressDto addressDto = entityToDto.apply(address);
		return addressDto;
	}

	// Add
	@Override
	public String add(AddressDto dto) {
		Address address = dtoToEntity.apply(dto);
		address.setValidFlag(1);
		addressRepository.save(address);
		return errorService.getErrorById("ER001");
	}

	// Update
	@Override
	public String update(Long id, AddressDto dto) {
		Address entity = dtoToEntity.apply(dto);

		Address address = addressRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));

		// Using Optional for Null check
		Optional.ofNullable(entity.getAddressType())
				.ifPresent((Address) -> address.setAddressType(entity.getAddressType()));
		Optional.ofNullable(entity.getAddress1())
				.ifPresent((Address) -> address.setAddress1(entity.getAddress1()));
		Optional.ofNullable(entity.getAddress2())
				.ifPresent((Address) -> address.setAddress2(entity.getAddress2()));
		Optional.ofNullable(entity.getAddress3())
				.ifPresent((Address) -> address.setAddress3(entity.getAddress3()));
		Optional.ofNullable(entity.getAddress4())
				.ifPresent((Address) -> address.setAddress4(entity.getAddress4()));
		Optional.ofNullable(entity.getAddress5())
				.ifPresent((Address) -> address.setAddress5(entity.getAddress5()));
		Optional.ofNullable(entity.getPostalCode())
				.ifPresent((Address) -> address.setPostalCode(entity.getPostalCode()));
		Optional.ofNullable(entity.getState())
				.ifPresent((Address) -> address.setState(entity.getState()));
		Optional.ofNullable(entity.getCountry())
				.ifPresent((Address) -> address.setCountry(entity.getCountry()));
		Optional.ofNullable(entity.getMobile())
				.ifPresent((Address) -> address.setMobile(entity.getMobile()));
		Optional.ofNullable(entity.getDistrict())
				.ifPresent((Address) -> address.setDistrict(entity.getDistrict()));

		addressRepository.save(address);
		return errorService.getErrorById("ER002");

	}

	// Soft Delete
	@Override
	public String softdelete(Long id) {
		Address address = addressRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
		address.setValidFlag(-1);
		addressRepository.save(address);
		return errorService.getErrorById("ER003");
	}

	// Hard Delete
	@Override
	public String harddelete(Long id) {
		Address address = addressRepository.getActiveById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "id", id));
		addressRepository.delete(address);
		return errorService.getErrorById("ER003");
	}

	// Global Search
	@Override
	public List<AddressDto> globalSearch(String key) {
		List<Address> listofaddress = addressRepository.globalSearch(key);
		List<AddressDto> listofAddressDto = listofaddress.stream().map(address -> entityToDto.apply(address))
				.collect(Collectors.toList());
		return listofAddressDto;
	}

}
