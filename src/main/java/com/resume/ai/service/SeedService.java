package com.resume.ai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeedService {

    @Autowired
    private DataSource dataSource;

    public int seedJobs(int targetCount) {
        List<JobSeed> seeds = getJobSeeds();
        int inserted = 0;
        try (Connection conn = dataSource.getConnection()) {
            int existing = getCount(conn, "jobs");
            int toInsert = Math.max(0, Math.min(targetCount, seeds.size()) - existing);
            if (toInsert <= 0) {
                return 0;
            }
            String sql = "INSERT INTO jobs (title, description, category, recruiter_id, created_at) VALUES (?, ?, ?, ?, NOW())";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < toInsert; i++) {
                    JobSeed seed = seeds.get(i);
                    ps.setString(1, seed.title);
                    ps.setString(2, seed.description);
                    ps.setString(3, seed.category);
                    ps.setNull(4, Types.BIGINT);
                    ps.addBatch();
                }
                int[] counts = ps.executeBatch();
                for (int c : counts) {
                    inserted += Math.max(0, c);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error seeding jobs", e);
        }
        return inserted;
    }

    public int seedResumesFromCsv(int targetCount) {
        int inserted = 0;
        Path csvPath = resolveDatasetPath();
        if (!Files.exists(csvPath)) {
            throw new RuntimeException("Dataset not found at: " + csvPath.toAbsolutePath());
        }

        try (Connection conn = dataSource.getConnection()) {
            int existing = getCount(conn, "resumes");
            int toInsert = Math.max(0, targetCount - existing);
            if (toInsert <= 0) {
                return 0;
            }

            String sql = "INSERT INTO resumes (file_name, predicted_category, content, candidate_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
                String line;
                boolean isHeader = true;
                int row = 0;
                while ((line = reader.readLine()) != null && inserted < toInsert) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    String[] cols = parseCsvLine(line);
                    if (cols.length < 2) {
                        continue;
                    }
                    String resumeText = cols[0];
                    String category = cols[1];
                    String fileName = "seed_resume_" + (existing + row + 1) + ".txt";

                    ps.setString(1, fileName);
                    ps.setString(2, category);
                    ps.setString(3, resumeText);
                    ps.setNull(4, Types.BIGINT);
                    ps.addBatch();

                    row++;
                    if (row % 100 == 0) {
                        int[] counts = ps.executeBatch();
                        for (int c : counts) {
                            inserted += Math.max(0, c);
                        }
                    }
                }
                int[] counts = ps.executeBatch();
                for (int c : counts) {
                    inserted += Math.max(0, c);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Error seeding resumes", e);
        }

        return inserted;
    }

    private int getCount(Connection conn, String table) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Path resolveDatasetPath() {
        Path base = Paths.get(System.getProperty("user.dir"));
        Path csvPath = base.resolve("..").resolve("dataset").resolve("resumes_augmented.csv").normalize();
        return csvPath;
    }

    private String[] parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (c == ',' && !inQuotes) {
                cols.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        cols.add(sb.toString().trim());
        return cols.toArray(new String[0]);
    }

    private List<JobSeed> getJobSeeds() {
        List<JobSeed> seeds = new ArrayList<>();
        seeds.add(new JobSeed("Data Scientist", "Data Science", "Python, SQL, NLP, ML, Deep Learning, Statistics"));
        seeds.add(new JobSeed("Frontend Developer", "Frontend Developer", "React, JavaScript, HTML, CSS, Tailwind"));
        seeds.add(new JobSeed("Full Stack Developer", "Full Stack Developer", "Node.js, React, SQL, REST API, Docker"));
        seeds.add(new JobSeed("Java Developer", "Java Developer", "Java, Spring Boot, PostgreSQL, REST API"));
        seeds.add(new JobSeed("Python Developer", "Python Developer", "Python, Flask, Django, SQL"));
        seeds.add(new JobSeed("DevOps Engineer", "DevOps Engineer", "Docker, Kubernetes, CI/CD, AWS"));
        seeds.add(new JobSeed("React Developer", "React Developer", "React, Redux, TypeScript, UI/UX"));
        seeds.add(new JobSeed("Backend Developer", "Backend Developer", "Spring Boot, Java, SQL, Microservices"));
        seeds.add(new JobSeed("Data Analyst", "Data Science", "Excel, SQL, Tableau, Power BI"));
        seeds.add(new JobSeed("ML Engineer", "Data Science", "PyTorch, TensorFlow, ML, MLOps"));
        return seeds;
    }

    private static class JobSeed {
        String title;
        String category;
        String description;

        JobSeed(String title, String category, String description) {
            this.title = title;
            this.category = category;
            this.description = description;
        }
    }
}

