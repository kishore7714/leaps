package com.leaps.controller.surrender;

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

import com.leaps.dto.surrender.SurrenderTransactionPasDto;
import com.leaps.responses.surrender.SurrenderTransactionPasResponse;
import com.leaps.serviceImpl.surrender.SurrenderTransactionPasServiceImpl;
import com.leaps.utilities.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/surrenderTransactionPas")
public class SurrenderTransactionPasController {

	private static final String DEFAULT_SORT_DIRECTION = AppConstants.DEFAULT_SORT_DIRECTION;
	private static final String DEFAULT_SORT_BY = AppConstants.DEFAULT_SORT_BY;
	private static final String DEFAULT_PAGE_SIZE = AppConstants.DEFAULT_PAGE_SIZE;
	private static final String DEFAULT_PAGE_NUMBER = AppConstants.DEFAULT_PAGE_NUMBER;

	@Autowired
	private SurrenderTransactionPasServiceImpl surrenderTransactionPasService;

	// Get-all with Pagination
	@GetMapping("/getall-pagination")
	public ResponseEntity<SurrenderTransactionPasResponse> getallWithPagination(
			@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		HttpHeaders header = new HttpHeaders();
		SurrenderTransactionPasResponse surrenderTransactionPas = surrenderTransactionPasService
				.getAllWithPagination(pageNo, pageSize, sortBy, sortDir);
		header.add("description", "SurrenderTransactionPas-getall-pagination");
		return new ResponseEntity<>(surrenderTransactionPas, header, HttpStatus.OK);
		// return
		// ResponseEntity.status(HttpStatus.OK).headers(header).body(surrenderTransactionPas);
	}

	// Get-all
	@GetMapping("/getall")
	public ResponseEntity<List<SurrenderTransactionPasDto>> getall() {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "getall");
		List<SurrenderTransactionPasDto> surrenderTransactionPas = surrenderTransactionPasService.getall();
		return new ResponseEntity<>(surrenderTransactionPas, header, HttpStatus.OK);
	}

	// Get By Id
	@GetMapping("/get-by-id/{id}")
	public ResponseEntity<SurrenderTransactionPasDto> getbyid(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "get-by-id");
		header.add("type", "SurrenderTransactionPas Object");
		SurrenderTransactionPasDto surrenderTransactionPas = surrenderTransactionPasService.getbyid(id);
		return new ResponseEntity<>(surrenderTransactionPas, header, HttpStatus.OK);
	}

	// Add
	@PostMapping("/add")
	public ResponseEntity<String> add(@Valid @RequestBody SurrenderTransactionPasDto surrenderTransactionPasDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "add-surrenderTransactionPas");
		String add = surrenderTransactionPasService.add(surrenderTransactionPasDto);
		return new ResponseEntity<>(add, header, HttpStatus.CREATED);
	}

	// Update
	@PatchMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @PathVariable Long id,
			@RequestBody SurrenderTransactionPasDto surrenderTransactionPasDto) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "update-surrenderTransactionPas");
		String update = surrenderTransactionPasService.update(id, surrenderTransactionPasDto);
		return new ResponseEntity<>(update, header, HttpStatus.OK);
	}

	// Delete
	@PatchMapping("/soft-delete/{id}")
	public ResponseEntity<String> softDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "soft-delete-surrenderTransactionPas");
		String softdelete = surrenderTransactionPasService.softdelete(id);
		return new ResponseEntity<>(softdelete, header, HttpStatus.OK);
	}

	// Delete
	@DeleteMapping("/hard-delete/{id}")
	public ResponseEntity<String> hardDelete(@PathVariable Long id) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "hard-delete-surrenderTransactionPas");
		String harddelete = surrenderTransactionPasService.harddelete(id);
		return new ResponseEntity<>(harddelete, header, HttpStatus.OK);
	}

	// Global Search
	@GetMapping("/global-search/{key}")
	public ResponseEntity<List<SurrenderTransactionPasDto>> globalSearch(String key) {
		HttpHeaders header = new HttpHeaders();
		header.add("description", "global-search-surrenderTransactionPas");
		List<SurrenderTransactionPasDto> globalSearch = surrenderTransactionPasService.globalSearch(key);
		return new ResponseEntity<>(globalSearch, header, HttpStatus.OK);
	}
}
