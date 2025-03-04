package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository  extends JpaRepository<Invoice, Long> {
}
