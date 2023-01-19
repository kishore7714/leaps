package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.ServiceTableDto;
import com.leaps.responses.admin.ServiceTableResponse;

public interface ServiceTableService {
	 public List<ServiceTableDto> getall();

	    public ServiceTableResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	    public ServiceTableDto getbyid(Long id);

	    public String add(ServiceTableDto dto);

	    public String update(Long id, ServiceTableDto dto);

	 

	    public String harddelete(Long id);

}
