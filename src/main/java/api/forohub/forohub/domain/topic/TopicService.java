package api.forohub.forohub.domain.topic;

import api.forohub.forohub.domain.user.UserRepository;
import api.forohub.forohub.infra.errors.IntegrityValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseTopicData createTopic(TopicData topicData) throws IntegrityValidation {
        // Verificar si el usuario está registrado
        userRepository.findById(topicData.idUser())
                .orElseThrow(() -> new IntegrityValidation("El usuario con ID proporcionado no está registrado"));

        // Verificar si el título ya existe (ignorando mayúsculas y minúsculas)
        if (topicRepository.existsByTitleIgnoreCase(topicData.title())) {
            throw new IntegrityValidation("El título ya está presente en la base de datos. Revisa el tema existente.");
        }

        // Verificar si el mensaje ya existe (ignorando mayúsculas y minúsculas)
        if (topicRepository.existsByMessageIgnoreCase(topicData.message())) {
            throw new IntegrityValidation("El mensaje ya está presente en la base de datos. Revisa el tema existente.");
        }

        // Obtener el usuario de la base de datos
        var user = userRepository.findById(topicData.idUser()).get();

        // Crear y guardar el nuevo topic
        var topic = new Topic(
                null,
                topicData.title(),
                topicData.message(),
                topicData.date(),
                topicData.status(),
                user,
                topicData.course(),
                true
        );

        topicRepository.save(topic);

        // Retornar los datos de respuesta del topic creado
        return new ResponseTopicData(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getStatus(), topic.getAuthor().getId(), topic.getCourse(), topic.getDate());
    }
}
