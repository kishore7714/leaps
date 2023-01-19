package com.leaps.service.surrender;

import java.util.List;

import com.leaps.dto.surrender.SurrenderTransactionPasDto;
import com.leaps.responses.surrender.SurrenderTransactionPasResponse;

public interface SurrenderTransactionPasService {
	public List<SurrenderTransactionPasDto> getall();

	public SurrenderTransactionPasResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

	public SurrenderTransactionPasDto getbyid(Long id);

	public String add(SurrenderTransactionPasDto dto);

	public String update(Long id, SurrenderTransactionPasDto dto);

	public String softdelete(Long id);

	public String harddelete(Long id);

	public List<SurrenderTransactionPasDto> globalSearch(String key);
}
