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

import com.leaps.dto.admin.ServiceTableDto;
import com.leaps.entity.admin.ServiceTable;
import com.leaps.error.ErrorService;
import com.leaps.exceptions.ResourceNotFoundException;
import com.leaps.repository.admin.ServiceTableRepository;
import com.leaps.responses.admin.ServiceTableResponse;
import com.leaps.service.admin.ServiceTableService;

@Service
public class ServiceTableServiceImpl implements ServiceTableService{
	
	// Autowiring ModelMapper to Convert Entity to Dto and Vice Versa
		@Autowired
		private ModelMapper mapper;
	
	// Autowiring Repository to use jpa methods and custom queries
    @Autowired
    private ServiceTableRepository serviceTableRepository;

    // Autowiring Error Service to Display the Response Messages Stored in Error
    // Table
    @Autowired
    private ErrorService errorService;

    // Entity to Dto using Function

    public Function<ServiceTable, ServiceTableDto> entityToDto = entity -> mapper.map(entity,ServiceTableDto.class);
        
      

    // Dto to Entity using Function

    public Function<ServiceTableDto, ServiceTable> dtoToEntity = dto -> mapper.map(dto,ServiceTable.class);

    // Get all where valid flag=1
    @Override
    public List<ServiceTableDto> getall() {
        List<ServiceTable> listOfServiceTable = serviceTableRepository.findAll();
        List<ServiceTableDto> listOfServiceTableDto = listOfServiceTable.stream()
                .map(serviceTable -> entityToDto.apply(serviceTable))
                .collect(Collectors.toList());
        return listOfServiceTableDto;
    }

    // Get All where valid flag=1 With Pagination
    @Override
    public ServiceTableResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
        // sort Condition
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Create Pageable Instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<ServiceTable> serviceTablePage = serviceTableRepository.findAll(pageable);

        // get content
        List<ServiceTable> listOfServiceTable = serviceTablePage.getContent();

        List<ServiceTableDto> content = listOfServiceTable.stream().map(serviceTable -> entityToDto.apply(serviceTable))
                .collect(Collectors.toList());

        // Setting the the values to Custom Response Created
        ServiceTableResponse serviceTableResponse = new ServiceTableResponse();
        serviceTableResponse.setContent(content);
        serviceTableResponse.setPageNo(serviceTablePage.getNumber());
        serviceTableResponse.setPageSize(serviceTablePage.getSize());
        serviceTableResponse.setTotalElements(serviceTablePage.getTotalElements());
        serviceTableResponse.setTotalPages(serviceTablePage.getTotalPages());
        serviceTableResponse.setLast(serviceTablePage.isLast());

        return serviceTableResponse;
    }

    // Get Active By id
    @Override
    public ServiceTableDto getbyid(Long id) {
        ServiceTable serviceTable = serviceTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceTable", "id", id));
        ServiceTableDto serviceTableDto = entityToDto.apply(serviceTable);
        return serviceTableDto;
    }

    // Add
    @Override
    public String add(ServiceTableDto dto) {
        ServiceTable serviceTable = dtoToEntity.apply(dto);
        serviceTableRepository.save(serviceTable);
        return errorService.getErrorById("ER001");
    }

    // Update
    @Override
    public String update(Long id, ServiceTableDto dto) {
        ServiceTable entity = dtoToEntity.apply(dto);

        ServiceTable serviceTable = serviceTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceTable", "id", id));

        // Using Optional for Null check
        Optional.ofNullable(entity.getServiceName()).ifPresent((ServiceTable) -> serviceTable.setServiceName(entity.getServiceName()));
        Optional.ofNullable(entity.getServicePurpose()).ifPresent((ServiceTable) -> serviceTable.setServicePurpose(entity.getServicePurpose()));
        

        serviceTableRepository.save(serviceTable);
        return errorService.getErrorById("ER002");

    }


    // Hard Delete
    @Override
    public String harddelete(Long id) {
        ServiceTable serviceTable = serviceTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceTable", "id", id));
        serviceTableRepository.delete(serviceTable);
        return errorService.getErrorById("ER003");
    }

    
}
