package com.auto.trade.services;

import com.auto.trade.entity.OrderPrice;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface OrderPriceRepository extends CrudRepository<OrderPrice, Long> {

}
