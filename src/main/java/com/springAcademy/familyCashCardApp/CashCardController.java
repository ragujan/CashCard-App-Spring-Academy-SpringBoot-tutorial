package com.springAcademy.familyCashCardApp;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
	private CashCardRepository cashCardRepository;

	private CashCardController(CashCardRepository repository) {
		this.cashCardRepository = repository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<CashCardEntity> findById(@PathVariable Long requestedId) {
		Optional<CashCardEntity> cashCardOptional = cashCardRepository.findById(requestedId);

		if (cashCardOptional.isPresent()) {
			return ResponseEntity.ok(cashCardOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/fake/{requestedId}")
	private String findById2(@PathVariable Long requestedId) {
		return Long.toString(requestedId);
	}

	@PostMapping
	private ResponseEntity<Void> createCashCard(@RequestBody CashCardEntity newCashCardRequest,
			UriComponentsBuilder ucb) {
		CashCardEntity savedCard = cashCardRepository.save(newCashCardRequest);
		URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCard.getId()).toUri();
		return ResponseEntity.created(locationOfNewCashCard).build();
	}

	@GetMapping
	private ResponseEntity<List<CashCardEntity>> findAll(Pageable pageable) {
		Page<CashCardEntity> page = cashCardRepository.findAll(
				PageRequest.of(
				        pageable.getPageNumber(),
				        pageable.getPageSize(),
				        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
	    ));
	    return ResponseEntity.ok(page.getContent());
	}

}
