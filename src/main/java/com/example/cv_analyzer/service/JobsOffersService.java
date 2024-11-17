package com.example.cv_analyzer.service;

import com.example.cv_analyzer.domain.JobOffer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobsOffersService implements Function<JobsOffersService.Request, List<JobOffer>> {

    public record Request(long limit) {
    }

    @SneakyThrows
    @Override
    public List<JobOffer> apply(JobsOffersService.Request request) {
        return getJobsOffers(request.limit);

    }

    public List<JobOffer> getJobsOffers(long limit) throws IOException {

        org.jsoup.nodes.Document doc = Jsoup.connect("https://justjoin.it/job-offers/krakow/java?experience-level=junior").get();

        Element firstJobOffer = doc.select("script[type=application/json]").first();
        if (firstJobOffer == null) {
            throw new IOException("No job offers found");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(firstJobOffer.data())
                .get("props")
                .get("pageProps")
                .get("dehydratedState")
                .get("queries")
                .get(0)
                .get("state")
                .get("data")
                .get("pages")
                .get(0)
                .get("data");

        List<JobOffer> jobOffersList = convertJsonNodeToList(jsonNode);

        List<JobOffer> collect = jobOffersList.stream()
                .limit(limit)
                .collect(Collectors.toList());
        collect.forEach(System.out::println);
        return collect;
    }

    public static List<JobOffer> convertJsonNodeToList(JsonNode jsonNode) {
        List<JobOffer> jobOffersList = new ArrayList<>();

        for (JsonNode node : jsonNode) {
            JobOffer jobOffer = new JobOffer();
            jobOffer.setCompanyName(node.has("companyName")
                    && !node.get("companyName").isNull()
                    ? node.get("companyName").asText() : null);
            if (node.has("employmentTypes")
                    && !node.get("employmentTypes").isNull()) {

                JsonNode employmentNode = node.get("employmentTypes").get(0);

                if (employmentNode.has("fromPln")
                        && !employmentNode.get("fromPln").isNull()) {
                    jobOffer.setSalaryFromPln(employmentNode.get("fromPln").asDouble());
                }
                if (employmentNode.has("toPln")
                        && !employmentNode.get("toPln").isNull()) {
                    jobOffer.setSalaryToPln(employmentNode.get("toPln").asDouble());
                }
            } else {

                if (node.has("salaryFromPln")
                        && !node.get("salaryFromPln").isNull()) {
                    jobOffer.setSalaryFromPln(node.get("salaryFromPln").asDouble());
                }
                if (node.has("salaryToPln")
                        && !node.get("salaryToPln").isNull()) {
                    jobOffer.setSalaryToPln(node.get("salaryToPln").asDouble());
                }
            }
            if (node.has("requiredSkills") && !node.get("requiredSkills").isNull()) {
                List<String> requiredSkills = new ArrayList<>();
                node.get("requiredSkills").forEach(skill -> requiredSkills.add(skill.asText()));
                jobOffer.setRequiredSkills(requiredSkills);
            }
            if (node.has("niceToHaveSkills")
                    && !node.get("niceToHaveSkills").isNull()) {
                List<String> niceToHaveSkills = new ArrayList<>();
                node.get("niceToHaveSkills").forEach(skill -> niceToHaveSkills.add(skill.asText()));
                jobOffer.setNiceToHaveSkills(niceToHaveSkills);
            } else {
                jobOffer.setNiceToHaveSkills(null);
            }
            jobOffer.setWorkplaceType(node.has("workplaceType")
                    && !node.get("workplaceType").isNull() ? node.get("workplaceType").asText() : null);
            jobOffer.setExperienceLevel(node.has("experienceLevel")
                    && !node.get("experienceLevel").isNull() ? node.get("experienceLevel").asText() : null);

            jobOffersList.add(jobOffer);
        }
        return jobOffersList;
    }
}






