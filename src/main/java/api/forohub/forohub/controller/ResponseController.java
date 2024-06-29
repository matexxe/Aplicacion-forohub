package api.forohub.forohub.controller;

import api.forohub.forohub.domain.response.*;
import api.forohub.forohub.domain.topic.TopicRepository;
import api.forohub.forohub.domain.user.UserRepository;
import api.forohub.forohub.infra.errors.IntegrityValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/response")
@SecurityRequirement(name="bearer-key")
public class ResponseController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private ResponseRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity responseRegister (@RequestBody @Valid ResponseData responseData) throws IntegrityValidation {
        // Create a new response using the provided data
        var finaldata = responseService.createResponse(responseData);

        // Return the created response data
        return ResponseEntity.ok(finaldata);
    }
    @GetMapping
    public ResponseEntity<Page<DataListResponses>>  listOfResponses(@PageableDefault(size = 10) Pageable paged){
        //return medicRepository.findAll(paged).map(DataListMedics::new);
        // Retrieve a pageable list of active responses
        return ResponseEntity.ok(repository.findByActiveTrue(paged).map(DataListResponses::new));
    }
    @PutMapping
    @Transactional
    public ResponseEntity updateResponse(@RequestBody @Valid UpdateResponseDetails updateResponseDetails){
        // Retrieve the response by ID (without loading it fully) for efficient update
        Response response=repository.getReferenceById(updateResponseDetails.id());

        // Update the response details
        response.responseUpdate(updateResponseDetails);

        // Update the response details
        return ResponseEntity.ok(new ResponseDataT(response.getId(),response.getSolution(),
                response.getAuthor().getId(),
                response.getTopic().getId(),
                response.getCreationdate()));
    }
    //logical delete
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteResponse(@PathVariable Long id){
        // Retrieve the response by ID (without loading it fully) for efficient deletion
        Response response = repository.getReferenceById(id);

        // Deactivate (logical delete) the response
        response.diactivateResponse();

        // Return a 204 No Content response indicating successful deletion
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity <ResponseDataT> responseReturn(@PathVariable Long id){
        // Retrieve the full response data by ID
        Response response=repository.getReferenceById(id);
        var dResponse = new ResponseDataT(response.getId(),
                response.getSolution(),
                response.getAuthor().getId(),
                response.getTopic().getId(),
                response.getCreationdate());

        // Return the response data
        return ResponseEntity.ok(dResponse);
    }
}
