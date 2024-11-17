
package com.example.rating.service;

import com.example.rating.entity.Comment;
import com.example.rating.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final CommentRepository commentRepository;

    public ReportService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public String generateWeeklyReport() {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        List<Comment> recentComments = commentRepository.findAll()
            .stream()
            .filter(comment -> comment.getCreatedDate().isAfter(oneWeekAgo.atStartOfDay()))
            .collect(Collectors.toList());

        StringBuilder report = new StringBuilder("Weekly Comments Report:\n");

        Map<Long, List<Comment>> commentsByAlternative = recentComments.stream()
            .collect(Collectors.groupingBy(comment -> comment.getAlternative().getId()));

        for (Map.Entry<Long, List<Comment>> entry : commentsByAlternative.entrySet()) {
            report.append("Alternative ID: ").append(entry.getKey()).append("\n");
            for (Comment comment : entry.getValue()) {
                report.append(" - ").append(comment.getContent()).append("\n");
            }
        }

        return report.toString();
    }
}
