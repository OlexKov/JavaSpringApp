package org.example.interfaces;

import org.example.entities.Invoice;

import java.util.List;

public interface IInvoiceService {
    public Invoice saveInvice(Invoice invoice);
    public List<Invoice> getAllInvoices();
    public Invoice getInvoiceById(Long id);
    public void deleteInvoiceById(Long id);
    public void updateInvoice(Invoice invoice);
}
