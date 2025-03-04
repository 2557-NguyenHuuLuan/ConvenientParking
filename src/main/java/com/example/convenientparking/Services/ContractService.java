package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.*;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.ContractRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public List<Contract> getContractsByUser(User user) {
        return contractRepository.findByUser(user);
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract findContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with ID " + id + " not found"));
    }

    public List<ContractDetail> getAllContractDetailByContractId(Long contractId) {
        return contractDetailRepository.findByContractId(contractId);
    }

    public Contract createContract(User user){
        Contract contract = new Contract();
        contract.setUser(user);
        contract.setValue(0L);
        contract.setPaymentStatus(false);
        contract.setStatus(false);
        contract.setValidity("Chưa có hiệu lực");
        contract.setWrittenOn(LocalDate.now());
        contractRepository.save(contract);
        return contract;
    }

    public ContractDetail addMoreDetail( Contract contract,
                                         RentalPackage rentalPackage,
                                         ParkingSpot parkingSpot,
                                         Vehicle vehicle,
                                         LocalDate rentalStart,
                                         LocalTime rentalStartTime){
        ContractDetail contractDetail = new ContractDetail();
        contractDetail.setContract(contract);
        contractDetail.setRentalPackage(rentalPackage);
        contractDetail.setParkingSpot(parkingSpot);
        contractDetail.setVehicle(vehicle);
        contractDetail.setPrice(priceContractDetail(rentalPackage, vehicle));
        contractDetail.setStatus(false);
        contractDetail.setRentalStart(rentalStart);
        contractDetail.setRentalStartTime(rentalStartTime);
        if("HOUR".equals(rentalPackage.getRentalForm().getForm())){
            LocalTime rentalEndTime = rentalStartTime.plusHours(rentalPackage.getNumberOf());
            if (rentalEndTime.isBefore(rentalStartTime)) {
                contractDetail.setRentalEnd(rentalStart.plusDays(1));
                contractDetail.setRentalEndTime(rentalEndTime.minusHours(24));
            } else {
                contractDetail.setRentalEnd(rentalStart);
                contractDetail.setRentalEndTime(rentalEndTime);
            }
        }else if("DAY".equals(rentalPackage.getRentalForm().getForm())){
            contractDetail.setRentalEnd(rentalStart.plusDays(rentalPackage.getNumberOf()));
            contractDetail.setRentalEndTime(LocalTime.MIDNIGHT);
        }else if("MONTH".equals(rentalPackage.getRentalForm().getForm())){
            contractDetail.setRentalEnd(rentalStart.plusMonths(rentalPackage.getNumberOf()));
            contractDetail.setRentalEndTime(LocalTime.MIDNIGHT);
        }else if("YEAR".equals(rentalPackage.getRentalForm().getForm())){
            contractDetail.setRentalEnd(rentalStart.plusYears(rentalPackage.getNumberOf()));
            contractDetail.setRentalEndTime(LocalTime.MIDNIGHT);
        }
        contractDetailRepository.save(contractDetail);
        return contractDetail;
    }

    public Long priceContractDetail(RentalPackage rentalPackage, Vehicle vehicle){
        Long price = 0L;
        price = rentalPackage.getNumberOf() * rentalPackage.getRentalForm().getPricePer();
        price = (long) (price * vehicle.getVehicleType().getCoefficient());
        return price;
    }

    public Long contractValue(Long contractId) {
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractId(contractId);
        Long total = (contractDetails == null || contractDetails.isEmpty())
                ? 0L
                : contractDetails.stream()
                .mapToLong(ContractDetail::getPrice)
                .sum();

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
        contract.setValue(total);
        contractRepository.save(contract);
        return total;
    }

    @Transactional
    public void deleteOverdueContracts() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);
        List<Contract> overdueContracts = contractRepository.findContractOverdate(cutoffDate);

        if (!overdueContracts.isEmpty()) {
            contractRepository.deleteAll(overdueContracts);
            System.out.println("Các hợp đồng quá hạn thanh toán đã được thanh tẩy.");
        }else {
            System.out.println("Không có hợp đồng quá hạn thanh toán.");
        }
    }
    @Transactional
    public void voidExpiredContract(){
        List<Contract> contracts = contractRepository.findAll();
        for (Contract contract : contracts) {
            if (contract.isStatus()) {
                List<ContractDetail> details = contractDetailRepository.findByContractId(contract.getId());
                boolean allDetailsInactive = details.stream().allMatch(detail -> !detail.isStatus());
                if (allDetailsInactive) {
                    contract.setValidity("Hết hiệu lực");
                    contract.setStatus(false);
                    contractRepository.save(contract);
                }
            }
        }
    }
    @Transactional
    public void checkContractDetail(){
        List<ContractDetail> contractDetails = contractDetailRepository.findAll();
        for (ContractDetail contractDetail : contractDetails) {
            if(contractDetail.isStatus()){
                if("HOUR".equals(contractDetail.getRentalPackage().getRentalForm().getForm())){
                    contractDetail.setStatus(checkHourlyContractDetail(contractDetail));
                    contractDetailRepository.save(contractDetail);
                }else{
                    contractDetail.setStatus(checkDateContractDetail(contractDetail));
                    contractDetailRepository.save(contractDetail);
                }
            }
        }
    }

    public boolean checkHourlyContractDetail(ContractDetail contractDetail){
        if(contractDetail.getRentalStart().isEqual(contractDetail.getRentalEnd()) &&
                contractDetail.getRentalStart().isEqual(LocalDate.now())){
            if(contractDetail.getRentalEndTime().isBefore(LocalTime.now())){
                return false;
            }
        }else{
            if(contractDetail.getRentalEnd().isEqual(LocalDate.now())){
                if(contractDetail.getRentalEndTime().isBefore(LocalTime.now())){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkDateContractDetail(ContractDetail contractDetail){
        if(contractDetail.getRentalEnd().isBefore(LocalDate.now())){
            return false;
        }
        return true;
    }

    @PostConstruct
    public void init() {
        checkContractDetail();
        deleteOverdueContracts();
        voidExpiredContract();
    }

}
