package org.example.controllers;

import org.example.entities.Invoice;
import org.example.exceptions.InvoiceNotFoundException;
import org.example.interfaces.IInvoiceService;
import org.example.interfaces.ISorangeService;
import org.example.models.InvoiceCreationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private IInvoiceService service;
    @Autowired
    private ISorangeService storageService;

    @GetMapping("/")
    public String showHomePage() {
        return "homePage";
    }

    @GetMapping("/register")
    public String showRegistration() {
        return "registerInvoicePage";
    }

    @PostMapping("/save")
    public String saveInvoice(
            @ModelAttribute InvoiceCreationModel invoiceModel,
            Model model
    ) {
        Invoice invoice = new Invoice(
                invoiceModel.getId(),
                invoiceModel.getName(),
                invoiceModel.getLocation(),
                invoiceModel.getAmount(),""
        );
        invoice.setFileName(storageService.saveFile(invoiceModel.getFile()));
        service.saveInvice(invoice);
        Long id = service.saveInvice(invoice).getId();
        String message = "Record with id : '"+id+"' is saved successfully !";
        model.addAttribute("message", message);
        return "registerInvoicePage";
    }

    @GetMapping("/getAllInvoices")
    public String getAllInvoices(
            @RequestParam(value = "message", required = false) String message,
            Model model
    ) {
        List<Invoice> invoices= service.getAllInvoices();
        model.addAttribute("list", invoices);
        model.addAttribute("message", message);
        return "allInvoicesPage";
    }

    @GetMapping("/edit")
    public String getEditPage(
            Model model,
            RedirectAttributes attributes,
            @RequestParam Long id
    ) {
        String page = null;
        try {
            Invoice invoice = service.getInvoiceById(id);
            model.addAttribute("invoice", invoice);
            page="editInvoicePage";
        } catch (InvoiceNotFoundException e) {
            e.printStackTrace();
            attributes.addAttribute("message", e.getMessage());
            page="redirect:getAllInvoices";
        }
        return page;
    }

    @PostMapping("/update")
    public String updateInvoice(
            @ModelAttribute InvoiceCreationModel invoiceModel,
            RedirectAttributes attributes
    ) {
        Invoice invoice = new Invoice(
                invoiceModel.getId(),
                invoiceModel.getName(),
                invoiceModel.getLocation(),
                invoiceModel.getAmount(),""
        );
        if(invoiceModel.getFile() != null){
            String fileName = service.getInvoiceById(invoice.getId()).getFileName();
            storageService.deleteFile(fileName);
            invoice.setFileName(storageService.saveFile(invoiceModel.getFile()));
        }
        service.updateInvoice(invoice);
        Long id = invoice.getId();
        attributes.addAttribute("message", "Invoice with id: '"+id+"' is updated successfully !");
        return "redirect:getAllInvoices";
    }

    @GetMapping("/delete")
    public String deleteInvoice(
            @RequestParam Long id,
            RedirectAttributes attributes
    ) {
        try {
            String fileName = service.getInvoiceById(id).getFileName();
            service.deleteInvoiceById(id);
            storageService.deleteFile(fileName);
            attributes.addAttribute("message", "Invoice with Id : '"+id+"' is removed successfully!");
        } catch (InvoiceNotFoundException e) {
            e.printStackTrace();
            attributes.addAttribute("message", e.getMessage());
        }
        return "redirect:getAllInvoices";
    }
}
