package api.forohub.forohub.controller;

import api.forohub.forohub.domain.PagedResponse;
import api.forohub.forohub.domain.topic.*;
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
@RequestMapping("/topic")
@SecurityRequirement(name = "bearer-key")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicService topicService;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid TopicData topicData) throws IntegrityValidation {
        var finaldata = topicService.createTopic(topicData);
        return ResponseEntity.ok(finaldata);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<DataListTopics>> listOfTopics(@PageableDefault(size = 10) Pageable pageable) {
        Page<Topic> topicsPage = topicRepository.findByActiveTrue(pageable);
        Page<DataListTopics> dataListTopicsPage = topicsPage.map(DataListTopics::new);
        PagedResponse<DataListTopics> pagedResponse = new PagedResponse<>(dataListTopicsPage);
        return ResponseEntity.ok(pagedResponse);
    }

    @PutMapping
    @Transactional
    public ResponseEntity updateTopics(@RequestBody @Valid UpdateTopic updateTopic) {
        Topic topic = topicRepository.getReferenceById(updateTopic.id());
        topic.updateData(updateTopic);
        return ResponseEntity.ok(new ResponseTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getStatus(), topic.getAuthor().getId(), topic.getCourse(), topic.getDate()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteTopic(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        topic.diactivateTopic();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTopicData> topicReturn(@PathVariable Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        var dTopic = new ResponseTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getStatus(), topic.getAuthor().getId(), topic.getCourse(), topic.getDate());
        return ResponseEntity.ok(dTopic);
    }
}
