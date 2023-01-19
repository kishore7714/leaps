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

import com.leaps.dto.master.MortalityFlagMasterDto;
import com.leaps.responses.master.MortalityFlagMasterResponse;
import com.leaps.serviceImpl.master.MortalityFlagMasterServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mortalityFlagMaster")
public class MortalityFlagMasterController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
	private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
	private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
	private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

	@Autowired
	private MortalityFlagMasterServiceImpl mortalityFlagMasterService;

	// Get-all with Pagination
	@GetMapping("/getall-pagination")
	public ResponseEntity<MortalityFlagMasterResponse> getallWithPagination(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		HttpHeaders header = new HttpHeaders();
		MortalityFlagMasterResponse mortalityFlagMaster = mortalityFlagMasterService.getAllWithPagination(pageNo, pageSize, sortBy,
				sortDir);
		header.add("description", "MortalityFlagMaster-getall-pagination");
		return new ResponseEntity<>(mortalityFlagMaster, header, HttpStatus.OK);
		// return
		// ResponseEntity.status(HttpStatus.OK).headers(header).body(mortalityFlagMaster);
	}

	// Get-all
	@GetMapping("/getall")
	public ResponseEntity<List<MortalityFlagMasterDto>> getall() {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "getall");
		List<MortalityFlagMasterDto> mortalityFlagMaster = mortalityFlagMasterService.getall();
		return new ResponseEntity<>(mortalityFlagMaster, header, HttpStatus.OK);
	}

	// Get By Id
	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<MortalityFlagMasterDto> getbyid(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "get-by-id");
		header.add("type", "MortalityFlagMaster Object");
		MortalityFlagMasterDto mortalityFlagMaster = mortalityFlagMasterService.getbyid(id);
		return new ResponseEntity<>(mortalityFlagMaster, header, HttpStatus.OK);
	}

	// Add
	@PostMapping("/add")
	public ResponseEntity<String> add(@Valid @RequestBody MortalityFlagMasterDto mortalityFlagMasterDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "add-mortalityFlagMaster");
		String add = mortalityFlagMasterService.add(mortalityFlagMasterDto);
		return new ResponseEntity<>(add, header, HttpStatus.CREATED);
	}

	// Update
	@PatchMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @PathVariable Long id,
			@RequestBody MortalityFlagMasterDto mortalityFlagMasterDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "update-mortalityFlagMaster");
		String update = mortalityFlagMasterService.update(id, mortalityFlagMasterDto);
		return new ResponseEntity<>(update, header, HttpStatus.OK);
	}

	// Delete
	@PatchMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "soft-delete-mortalityFlagMaster");
		String softdelete = mortalityFlagMasterService.softdelete(id);
		return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
	}

	// Delete
	@DeleteMapping("/hard-delete/{id}")
	public ResponseEntity<String> hardDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "hard-delete-mortalityFlagMaster");
		String harddelete = mortalityFlagMasterService.harddelete(id);
		return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
	}

	// Global Search
	@GetMapping("/global-search/{key}")
	public ResponseEntity<List<MortalityFlagMasterDto>> globalSearch(String key) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "global-search-mortalityFlagMaster");
		List<MortalityFlagMasterDto> globalSearch = mortalityFlagMasterService.globalSearch(key);
		return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
	}
}
