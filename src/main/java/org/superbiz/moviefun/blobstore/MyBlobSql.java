package org.superbiz.moviefun.blobstore;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public class MyBlobSql implements BlobStore{

    private final DataSource dataSource;

    public MyBlobSql(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public void put(Blob blob) throws IOException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.execute();
    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
