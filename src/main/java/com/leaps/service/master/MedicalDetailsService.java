package com.leaps.service.master;

import java.util.List;

import com.leaps.dto.master.MedicalDetailsDto;
import com.leaps.responses.master.MedicalDetailsResponse;

public interface MedicalDetailsService {
	public List<MedicalDetailsDto> getall();

    public MedicalDetailsResponse getAllWithPagination(int pageNo, int pageSize, String sortBy, String sortDir);

    public MedicalDetailsDto getbyid(Long id);

    public String add(MedicalDetailsDto dto);

    public String update(Long id, MedicalDetailsDto dto);

    public String softdelete(Long id);

    public String harddelete(Long id);

    public List<MedicalDetailsDto> globalSearch(String key);
}
