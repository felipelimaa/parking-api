package com.parkingsystem.parkingapi.repositories

import com.parkingsystem.parkingapi.configuration.DatasourceProvider
import com.parkingsystem.parkingapi.domain.utilizations.Utilization
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.infrastructure.exceptions.InternalServerException
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.rowmapper.UtilizationRowMapper
import com.parkingsystem.parkingapi.utils.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

import java.util.concurrent.Callable

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_FINDING_UTILIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_REGISTER_UTILIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.UTILIZATION_NOT_FOUND

@Component
class UtilizationRepository {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationRepository)

    @Autowired
    DatasourceProvider datasourceProvider

    @Autowired
    UtilizationRowMapper utilizationRowMapper

    @Autowired
    @Qualifier("jdbcScheduler")
    Scheduler scheduler

    static final String INSERT = '''
        INSERT INTO Utilizations (
            Organization_ID,
            Plate,
            Brand,
            Model,
            InitialParkingDate,
            UtilizationStatus,
            CreatedAt
        ) VALUES (
            :Organization_ID,
            :Plate,
            :Brand,
            :Model,
            :InitialParkingDate,
            :UtilizationStatus,
            :CreatedAt
        )
    '''

    static final String FIND_BY_ID = '''
        SELECT
            u.Utilization_ID,
            u.Organization_ID       as Organization__ID,
            o.Name                  as OrganizationName,
            o.Cost                  as OrganizationCost,
            o.MaximumCapacity       as OrganizationMaximumCapacity,
            u.Plate,
            u.Brand,
            u.Model,
            u.InitialParkingDate,
            u.FinishParkingDate,
            u.Cost,
            u.UtilizationStatus,
            u.CreatedAt,
            u.UpdatedAt
        FROM
            Utilizations u
        INNER JOIN
            Organizations o ON o.Organization_ID = u.Organization_ID
        WHERE
            u.Utilization_ID    = :Utilization_ID
        AND u.Organization_ID   = :Organization__ID
    '''

    Mono<Long> registerUtilization(
        Long organization_ID,
        String plate,
        String brand,
        String model,
        String initialParkingDate,
        String utilizationStatus
    ) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)

            def params = new MapSqlParameterSource('Organization_ID', organization_ID)
                .addValue('Plate', plate)
                .addValue('Brand', brand)
                .addValue('Model', model)
                .addValue('InitialParkingDate', initialParkingDate)
                .addValue('UtilizationStatus', utilizationStatus)
                .addValue('CreatedAt', DateUtils.toBrazilGMT())

            logger.createMessage("${this.class.simpleName}.registerUtilization", "Executing query")
                .with("params", params.values)
                .info()

            String[] arr = ["Utilization_ID"]
            KeyHolder keyHolder = new GeneratedKeyHolder()

            try {
                jdbcTemplate.update(INSERT, params, keyHolder, arr)
            } catch(Exception ex) {
                logger.createMessage("${this.class.simpleName}.registerUtilization", ERROR_WHILE_REGISTER_UTILIZATION.message)
                    .error(ex)

                throw new Error(ERROR_WHILE_REGISTER_UTILIZATION.message, ex)
            }

            keyHolder.key.longValue()
        }
    }

    Mono<Utilization> findById(Long organizationId, Long utilizationId) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
            def params = [
                'Utilization_ID': utilizationId,
                'Organization__ID': organizationId
            ]
            try {
                jdbcTemplate.queryForObject(FIND_BY_ID, params, utilizationRowMapper)
            } catch(EmptyResultDataAccessException ex) {
                logger.createMessage("${this.class.simpleName}.findById", UTILIZATION_NOT_FOUND.message)
                    .warn()

                throw new NotFoundException(UTILIZATION_NOT_FOUND)
            } catch(Exception ex) {
                logger.createMessage("${this.class.simpleName}.findById", ERROR_WHILE_FINDING_UTILIZATION.message)
                        .error(ex)

                throw new InternalServerException(ERROR_WHILE_FINDING_UTILIZATION)
            }
        }
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(scheduler)
    }

}
