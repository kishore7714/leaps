package com.leaps.service.flc;

import java.util.List;

import com.leaps.dto.flc.FlcTransactionDetailsDto;
import com.leaps.responses.flc.FlcTransactionDetailsResponse;

public interface FlcTransactionDetailsService {

	public List<FlcTransactionDetailsDto> getall();

	public FlcTransactionDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public FlcTransactionDetailsDto getbyid(Long id);

	public String add(FlcTransactionDetailsDto dto);

	public String update(Long id, FlcTransactionDetailsDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<FlcTransactionDetailsDto> globalSearch(String key);
}
