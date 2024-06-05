package com.springAcademy.familyCashCardApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
interface CashCardRepository extends CrudRepository<CashCardEntity,Long>, PagingAndSortingRepository<CashCardEntity,Long >{
    
    CashCardEntity findByIdAndOwner(Long id, String owner);
    Page<CashCardEntity> findByOwner(String owner, PageRequest pageRequest);
    boolean existsByIdAndOwner(Long id, String owner);
    
} 
