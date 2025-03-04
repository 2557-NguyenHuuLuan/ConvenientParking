package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.RentalPackage;
import com.example.convenientparking.Services.RentalPackageService;
import com.example.convenientparking.Services.StatisticsService.PackageStatisticsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/package")
public class PackageController {
    @Autowired
    private PackageStatisticsService packageStatisticsService;
    @Autowired
    private RentalPackageService rentalPackageService;

    @GetMapping("/list-form")
    public String listForm(@NotNull Model model) {
        List<RentalForm> rentalForms = packageStatisticsService.getRentalForms();
        List<Long> dataRental = packageStatisticsService.countRentalAmount(rentalForms);
        List<Long> dataRevenue = packageStatisticsService.revenueForRentalForm(rentalForms);
        List<String> labels = rentalForms.stream()
                .map(RentalForm::getForm)
                .collect(Collectors.toList());
        model.addAttribute("rentalForms", rentalForms);
        model.addAttribute("dataRental", dataRental);
        model.addAttribute("dataRevenue", dataRevenue);
        model.addAttribute("labels", labels);
        return "employee/FormAndPackage/listForm";
    }

    @PostMapping("/rental-form/update-price")
    public String updatePrice(@RequestParam Long id, @RequestParam Long pricePer) {
        RentalForm rentalForm = rentalPackageService.getRentalFormById(id);
        if (rentalForm != null) {
            rentalForm.setPricePer(pricePer);
            rentalPackageService.saveRentalForm(rentalForm);
            return "redirect:/package/list-form";
        }
        return "redirect:/package/list-form";
    }


    @GetMapping("/list-form/package")
    public String listFormPackage(@RequestParam("formId") Long formId, @NotNull Model model) {
        RentalForm rentalForm = packageStatisticsService.getRentalFormById(formId);
        List<RentalPackage> rentalPackages = rentalForm.getRentalPackages();
        List<Long> dataRental = packageStatisticsService.countRentalAmountForRentalPackage(rentalPackages);
        List<String> labels = new ArrayList<>();
        for(RentalPackage r : rentalPackages) {
            Long numberOf = r.getNumberOf();
            String form = r.getRentalForm().getForm();
            if ("HOUR".equalsIgnoreCase(form)) {
                form = "giờ";
            } else if ("DAY".equalsIgnoreCase(form)) {
                form = "ngày";
            } else if ("MONTH".equalsIgnoreCase(form)) {
                form = "tháng";
            } else if ("YEAR".equalsIgnoreCase(form)) {
                form = "năm";
            }
            String label = numberOf + " " + form;
            labels.add(label);
        }
        model.addAttribute("dataRental", dataRental);
        model.addAttribute("labels", labels);
        model.addAttribute("rentalForm", rentalForm);
        model.addAttribute("package",  rentalPackages);
        return "employee/FormAndPackage/listPackage";
    }

    @PostMapping("/add-rental-package")
    public String addRentalPackage(@RequestParam("rentalFormId") Long rentalFormId,
                                   @RequestParam("numberOf") Long numberOf,
                                   Model model) {
        RentalPackage rentalPackage = new RentalPackage();
        rentalPackage.setNumberOf(numberOf);
        RentalPackage savedRentalPackage = rentalPackageService.addRentalPackage(rentalFormId, rentalPackage);
        if (savedRentalPackage != null) {
            model.addAttribute("message", "Gói thuê đã được thêm thành công!");
        } else {
            model.addAttribute("message", "Lỗi: Gói thuê với số lượng này đã tồn tại hoặc không tìm thấy RentalForm.");
        }
        List<RentalPackage> rentalPackages = rentalPackageService.getRentalPackagesByRentalFormID(rentalFormId);
        model.addAttribute("rentalPackages", rentalPackages);
        model.addAttribute("rentalFormId", rentalFormId);

        return "redirect:/package/list-form/package?formId=" + rentalFormId;
    }



}
