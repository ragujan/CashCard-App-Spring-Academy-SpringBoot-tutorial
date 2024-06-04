package com.springAcademy.familyCashCardApp;
import org.h2.mvstore.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
interface CashCardRepository extends CrudRepository<CashCardEntity,Long>, PagingAndSortingRepository<CashCardEntity,Long >{
 
} 
