package com.ebsolutions.papertrail.financialdataproviderservice.tooling;

import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseTest {
  protected final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  protected MockMvc mockMvc;

  @MockitoBean
  protected UserRepository userRepository;
}
