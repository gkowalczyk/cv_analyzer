package com.example.cv_analyzer.service;

import com.example.cv_analyzer.domain.Company;
import com.example.cv_analyzer.domain.JobOffer;
import com.example.cv_analyzer.domain.JobOfferDTO;
import com.example.cv_analyzer.repository.JobOfferRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;
    private final JobOfferRepo jobOfferRepo;


    public String chat(String message) {

        UserMessage userMessage = new UserMessage(message);
        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions
                        .builder()
                        .withFunction("getMyCv")
                        .withFunction("getOffersAdapter")
                        .build()));
        return response
                .getResult()
                .getOutput()
                .getContent();
    }


    public List<JobOfferDTO> llmExtractorOffers(String markdownOrText) {
        String prompt = """
                Zamień poniższą treść (może być w Markdown) na TABLICĘ JSON ofert pracy.
                ZWRÓĆ WYŁĄCZNIE poprawny JSON (RFC 8259), **bez** dodatkowego tekstu, komentarzy i backticków.
                Każdy element musi mieć dokładnie pola:
                {
                  "offerName": string,
                  "company": string,
                  "workplaceType": string|null,
                  "salaryRaw": string|null,
                  "link": string|null,
                  "requiredSkills": string[]
                }
                Treść do konwersji:
                ---
                %s
                ---
                """.formatted(markdownOrText);

        ChatResponse resp = chatModel.call(new Prompt(
                List.of(new UserMessage(prompt)),
                OpenAiChatOptions
                        .builder()
                        .withTemperature(0.0)
                        .build()));

        String raw = resp.getResult()
                .getOutput()
                .getContent();

        try {
            return objectMapper.readValue(raw, new TypeReference<List<JobOfferDTO>>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("The model did not return valid JSON, received:\n" + raw, e);
        }
    }

    public List<JobOffer> saveDataToDb(String result) {
        var dTos = llmExtractorOffers(result);
        var entities = dTos.stream()
                .map(dto -> JobOffer.builder()
                        .offerName(dto.getOfferName())
                        .company(Company.builder().company(dto.getCompany().getCompany()).build())
                        .workplaceType(dto.getWorkplaceType())
                        .salaryRaw(dto.getSalaryRaw())
                        .link(dto.getLink())
                        .requiredSkills(dto.getRequiredSkills())
                        .build()
                ).toList();
        log.info("Save entities to DB: {}", entities);
       jobOfferRepo.saveAll(entities);
       return entities;
    }
}

