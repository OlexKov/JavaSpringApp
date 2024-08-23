package org.example.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.entities.Invoice;
import org.example.exceptions.InvoiceNotFoundException;
import org.example.interfaces.IInvoiceService;
import org.example.interfaces.ISorangeService;
import org.example.models.InvoiceCreationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        Invoice invoice = service.getInvoiceById(invoiceModel.getId());
        invoice.setName(invoiceModel.getName());
        invoice.setLocation(invoiceModel.getLocation());
        invoice.setAmount( invoiceModel.getAmount());

        if(invoiceModel.getFile() != null){
            storageService.deleteFile(invoice.getFileName());
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

    @RequestMapping(value = "/files")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestParam String file) throws IOException {
        FileSystemResource fileResource = new FileSystemResource(storageService.getFile(file));
        if (!fileResource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file );

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileResource);
    }
}
