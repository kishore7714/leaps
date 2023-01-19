package com.leaps.service.admin;

import java.util.List;

import com.leaps.dto.admin.AddressDto;
import com.leaps.responses.admin.AddressResponse;

public interface AddressService {

	public List<AddressDto> getall();

	public AddressResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public AddressDto getbyid(Long id);

	public String add(AddressDto dto);

	public String update(Long id, AddressDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<AddressDto> globalSearch(String key);

}
