package com.leaps.responses.master;

import java.util.List;

import com.leaps.dto.master.StamDutyMasterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StamDutyMasterResponse {
	private List<StamDutyMasterDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
