package com.springAcademy.familyCashCardApp;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
	private CashCardRepository cashCardRepository;

	private CashCardController(CashCardRepository repository) {
		this.cashCardRepository = repository;
	}

	@GetMapping("/{requestedId}")
	private ResponseEntity<CashCardEntity> findById(@PathVariable Long requestedId, Principal principal) {
		CashCardEntity cashCard = findCashCard(requestedId, principal);
		if (cashCard != null) {
			return ResponseEntity.ok(cashCard);
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
			UriComponentsBuilder ucb, Principal principal) {
		CashCardEntity savedCard = cashCardRepository
				.save(new CashCardEntity(newCashCardRequest.getAmount(), principal.getName()));
		URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCard.getId()).toUri();
		return ResponseEntity.created(locationOfNewCashCard).build();
	}

	@GetMapping
	private ResponseEntity<List<CashCardEntity>> findAll(Pageable pageable, Principal prn) {

		Page<CashCardEntity> page = cashCardRepository.findByOwner(prn.getName(),
				PageRequest.of(pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
		return ResponseEntity.ok(page.getContent());
	}

	@PutMapping("/{requestedId}")
	public ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCardEntity cashCardUpdate,
			Principal principal) {
		CashCardEntity cashCardEntity = findCashCard(requestedId, principal);
		if (cashCardEntity != null) {
			CashCardEntity updatedCardEntity = new CashCardEntity(cashCardEntity.getId(), cashCardUpdate.getAmount(),
					principal.getName());
			cashCardRepository.save(updatedCardEntity);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	private CashCardEntity findCashCard(Long requestedId, Principal principal) {
		return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
	}

	@DeleteMapping("/{id}")
	private ResponseEntity deleteCashCard(@PathVariable Long id, Principal principal) {

		if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
			cashCardRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
}
