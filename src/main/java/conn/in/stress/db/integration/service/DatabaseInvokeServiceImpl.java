package conn.in.stress.db.integration.service;

import conn.in.stress.db.integration.dao.DatabaseStressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInvokeServiceImpl implements DatabaseInvokeService {

    private final DatabaseStressMapper databaseStressMapper;

    @Override
    public void invoke() {
        databaseStressMapper.databaseInStress();
    }
}
