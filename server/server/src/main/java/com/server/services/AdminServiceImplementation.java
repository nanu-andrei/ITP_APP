package com.server.services;

import com.server.DTO.EmployeeCredentialsDTO;

import com.server.DTO.RequestDTO;
import com.server.DTO.RequestDataDTO;
import com.server.DTO.RequestInfoDTO;
import com.server.Entity.EmployeeCredentials;
import com.server.Entity.Firm;
import com.server.Entity.Request;

import com.server.Repository.EmployeeCredentialsRepository;
import com.server.Repository.FirmRepository;
import com.server.Repository.UserRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImplementation.class);

    public final EmployeeCredentialsRepository employeeCredentialsRepository;
    public final FirmRepository firmRepository;
    public final UserRequestRepository requestRepository;
    public final WalletService walletService;
    public final ContractServiceImplementation contractService;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AdminServiceImplementation(EmployeeCredentialsRepository employeeCredentialsRepository, FirmRepository firmRepository, UserRequestRepository requestRepository, WalletService walletService, ContractServiceImplementation contractService, EntityManager entityManager) {
        this.employeeCredentialsRepository = employeeCredentialsRepository;
        this.contractService = contractService;
        this.entityManager = entityManager;
        this.firmRepository = firmRepository;
        this.requestRepository = requestRepository;
        this.walletService = walletService;
        logger.debug("AdminServiceImplementation initialized with repositories.");
    }

    @Transactional
    @Override
    public void addInspectorToFirm(String firmName, List<EmployeeCredentialsDTO> inspectors) throws Exception{
        Firm firm = firmRepository.findByFirmName(firmName);
        EmployeeCredentials admin = firm.getAdmin();
        if (firm != null) {
            for(EmployeeCredentialsDTO inspector : inspectors) {
                inspector.setFirm(firm);
                EmployeeCredentials inspector_e =convertToEntity(inspector);
                employeeCredentialsRepository.saveAndFlush(inspector_e);
                walletService.createWallet(inspector.getUsername() + "WP", inspector_e);
                List<Object> param = new ArrayList<>();
                param.add(inspector_e.getWallet().getAddress());
                contractService.executeTransaction( admin.getWallet().getAddress(),"addInspector",param);
                firm.getEmployeeCredentials().add(inspector_e);

            }
            firmRepository.saveAndFlush(firm);
        }
    }
    @Transactional
    @Override
    public void removeInspectorFromFirm(List<EmployeeCredentialsDTO> employeeCredentialsDTOS) {
        for(EmployeeCredentialsDTO employeeCredentialsDTO: employeeCredentialsDTOS) {
            Firm firm = firmRepository.findById(employeeCredentialsDTO.getFirm().getId()).orElse(null);
            EmployeeCredentials credentials = convertToEntity(employeeCredentialsDTO);
            EmployeeCredentials admin = firm.getAdmin();
            if (firm != null && credentials != null && firm.getEmployeeCredentials().contains(credentials)) {
                List<Object> param = new ArrayList<>();
                param.add(credentials.getWallet().getAddress());
                try{contractService.executeTransaction( admin.getWallet().getAddress(),"removeInspector",param);}catch (Exception e){e.printStackTrace();}
                firm.getEmployeeCredentials().remove(credentials);
                firmRepository.save(firm);
                employeeCredentialsRepository.delete(credentials);
            }
        }
    }
    @Transactional
    @Override
    public void assignContract(String inspectorName, String adminName, Long requestId) {

        adminName = adminName.trim();
        logger.debug("Attempting to find admin with username: \"{}\"", adminName);

        EmployeeCredentials admin = employeeCredentialsRepository.findByUsername(adminName);
        logger.debug("Query executed. Result: {}", admin != null ? admin.getUsername() : "null");

        if (admin != null) {
            logger.debug("Admin found: {}", admin.getUsername());
            entityManager.refresh(admin);
        } else {
            logger.debug("Admin not found in initial query.");
        }

        if (admin == null) {
            throw new IllegalArgumentException("No admin instantiated");
        }
        System.out.println(inspectorName);
        EmployeeCredentials inspector = employeeCredentialsRepository.findByUsername(inspectorName);
        if(inspector==null)
        {
            throw new IllegalArgumentException("No inspector instanciated");
        }
        System.out.println(requestId);
        Request request = requestRepository.findById(requestId).orElse(null);
        System.out.println("vin is "+request.getDetails().getVin());
        if(request!=null)
        {
            if(inspector.getFirm().getId().equals(admin.getFirm().getId())) {
                System.out.println("Inspector is"+inspector.getFirm().getId());
                System.out.println("Admin is "+admin.getFirm().getId());
                System.out.println("The request is from admin"+ request.getFirm().getId());
                if (admin.getFirm().getId().equals(request.getFirm().getId())) {
                    request.setInspector(inspector);
                    if(requestRepository.findAllByDetailsVin(request.getDetails().getVin()).size()==1)
                    {
                        try{
                            contractService.deployContract("car", request.getDetails().getVin());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println("The inspector set is:"+request.getInspector());
                    requestRepository.save(request);
                }
            }
            else
            {
                throw  new IllegalArgumentException("Admin and inspector don't belong to he same firm");
            }
        }
        else
        {
            throw new IllegalArgumentException("No request available with this id");
        }

    }

    @Override
    public List<RequestDTO> viewRequests(String firmName) {
        List<RequestDTO> view = new ArrayList<>();
        List<Request> requests = requestRepository.findByFirmFirmName(firmName);
        Firm firm = firmRepository.findByFirmName(firmName);
        for(Request request : requests) {
            if (firm.getRequests().contains(request)) {
                view.add(convertToDTO(request));
            }
        }
        return view;
    }


    public EmployeeCredentials convertToEntity(EmployeeCredentialsDTO credentialsDTO)
    {
        EmployeeCredentials credentials = new EmployeeCredentials();
        credentials.setRole(credentialsDTO.getRole());
        credentials.setFirm(credentialsDTO.getFirm());
        credentials.setUsername(credentialsDTO.getUsername());
        credentials.setWallet(credentialsDTO.getWallet());
        return credentials;
    }
    private RequestDTO convertToDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        RequestDataDTO requestDataDTO = new RequestDataDTO();
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();

        requestDataDTO.setDetails(request.getDetails());
        requestInfoDTO.setUserData(request.getUserData());
        requestInfoDTO.setInspector(request.getInspector());
        requestInfoDTO.setFirm(request.getFirm());

        requestDTO.setRequestData(requestDataDTO);
        requestDTO.setRequestInfo(requestInfoDTO);

        return requestDTO;
    }
}
