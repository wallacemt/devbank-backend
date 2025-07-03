package com.devbank.DevBank.services;

import com.devbank.DevBank.dtos.response.StatusResponseDTO;
import com.devbank.DevBank.repositories.DBStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatusService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private final DBStatusRepository dbRepo;

    public StatusService(DBStatusRepository dbRepo) {
        this.dbRepo = dbRepo;
    }

    @Value("$spring.profiles.active:prod")
    private String contextProfile;

    @Value("${git.commit.id:unknown}")
    private String commitId;

    @Value("${git.commit.user.name:unknown}")
    private String authorName;


    public StatusResponseDTO getStatus() {
        StatusResponseDTO response = new StatusResponseDTO();

        response.setDatabase(getDatabaseStatus());
        response.setServer(getServerStatus());
        response.setGit(getGitInfo());
        return response;
    }

    private Map<String, Object> getDatabaseStatus() {
        Map<String, Object> db = new HashMap<>();

        try {
            db.put("status", "healthy");
            int active = dbRepo.getActiveConnections();
            int total = dbRepo.getTotalConnections();
            int max = dbRepo.getMaxConnections();
            int disponiveis = max - total;
            db.put("conexões ativas", active);
            db.put("conexões abertas", total);
            db.put("conexões disponíveis", disponiveis);

            String version = jdbcTemplate.queryForObject("SELECT version()", String.class);
            db.put("versão", version);

            long start = System.currentTimeMillis();
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            long latency = System.currentTimeMillis() - start;

            db.put("latência", latency + "ms");

        } catch (Exception e) {
            db.put("status", "unhealthy");
            db.put("erro", e.getMessage());
        }

        return db;
    }

    private Map<String, Object> getServerStatus() {
        Map<String, Object> server = new HashMap<>();
        server.put("status", "healthy");
        server.put("provedor", "render");
        server.put("ambiente", contextProfile);
        server.put("timezone", java.time.ZoneId.systemDefault().toString());
        server.put("região_render", System.getenv().getOrDefault("RENDER_REGION", "desconhecida"));
        server.put("timezone", java.time.ZoneId.systemDefault().toString());
        server.put("versão_java", System.getProperty("java.version"));
        return server;
    }

    private Map<String, Object> getGitInfo() {
        Map<String, Object> git = new HashMap<>();
        git.put("autor_último_commit", authorName);
        git.put("sha_commit", commitId);

        // Se quiser puxar isso automaticamente, use `git-commit-id-plugin` no Maven
        return git;
    }
}
