package com.parkingsystem.parkingapi.repositories

import com.parkingsystem.parkingapi.configuration.DatasourceProvider
import com.parkingsystem.parkingapi.domain.organizations.Organization
import com.parkingsystem.parkingapi.infrastructure.exceptions.InternalServerException
import com.parkingsystem.parkingapi.infrastructure.exceptions.NotFoundException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.rowmapper.OrganizationCountRowMapper

import com.parkingsystem.parkingapi.repositories.rowmapper.OrganizationRowMapper
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

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_FINDING_ORGANIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_REGISTER_ORGANIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_UPDATE_ORGANIZATION
import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ORGANIZATION_NOT_FOUND

@Component
class OrganizationRepository {

    private static final Logger logger = LoggerFactory.createLogger(OrganizationRepository)

    @Autowired
    DatasourceProvider datasourceProvider

    @Autowired
    OrganizationRowMapper organizationRowMapper

    @Autowired
    OrganizationCountRowMapper organizationCountRowMapper

    @Autowired
    @Qualifier("jdbcScheduler")
    Scheduler scheduler

    static final String INSERT = '''
        INSERT INTO Organizations (
            Name,
            Cost,
            MaximumCapacity,
            CreatedAt
        ) VALUES (
            :Name,
            :Cost,
            :MaximumCapacity,
            :CreatedAt
        )
    '''

    static final String COUNT_BY_NAME = '''
        SELECT 
            if(count(*) > 0, "true", "false") as organizationNameExists
        FROM
            Organizations o
        WHERE
            o.Name = :Name
    '''

    static final String FIND_ALL_ORGANIZATIONS = '''
        SELECT
            o.Organization_ID,
            o.Name,
            o.Cost,
            o.MaximumCapacity,
            o.CreatedAt,
            o.UpdatedAt
        FROM
            Organizations o
    '''

    static final String FIND_BY_ID = '''
        SELECT
            o.Organization_ID,
            o.Name,
            o.Cost,
            o.MaximumCapacity,
            o.CreatedAt,
            o.UpdatedAt
        FROM
            Organizations o
        WHERE
            Organization_ID = :Organization_ID
    '''

    static final String UPDATE = '''
        UPDATE Organizations
        SET Cost            = :Cost,
            MaximumCapacity = :MaximumCapacity,
            UpdatedAt       = :UpdatedAt
        WHERE
            Organization_ID = :Organization_ID
    '''

    Mono<Long> registerOrganization(String name, BigDecimal cost, Integer maximumCapacity) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
            def params = new MapSqlParameterSource('Name', name)
            params.addValue('Cost', cost)
            params.addValue('MaximumCapacity', maximumCapacity)
            params.addValue('CreatedAt', DateUtils.toBrazilGMT())

            String[] arr = ['Organization_ID']
            KeyHolder keyHolder = new GeneratedKeyHolder()

            try {
                jdbcTemplate.update(INSERT, params, keyHolder, arr)
            } catch(Exception ex) {
                logger.createMessage("${this.class.simpleName}.registerOrganization", ERROR_WHILE_REGISTER_ORGANIZATION.message)
                    .error(ex)

                throw new InternalServerException(ERROR_WHILE_REGISTER_ORGANIZATION)
            }

            keyHolder.key.longValue()
        }
    }

    Mono<List<Organization>> findAll() {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
            try {
                jdbcTemplate.query(FIND_ALL_ORGANIZATIONS, organizationRowMapper)
            } catch (Exception e) {
                logger.createMessage("${this.class.simpleName}.findAll", ERROR_WHILE_FINDING_ORGANIZATION.message)
                    .error(e)

                throw new InternalServerException(ERROR_WHILE_FINDING_ORGANIZATION)
            }
        }
    }

    Mono<Organization> findById(Long organizationId) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
            def params = [
                Organization_ID: organizationId
            ]
            try {
                jdbcTemplate.queryForObject(FIND_BY_ID, params, organizationRowMapper)
            } catch (EmptyResultDataAccessException ex) {
                logger.createMessage("${this.class.simpleName}.findById", ORGANIZATION_NOT_FOUND.message)
                    .warn()

                throw new NotFoundException(ORGANIZATION_NOT_FOUND)
            } catch (Exception ex) {
                logger.createMessage("${this.class.simpleName}.findById", ERROR_WHILE_FINDING_ORGANIZATION.message)
                    .error(ex)

                throw new InternalServerException(ERROR_WHILE_FINDING_ORGANIZATION)
            }
        }
    }

    void updateOrganization(Long organizationId, BigDecimal cost, Integer maximumCapacity) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
        def params = new MapSqlParameterSource("Organization_ID", organizationId)
            .addValue("Cost", cost)
            .addValue("MaximumCapacity", maximumCapacity)
            .addValue("UpdatedAt", DateUtils.toBrazilGMT())

        try {
            jdbcTemplate.update(UPDATE, params)
        } catch (Exception ex) {
            logger.createMessage("${this.class.simpleName}.updateOrganization", ERROR_WHILE_UPDATE_ORGANIZATION.message)
                .with('organizationId', organizationId)
                .error(ex)

            throw new InternalServerException(ERROR_WHILE_UPDATE_ORGANIZATION)
        }
    }

    Mono<Boolean> findOrganizationByName(String name) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
            try {
                def params = [
                    Name: name
                ]
                jdbcTemplate.queryForObject(COUNT_BY_NAME, params, organizationCountRowMapper)
            } catch(EmptyResultDataAccessException e){
                return false
            } catch (Exception e) {
                logger.createMessage("${this.class.simpleName}.findOrganizationByName", ERROR_WHILE_FINDING_ORGANIZATION.message)
                    .error(e)

                throw new InternalServerException(ERROR_WHILE_FINDING_ORGANIZATION)
            }
        }
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(scheduler)
    }
}
