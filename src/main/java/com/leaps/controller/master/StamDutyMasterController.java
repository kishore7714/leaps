package com.leaps.controller.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leaps.dto.master.StamDutyMasterDto;
import com.leaps.responses.master.StamDutyMasterResponse;
import com.leaps.serviceImpl.master.StamDutyMasterServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/stamDutyMaster")
public class StamDutyMasterController {
	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
	private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
	private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
	private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

	@Autowired
	private StamDutyMasterServiceImpl stamDutyMasterService;

	// Get-all with Pagination
	@GetMapping("/getall-pagination")
	public ResponseEntity<StamDutyMasterResponse> getallWithPagination(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		HttpHeaders header = new HttpHeaders();
		StamDutyMasterResponse stamDutyMaster = stamDutyMasterService.getAllWithPagination(pageNo, pageSize, sortBy,
				sortDir);
		header.add("description", "StamDutyMaster-getall-pagination");
		return new ResponseEntity<>(stamDutyMaster, header, HttpStatus.OK);
		// return
		// ResponseEntity.status(HttpStatus.OK).headers(header).body(stamDutyMaster);
	}

	// Get-all
	@GetMapping("/getall")
	public ResponseEntity<List<StamDutyMasterDto>> getall() {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "getall");
		List<StamDutyMasterDto> stamDutyMaster = stamDutyMasterService.getall();
		return new ResponseEntity<>(stamDutyMaster, header, HttpStatus.OK);
	}

	// Get By Id
	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<StamDutyMasterDto> getbyid(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "get-by-id");
		header.add("type", "StamDutyMaster Object");
		StamDutyMasterDto stamDutyMaster = stamDutyMasterService.getbyid(id);
		return new ResponseEntity<>(stamDutyMaster, header, HttpStatus.OK);
	}

	// Add
	@PostMapping("/add")
	public ResponseEntity<String> add(@Valid @RequestBody StamDutyMasterDto stamDutyMasterDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "add-stamDutyMaster");
		String add = stamDutyMasterService.add(stamDutyMasterDto);
		return new ResponseEntity<>(add, header, HttpStatus.CREATED);
	}

	// Update
	@PatchMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @PathVariable Long id,
			@RequestBody StamDutyMasterDto stamDutyMasterDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "update-stamDutyMaster");
		String update = stamDutyMasterService.update(id, stamDutyMasterDto);
		return new ResponseEntity<>(update, header, HttpStatus.OK);
	}

	// Delete
	@PatchMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "soft-delete-stamDutyMaster");
		String softdelete = stamDutyMasterService.softdelete(id);
		return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
	}

	// Delete
	@DeleteMapping("/hard-delete/{id}")
	public ResponseEntity<String> hardDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "hard-delete-stamDutyMaster");
		String harddelete = stamDutyMasterService.harddelete(id);
		return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
	}

	// Global Search
	@GetMapping("/global-search/{key}")
	public ResponseEntity<List<StamDutyMasterDto>> globalSearch(String key) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "global-search-stamDutyMaster");
		List<StamDutyMasterDto> globalSearch = stamDutyMasterService.globalSearch(key);
		return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
	}
}
