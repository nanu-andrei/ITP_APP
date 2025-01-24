package com.server.services;


import com.server.DTO.*;
import com.server.Entity.Firm;
import com.server.Entity.UserData;
import com.server.Entity.Request;
import com.server.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.server.Entity.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);
    public final UserDataRepository userDataRepository;
    public final  UserCredentialsRepository userCredentialsRepository;
    public final UserRequestRepository userRequestRepository;
    public final FirmRepository firmRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImplementation(UserDataRepository userDataRepository, UserCredentialsRepository userCredentialsRepository, UserRequestRepository userRequestRepository, FirmRepository firmRepository)
    {
        this.userDataRepository=userDataRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userRequestRepository=userRequestRepository;
        this.firmRepository = firmRepository;
    }
    @Override
    @Transactional
    public UserSignupDTO signup(UserSignupDTO signupDTO)
    {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(signupDTO.getSensitiveDataDTO().getUsername());
        credentials.setPassword(passwordEncoder.encode(signupDTO.getSensitiveDataDTO().getPassword()));
        credentials = userCredentialsRepository.save(credentials);

        UserData userData = new UserData();
        userData.setCnp(signupDTO.getDataDTO().getCnp());
        userData.setFirstName(signupDTO.getDataDTO().getFirstName());
        userData.setLastName(signupDTO.getDataDTO().getLastName());
        userData.setGender(signupDTO.getDataDTO().getGender());
        userData.setDateOfBirth(signupDTO.getDataDTO().getDateOfBirth());
        userData.setCredentials(credentials);
        userDataRepository.save(userData);

        return signupDTOConverter(userData,credentials);
    }

    @Override
    @Transactional
    public DataDTO login(String username, String  password) {
            logger.info("Received login request for username: {}", username);
            UserCredentials user = userCredentialsRepository.findByUsername(username);
            UserData userData = userDataRepository.findByCredentials(user);
            if(userData!=null)
            {
                if( passwordEncoder.matches(password, user.getPassword())){
                    logger.info("User {} succesfully logged in", username);
                    return converToDTO(userData);
                }
                else{
                    logger.warn("Invalid wallet_address for user: {}", username);
                }
            }
            else{
                logger.warn("User not found: {}", username);
            }
            throw new BadCredentialsException("Login Failed");
    }


    public DataDTO converToDTO (UserData userData)
    {
        DataDTO userDataDTO = new DataDTO();
        userDataDTO.setId(userData.getId());
        userDataDTO.setFirstName(userData.getFirstName());
        userDataDTO.setLastName(userData.getLastName());
        userDataDTO.setCnp(userData.getCnp());
        userDataDTO.setDateOfBirth(userData.getDateOfBirth());
        userDataDTO.setGender(userData.getGender());

        return userDataDTO;
    }

    public UserSignupDTO signupDTOConverter(UserData userData , UserCredentials credentials)
    {
        UserSignupDTO signupDTO = new UserSignupDTO();
        SensitiveDataDTO sensitiveDataDTO = new SensitiveDataDTO();
        sensitiveDataDTO.setUsername(credentials.getUsername());
        signupDTO.setSensitiveDataDTO(sensitiveDataDTO);

        DataDTO dataDTO = new DataDTO();
        dataDTO.setId(userData.getId());
        signupDTO.setDataDTO(dataDTO);
        return signupDTO;
    }



    public RequestDTO request(String firm, RequestDTO requestDTO) {
        Firm firmEntity = firmRepository.findByFirmName(firm);
        System.out.println(firmEntity.getFirmName());
        Request request = convertToEntity(requestDTO);
        request.setFirm(firmEntity);
        request.setStatus("Pending");

        Request savedRequest = userRequestRepository.save(request);
        return convertToDTO(savedRequest);
    }

    @Override
    public List<RequestDTO> getAllRequestsForUser(Long userId) {
        UserData user = userDataRepository.findById(userId).orElse(null);
        List<Request> requests = userRequestRepository.findByUserData(user);
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for(Request request: requests)
        {
            RequestDTO requestDTO= convertToDTO(request);
            requestDTOS.add(requestDTO);
        }

        return requestDTOS;
    }


    private Request convertToEntity(RequestDTO requestDTO) {
        Request request = new Request();
        request.setUserData(requestDTO.getRequestInfo().getUserData());
        request.setFirm(requestDTO.getRequestInfo().getFirm());
        request.setInspector(requestDTO.getRequestInfo().getInspector());

        Request.Details details = new Request.Details();
        details.setVin(requestDTO.getRequestData().getDetails().getVin());
        details.setManufacturer(requestDTO.getRequestData().getDetails().getManufacturer());
        details.setModel(requestDTO.getRequestData().getDetails().getModel());
        details.setProductionYear(requestDTO.getRequestData().getDetails().getProductionYear());
        details.setFuelType(requestDTO.getRequestData().getDetails().getFuelType());
        details.setPlateNumber(requestDTO.getRequestData().getDetails().getPlateNumber());
        details.setColor(requestDTO.getRequestData().getDetails().getColor());
        request.setDetails(details);

        return request;
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
