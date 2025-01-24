package conn.in.stress.db.integration.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DatabaseStressMapper {

    void databaseInStress();
}
