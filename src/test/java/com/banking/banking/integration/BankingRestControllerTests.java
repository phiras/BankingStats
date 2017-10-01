package com.banking.banking.integration;

import com.banking.banking.model.Transaction;
import com.banking.banking.statistics.TransactionServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the entire rest api
 *
 * @author Mohamad Alaloush
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BankingRestControllerTests {

    private static final String TRANSACTIONS_END_PONT = "/transactions";
    private static final String STATISTICS_END_PONT = "/statistics";

    private static final long ONE_MINUTE_AGO = System.currentTimeMillis() - TransactionServiceImpl.MINUTE_IN_MILL_SECONDS;
    private static final Transaction OLD_TRANSACTION = new Transaction(3000, new Date(ONE_MINUTE_AGO));
    private static final Transaction NEW_TRANSACTION_MIN_AMOUNT = new Transaction(1000, new Date());
    private static final Transaction NEW_TRANSACTION_MAX_AMOUNT = new Transaction(4000, new Date());
    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    //  mock object to send requests to the controller
    @Autowired
    private MockMvc mvc;

    //  to map objects to json and back
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * flushes the statistics and adds the test objects before each test.
     * 1. the first is new valid with lower amount
     * 2. the second is new valid with higher amount
     */
    @Before
    public void resetStatistics() {
        transactionServiceImpl.resetStatistics();
        transactionServiceImpl.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);
        transactionServiceImpl.registerTransaction(NEW_TRANSACTION_MAX_AMOUNT);
    }

    /**
     * posting a Transaction older than 60 seconds
     *
     * @throws Exception
     * @result response status 204 (No Content)
     */
    @Test
    public void When_PostingOldTransaction_Should_GetNoContentStatus() throws Exception {
        mvc.perform(post(TRANSACTIONS_END_PONT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(OLD_TRANSACTION)))
                .andExpect(status().isNoContent());
    }

    /**
     * @throws Exception
     * @result response status 201 ( Created )
     */
    @Test
    public void When_PostingNewValidTransaction_Should_GetCreatedStatus() throws Exception {
        mvc.perform(post(TRANSACTIONS_END_PONT)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(NEW_TRANSACTION_MAX_AMOUNT)))
                .andExpect(status().isCreated());
    }

    /**
     * get request to get statistics
     *
     * @throws Exception
     * @result status 200 (OK)
     */
    @Test
    public void When_GettingStatistics_Should_GetIsOKStatus() throws Exception {
        mvc.perform(get(STATISTICS_END_PONT)).andExpect(status().isOk());
    }

    /**
     * GET request to get statistics
     *
     * @throws Exception
     * @result should get a json with the correct result ( since we have two current transactions )
     */
    @Test
    public void When_GettingStatistics_Should_GetCorrectJsonResponse() throws Exception {

        MvcResult result = mvc.perform(get(STATISTICS_END_PONT).accept(APPLICATION_JSON_UTF8)).andReturn();

        String response = result.getResponse().getContentAsString();

//      creating a map from the json string to access each field easily
        Map<String, Object> map = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });
//      validating every thing given that we have these two Transactions already registered before each test
        double sum = NEW_TRANSACTION_MIN_AMOUNT.getAmount() + NEW_TRANSACTION_MAX_AMOUNT.getAmount();
        assertThat(map.get("count")).isEqualTo(2);
        assertThat(map.get("sum")).isEqualTo(sum);
        assertThat(map.get("avg")).isEqualTo(sum / 2);
        assertThat(map.get("min")).isEqualTo(NEW_TRANSACTION_MIN_AMOUNT.getAmount());
        assertThat(map.get("max")).isEqualTo(NEW_TRANSACTION_MAX_AMOUNT.getAmount());
    }

}
