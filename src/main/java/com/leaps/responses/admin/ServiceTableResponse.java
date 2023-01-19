package com.leaps.responses.admin;

import java.util.List;

import com.leaps.dto.admin.ServiceTableDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTableResponse {
	private List<ServiceTableDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
